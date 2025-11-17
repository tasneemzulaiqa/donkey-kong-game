import bagel.*;
import bagel.util.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Represents the second level of the game.
 * This level adds Monkey enemies alongside the existing platforms, ladders, barrels, and DonkeyKong.
 * It handles movement, gravity, collision detection with Mario's weapons and bullets,
 * banana projectiles, scoring, and level completion.
 */
public class Level2 extends Level {

    private final ArrayList<Monkey> monkeys;
    private static final int DESTROY_MONKEY_SCORE = 100;

    /**
     * Constructs a new Level2 instance and loads objects involved in level 2
     *
     * @param gameProps Properties object containing game configuration values.
     * @param levelId   The identifier for this level.
     */
    public Level2(Properties gameProps, int levelId) {
        super(gameProps, 2);
        this.monkeys = ObjectLoader.loadMonkeys(gameProps, getPlatforms(), 2);
        getWeapons().addAll(ObjectLoader.loadHammer(gameProps, getPlatforms(), 2));
        getWeapons().addAll(ObjectLoader.loadBlaster(gameProps, getPlatforms(), 2));
        setObjects(ObjectLoader.combineObjects(new GameObject[][]{
                getPlatforms(), getLadders(), getBarrels(), new GameObject[]{getDonkeyKong()}
        }));
    }

    /**
     * Updates the game state for Level 2.
     * This method draws the background, updates remaining time, applies gravity,
     * moves monkeys, updates Mario, checks collisions with weapons, bananas,
     * bullets, handles scoring, and checks win conditions.
     *
     * @param input        The current user input.
     * @param currentFrame The current frame count of the game loop.
     */
    @Override
    public void update(Input input, int currentFrame) {

        getBackground().draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);

        // update time and end game if time reaches 0
        updateRemainingTime(currentFrame);
        if (getRemainingTime() <= 0) {
            setGameOver(true);
            return;
        }

        // draw objects and apply gravity
        applyGravityAndRenderObjects();
        drawWeapons();

        for (Monkey monkey : monkeys) {
            monkey.applyGravity(getPlatforms());
            if (monkey instanceof SmartMonkey) {
                ((SmartMonkey) monkey).update(currentFrame);
            } else {
                monkey.move();
            }
            monkey.draw();
        }

        getMario().update(input, getPlatforms(), getLadders());
        getMario().draw();

        // check for collisions
        Rectangle marioBox = getMario().getImage().getBoundingBoxAt(new Point(getMario().getX(), getMario().getY()));
        checkMarioAndWeaponCollision(marioBox);
        checkMarioHammerEnemyCollisions(marioBox);
        checkMarioBananaCollisions(marioBox);
        checkBulletCollisions();
        checkMarioJumpScore(marioBox);
        addJumpScoreAfterLanding();

        // win condition
        if (getDonkeyKong().isDestroyed() && !isLevelCompleted()) {
            setLevelCompleted(true);
        }
    }

    /**
     * Checks for collisions between Mario (with hammer) and enemies including monkeys.
     * If Mario hits a monkey with the hammer, the monkey is destroyed and score is increased.
     * If Mario collides without hammer, game over is triggered.
     *
     * @param marioBox The bounding box of Mario.
     */
    @Override
    public void checkMarioHammerEnemyCollisions(Rectangle marioBox) {
        super.checkMarioHammerEnemyCollisions(marioBox);

        // handle monkey collisions
        for (Monkey monkey : monkeys) {
            if (!monkey.isDestroyed() && marioBox.intersects(monkey.getImage().getBoundingBoxAt(new Point(monkey.getX(), monkey.getY())))) {
                if (getMario().getCurrentWeapon() instanceof Hammer) {
                    monkey.destroy();
                    setGameScore(getGameScore() + DESTROY_MONKEY_SCORE);
                } else {
                    setGameOver(true);
                    return;
                }
            }
        }
    }

    /**
     * Checks collisions between Mario and bananas thrown by SmartMonkeys.
     * If Mario intersects with an active banana, the game is set to over.
     *
     * @param marioBox The bounding box of Mario.
     */
    public void checkMarioBananaCollisions(Rectangle marioBox) {
        for (Monkey monkey : monkeys) {
            if (monkey.isDestroyed()) continue;

            if (monkey instanceof SmartMonkey) {
                for (Banana banana : ((SmartMonkey) monkey).getBananas()) {
                    if (banana.isActive() && marioBox.intersects(
                            banana.getImage().getBoundingBoxAt(new Point(banana.getX(), banana.getY())))) {
                        setGameOver(true);
                        return;
                    }
                }
            }
        }
    }

    /**
     * Checks collisions between bullets fired by Mario and enemies(Monkeys and DonkeyKong).
     * Bullets deactivate on collision and score increases if monkey is destroyed.
     * DonkeyKong health reduces if hit by bullet.
     */
    public void checkBulletCollisions() {
        for (Bullet bullet : getMario().getBullets()) {
            if (!bullet.isActive()) continue;
            Rectangle bulletBox = bullet.getImage().getBoundingBoxAt(new Point(bullet.getX(), bullet.getY()));

            // Check platform collisions first
            for (Platform platform : getPlatforms()) {
                Rectangle platformBox = platform.getImage().getBoundingBoxAt(new Point(platform.getX(), platform.getY()));
                if (platformBox.intersects(bulletBox)) {
                    bullet.setActive(false);
                    break;
                }
            }

            if (!bullet.isActive()) continue;

            // Check monkey collisions
            for (Monkey monkey : monkeys) {
                if (monkey.isDestroyed()) continue;
                Rectangle monkeyBox = monkey.getImage().getBoundingBoxAt(new Point(monkey.getX(), monkey.getY()));

                if (monkeyBox.intersects(bulletBox)) {
                    monkey.destroy();
                    bullet.setActive(false);
                    setGameScore(getGameScore() + DESTROY_MONKEY_SCORE);
                    break;
                }
            }

            // Check Donkey Kong collision if bullet still active
            if (bullet.isActive()) {
                Rectangle donkeyBox = getDonkeyKong().getImage().getBoundingBoxAt(new Point(getDonkeyKong().getX(), getDonkeyKong().getY()));

                if (donkeyBox.intersects(bulletBox)) {
                    if (!getDonkeyKong().isDestroyed()) {
                        getDonkeyKong().reduceHealth();
                        bullet.setActive(false);
                    }
                }
            }

        }
    }

    /**
     * Returns the next level after Level 2.
     * Since Level 2 is the last level, this returns null to indicate the end of the game.
     *
     * @param props The game properties.
     * @return null as there is no next level.
     */
    @Override
    public Level getNextLevel(Properties props) {
        return null;
    }
}
