import bagel.*;
import java.util.Properties;

/**
 * Acts as game manager which manages level progression and total score tracking.
 */
public class Game {
    private Level currentLevel;
    private final Properties props;
    private int totalScore = 0;

    /**
     * Creates a new Game instance with the given game properties.
     * @param props  The game properties loaded from config file.
     */
    public Game(Properties props) {
        this.props = props;
    }

    /**
     * Updates the current game state based on input and frame count.
     * Handles transitions between levels and game states such as WIN and GAME_OVER.
     *
     * @param input         The current input from the player.
     * @param screen        The current game screen controller.
     * @param currentFrame  The current frame number of the game loop.
     */
    public void update(Input input, GameScreen screen, int currentFrame) {

        if (currentLevel.isLevelCompleted()) {
            if (currentLevel.getNextLevel(props) == null) {
                totalScore += currentLevel.getTotalScoreWithBonus();
                screen.setCurrentStatus(GameScreen.WIN);
            } else {
                totalScore += currentLevel.getGameScore();
                currentLevel = currentLevel.getNextLevel(props);
                screen.setCurrentStatus(GameScreen.PLAY);
            }
        } else if (currentLevel.isGameOver()) {
            totalScore = 0;
            screen.setCurrentStatus(GameScreen.GAME_OVER);
        }
        currentLevel.update(input, currentFrame);
        screen.renderGamePlayTexts(props, currentLevel, this);
    }

    /**
     * Starts the game at the given level number.
     *
     * @param levelNumber The number of the level to start
     */

    public void startLevel(int levelNumber) {
        if (levelNumber == 1) {
            currentLevel = new Level1(props, 1 );
        } else if (levelNumber == 2) {
            currentLevel = new Level2(props, 2);
        }
    }

    /**
     * Gets the total score accumulated across levels.
     *
     * @return The total score.
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * Sets the total score.
     *
     * @param totalScore The score to set.
     */
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
}
