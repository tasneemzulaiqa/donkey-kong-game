import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.Properties;

/**
 * Represents the first level of the game.
 * This class extends the generic Level class and initializes specific
 * objects such as platforms, ladders, barrels, weapons (hammers), and DonkeyKong
 * for level 1. It handles the game loop updates specific to level 1.
 */
public class Level1 extends Level{

    /**
     * Constructs a new Level1 instance.
     * Loads weapons, combines platforms, ladders, barrels, and DonkeyKong into a
     * unified object array for level 1.
     *
     * @param gameProps Properties object containing game configuration values.
     * @param levelId   The identifier for this level.
     */
    public Level1(Properties gameProps, int levelId) {

        super(gameProps, 1);
        getWeapons().addAll(ObjectLoader.loadHammer(gameProps, getPlatforms(), 1));
        // Set combined objects (platforms, ladders, barrels, donkey)
        setObjects(ObjectLoader.combineObjects(new GameObject[][]{
                getPlatforms(),
                getLadders(),
                getBarrels(),
                new GameObject[]{getDonkeyKong()}
        }));
    }

    /**
     * Updates the game state for Level 1.
     * This includes rendering the background, updating the remaining time,
     * applying gravity to objects, drawing weapons, updating and drawing Mario,
     * and performing various collision and scoring checks.
     * If the time runs out, the game over state is set.
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

        // draw objects
        applyGravityAndRenderObjects();
        drawWeapons();
        getMario().update(input, getPlatforms(), getLadders());
        getMario().draw();

        // check for collisions
        Rectangle marioBox = getMario().getImage().getBoundingBoxAt(new Point(getMario().getX(), getMario().getY()));
        checkMarioAndWeaponCollision(marioBox);
        checkMarioHammerEnemyCollisions(marioBox);
        checkMarioJumpScore(marioBox);
        addJumpScoreAfterLanding();
    }

    /**
     * Returns the next level in the game sequence after Level 1.
     *
     * @param props The game properties.
     * @return A new Level2 instance representing the next level.
     */
    @Override
    public Level getNextLevel(Properties props) {
        return new Level2(props, 2);
    }
}
