import bagel.Font;
import bagel.Image;
import bagel.Window;
import java.util.Properties;

/**
 * Handles rendering different screens of the game (home, gameplay, and end screens).
 */
public class GameScreen {

    private int currentStatus;

    /** Background image for all screens. */
    public static final Image BACKGROUND = new Image("res/background.png");

    /** Status representing the home screen */
    public static final int HOME = 0;

    /** Status representing the gameplay screen.*/
    public static final int PLAY = 1;

    /** Status representing the game over screen.*/
    public static final int GAME_OVER = 2;

    /** Status representing the win screen. */
    public static final int WIN = 3;

    /**
     * Constructs a GameScreen and sets the initial status to HOME.
     *
     * @param gameProps Game configuration properties
     */
    public GameScreen(Properties gameProps) {
        this.currentStatus = HOME;
    }

    /**
     * Renders the home screen with title and prompt messages.
     *
     * @param gameProps    Properties for fonts and layout.
     * @param messageProps Properties for text content.
     */
    public void renderHomeScreen(Properties gameProps, Properties messageProps) {
        BACKGROUND.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);

        String title = messageProps.getProperty("home.title");
        int titleFontSize = Integer.parseInt(gameProps.getProperty("home.title.fontSize"));
        int titleY = Integer.parseInt(gameProps.getProperty("home.title.y"));

        String prompt = messageProps.getProperty("home.prompt");
        int promptFontSize = Integer.parseInt(gameProps.getProperty("home.prompt.fontSize"));
        int promptY = Integer.parseInt(gameProps.getProperty("home.prompt.y"));

        String fontPath = gameProps.getProperty("font");
        Font titleFont = new Font(fontPath, titleFontSize);
        Font promptFont = new Font(fontPath, promptFontSize);

        double centerX = Window.getWidth() / 2.0;

        titleFont.drawString(title, centerX - titleFont.getWidth(title) / 2, titleY);
        promptFont.drawString(prompt, centerX - promptFont.getWidth(prompt) / 2, promptY);
    }

    /**
     * Renders the game end screen (win or lose) with a final score and prompt to continue.
     *
     * @param gameProps    Properties for fonts and layout.
     * @param messageProps Properties for text content.
     * @param game         The current Game object for retrieving the final score.
     * @param isWin        True if the game was won, false if lost.
     */
    public void renderGameEndScreen(Properties gameProps, Properties messageProps, Game game, boolean isWin) {

        BACKGROUND.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);

        // display win or lose message depending on status
        String statusMessage = isWin ? messageProps.getProperty("gameEnd.won") : messageProps.getProperty("gameEnd.lost");
        int statusFontSize = Integer.parseInt(gameProps.getProperty("gameEnd.status.fontSize"));
        int statusY = Integer.parseInt(gameProps.getProperty("gameEnd.status.y"));
        String fontPath = gameProps.getProperty("font");

        Font statusFont = new Font(fontPath, statusFontSize);
        double centerX = Window.getWidth() / 2.0;
        statusFont.drawString(statusMessage, centerX - statusFont.getWidth(statusMessage) / 2, statusY);

        // Prompt to continue
        String prompt = messageProps.getProperty("gameEnd.continue");
        int promptY = Window.getHeight() - 100;
        statusFont.drawString(prompt, centerX - statusFont.getWidth(prompt) / 2, promptY);

        // Final score
        int finalScore = game.getTotalScore();
        String scoreMessage = messageProps.getProperty("gameEnd.score");
        int scoreFontSize = Integer.parseInt(gameProps.getProperty("gameEnd.scores.fontSize"));
        Font scoreFont = new Font(fontPath, scoreFontSize);
        String finalScoreText = scoreMessage + " " + finalScore;
        scoreFont.drawString(finalScoreText, centerX - scoreFont.getWidth(finalScoreText) / 2, statusY + 60);
    }

    /**
     * Renders gameplay elements such as score, timer, Donkey Kong health, and bullet count.
     *
     * @param gameProps Properties for fonts and layout.
     * @param level     The current level being played.
     * @param game      Game instance for score tracking.
     */
    public void renderGamePlayTexts(Properties gameProps, Level level, Game game) {
        // Display score
        int gameScore =  game.getTotalScore() + level.getGameScore();
        String scoreText = "Score " + gameScore;
        String fontPath = gameProps.getProperty("font");
        int gamePlayFontSize = Integer.parseInt(gameProps.getProperty("gamePlay.score.fontSize"));
        Font gamePlayFont = new Font(fontPath, gamePlayFontSize);

        int scoreX = Integer.parseInt(gameProps.getProperty("gamePlay.score.x"));
        int scoreY = Integer.parseInt(gameProps.getProperty("gamePlay.score.y"));
        gamePlayFont.drawString(scoreText, scoreX, scoreY);

        // Display timer
        int remainingTime = level.getRemainingTime();
        String remainingTimeText = "Time Left " + remainingTime;
        gamePlayFont.drawString(remainingTimeText, scoreX, scoreY + 30);

        // Display donkey health
        int donkeyHealth = level.getDonkeyKong().getHealth();
        String healthText = "DONKEY HEALTH " + donkeyHealth;
        String[] coords = gameProps.getProperty("gamePlay.donkeyhealth.coords").split(",");
        int healthX = Integer.parseInt(coords[0]);
        int healthY = Integer.parseInt(coords[1]);
        gamePlayFont.drawString(healthText, healthX, healthY);

        // Display bullet count
        if (level instanceof Level2) {
            Mario mario = level.getMario();
            int bullets = mario.getBulletCount();
            String bulletText = "BULLET " + bullets;
            int bulletY = healthY + 30;
            gamePlayFont.drawString(bulletText, healthX, bulletY);
        }
    }

    /**
     * Gets the current screen status
     *
     * @return The current status as an integer constant.
     */
    public int getCurrentStatus() {
        return currentStatus;
    }

    /**
     * Sets the current screen status.
     *
     * @param status The new screen status.
     */
    public void setCurrentStatus(int status) {
        this.currentStatus = status;
    }
}
