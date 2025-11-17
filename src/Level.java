import bagel.Image;
import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Abstract base class representing a level in the game.
 * Handles loading, updating, and rendering of game components
 */
public abstract class Level {
    private final Image background;
    private final Mario mario;
    private final ArrayList<Weapon> weapons;
    private final DonkeyKong donkey;
    private final Platform[] platforms;
    private final Ladder[] ladders;
    private final Barrel[] barrels;
    private final Properties gameProps;
    private GameObject[] objects;
    private int remainingTime;
    private boolean levelCompleted;
    private int gameScore = 0;
    private boolean isGameOver = false;


    /** Score awarded for destroying a barrel. */
    public static final int DESTROY_BARREL_SCORE = 100;

    /** Score awarded for jumping over a barrel. */
    public static final int JUMP_SCORE = 30;

    /** Threshold in pixels to check if Mario has jumped close to a barrel's center. */
    public static final int CROSS_MIDDLE_THRESHOLD = 30;

    /** Multiplier used to convert remaining time into bonus score. */
    public static final int TIME_BONUS_MULTIPLIER = 3;

    /**
     * Constructs a new Level by loading all required game objects from the given properties.
     *
     * @param gameProps The properties file containing level data.
     * @param levelId   The level number to load.
     */
    public Level(Properties gameProps, int levelId) {
        this.gameProps = gameProps;
        this.background = new Image("res/background.png");

        // Load components for the given level
        this.platforms = ObjectLoader.loadPlatforms(gameProps, levelId);
        this.ladders = ObjectLoader.loadLadders(gameProps, platforms, levelId);
        this.barrels = ObjectLoader.loadBarrels(gameProps, platforms, levelId);
        this.mario = ObjectLoader.loadMario(gameProps, platforms, levelId);
        this.donkey = ObjectLoader.loadDonkeyKong(gameProps, platforms, levelId);
        this.weapons = new ArrayList<>();
    }

    /**
     * Abstract method for updating the level.
     *
     * @param input        The current keyboard input.
     * @param currentFrame The current game frame.
     */
    public abstract void update(Input input, int currentFrame);

    /**
     * Abstract method for retrieving the next level.
     *
     * @param props The properties object.
     * @return The next Level instance.
     */
    public abstract Level getNextLevel(Properties props);

    /**
     * Updates the remaining time based on the current frame.
     *
     * @param currentFrame The current frame of the game.
     */
    public void updateRemainingTime(int currentFrame) {
        int maxFrames = Integer.parseInt(gameProps.getProperty("gamePlay.maxFrames"));
        remainingTime = (maxFrames - currentFrame) / 60;
    }

    /**
     * Applies gravity to objects and renders them.
     */
    public void applyGravityAndRenderObjects() {
        for (GameObject obj : objects) {
            if (obj instanceof AffectedByGravity) {
                ((AffectedByGravity) obj).applyGravity(platforms);
            }
            obj.draw();
        }
    }

    /**
     * Checks collision between Mario and weapons.
     *
     * @param marioBox The bounding box of Mario.
     */
    public void checkMarioAndWeaponCollision(Rectangle marioBox) {

        // Check if Mario and weapon intersecting and weapon has not been collected
        for (Weapon weapon : weapons) {
            if (!weapon.isCollected() &&
                    marioBox.intersects(weapon.getImage().getBoundingBoxAt(new Point(weapon.getX(), weapon.getY())))) {

                // If it's a Blaster, add bullets from new blaster
                if (weapon instanceof Blaster newBlaster) {
                    if (mario.getCurrentWeapon() instanceof Blaster) {
                        mario.setBulletCount(mario.getBulletCount() + newBlaster.getBulletCount());
                    } else {
                        mario.setCurrentWeapon(newBlaster);
                        mario.setBulletCount(newBlaster.getBulletCount());
                    }
                }

                // If it's a Hammer, set current weapon as hammer and bullet count to 0
                else if (weapon instanceof Hammer) {
                    mario.setCurrentWeapon(weapon);
                    mario.setBulletCount(0);
                }
                weapon.collect();
            }
        }
    }

    /**
     * Checks collision between Mario and enemies (barrels or DonkeyKong) when holding a hammer.
     *
     * @param marioBox The bounding box of Mario.
     */
    public void checkMarioHammerEnemyCollisions(Rectangle marioBox) {
        Weapon currentWeapon = mario.getCurrentWeapon();

        // Check barrel collisions
        for (Barrel barrel : barrels) {
            if (!barrel.isDestroyed() && marioBox.intersects(barrel.getImage().getBoundingBoxAt(new Point(barrel.getX(), barrel.getY())))) {
                if (currentWeapon instanceof Hammer) {
                    barrel.destroy();
                    gameScore += DESTROY_BARREL_SCORE;
                } else {
                    setGameOver(true);
                    return;
                }
            }
        }

        // Check Donkey Kong collision
        Rectangle donkeyBox = donkey.getImage().getBoundingBoxAt(new Point(donkey.getX(), donkey.getY()));
        if (marioBox.intersects(donkeyBox)) {
            if (currentWeapon instanceof Hammer) {
                donkey.setHealth(0);
                levelCompleted = true;
            } else {
                setGameOver(true);
            }
        }
    }

    /**
     * Checks if Mario performed a valid jump over a barrel and is eligible for points.
     *
     * @param marioBox The bounding box of Mario.
     */
    public void checkMarioJumpScore(Rectangle marioBox) {
        boolean isFalling = mario.getVerticalVelocity() >= 0;
        if (!mario.isOnGround() && isFalling && !mario.hasScoredThisJump()) {
            for (Barrel barrel : barrels) {
                if (!barrel.isDestroyed()) {
                    checkAndScoreJumpForBarrel(barrel, marioBox);
                }
            }
        }
    }

    /**
     * Helper method that checks if Mario is positioned correctly over a specific barrel
     * to earn jump points.
     *
     * @param barrel   The barrel to check.
     * @param marioBox The bounding box of Mario.
     */
    private void checkAndScoreJumpForBarrel(Barrel barrel, Rectangle marioBox) {
        Rectangle barrelBox = barrel.getImage().getBoundingBoxAt(new Point(barrel.getX(), barrel.getY()));

        double marioX = mario.getX();
        double barrelX = barrel.getX();

        // Check if Mario is close to the middle of the barrel
        boolean crossedMiddle = Math.abs(marioX - barrelX) < CROSS_MIDDLE_THRESHOLD;

        // Look for the closest platform above the barrel
        double barrelTop = barrelBox.top();
        double ceilingY = 0;

        for (Platform platform : platforms) {
            Rectangle platformBox = platform.getImage().getBoundingBoxAt(new Point(platform.getX(), platform.getY()));
            boolean containsX = platformBox.left() <= barrelX && barrelX <= platformBox.right();
            boolean isAboveBarrel = platformBox.bottom() <= barrelTop;

            if (containsX && isAboveBarrel && platformBox.bottom() > ceilingY) {
                ceilingY = platformBox.bottom();
            }
        }

        // Check if Mario is between the barrel and the ceiling platform
        boolean withinVerticalRange = marioBox.bottom() >= ceilingY && marioBox.top() <= barrelTop;

        if (crossedMiddle && withinVerticalRange) {
            mario.setHasScoredThisJump(true);
        }
    }

    /**
     * Adds score if Mario has completed a jump successfully.
     */
    public void addJumpScoreAfterLanding() {
        if (mario.isOnGround() && mario.hasScoredThisJump()) {
            gameScore += JUMP_SCORE;
            mario.setHasScoredThisJump(false);
        }
    }

    /**
     * Draws all uncollected weapons in the level.
     */
    public void drawWeapons(){
        for (Weapon weapon : weapons) {
            weapon.draw();
        }
    }

    /**
     * @return The Mario instance for this level.
     */
    public Mario getMario() {
        return mario;
    }

    /**
     * Sets the array of objects managed by the level.
     *
     * @param objects Array of GameObjects.
     */
    public void setObjects(GameObject[] objects) {
        this.objects = objects;
    }

    /**
     * @return List of weapons in the level.
     */
    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    /**
     * @return Array of platforms in the level.
     */
    public Platform[] getPlatforms() {
        return platforms;
    }

    /**
     * @return Array of ladders in the level.
     */
    public Ladder[] getLadders() {
        return ladders;
    }

    /**
     * @return Array of barrels in the level.
     */
    public Barrel[] getBarrels() {
        return barrels;
    }

    /**
     * @return DonkeyKong instance in this level.
     */
    public DonkeyKong getDonkeyKong() {
        return donkey;
    }

    /**
     * @return The background image for this level.
     */
    public Image getBackground() {
        return background;
    }

    /**
     * @return Current score of the level.
     */
    public int getGameScore() {
        return gameScore;
    }

    /**
     * Sets the current score of the level.
     *
     * @param gameScore The new score.
     */
    public void setGameScore(int gameScore) {
        this.gameScore = gameScore;
    }

    /**
     * @return Remaining time in the level (in seconds).
     */
    public int getRemainingTime() {
        return remainingTime;
    }

    /**
     * @return Total score including time bonus.
     */
    public int getTotalScoreWithBonus() {
        return gameScore + (remainingTime * TIME_BONUS_MULTIPLIER);
    }

    /**
     * @return True if the game is over.
     */
    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * Sets whether the game is over.
     *
     * @param gameOver True if the game is over.
     */
    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    /**
     * @return True if the level has been completed.
     */
    public boolean isLevelCompleted() {
        return levelCompleted;
    }

    /**
     * Sets whether the level has been completed.
     *
     * @param levelCompleted True if level is completed.
     */
    public void setLevelCompleted(boolean levelCompleted) {
        this.levelCompleted = levelCompleted;
    }
}


