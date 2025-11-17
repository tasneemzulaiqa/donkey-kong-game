import bagel.*;
import java.util.Properties;

/**
 * The main class for the Shadow Donkey Kong game.
 * This class extends {@code AbstractGame} and is responsible for managing game initialization,
 * updates, rendering, and handling user input.
 *
 * It sets up the game world, initializes characters, platforms, ladders, and other game objects,
 * and runs the game loop to ensure smooth gameplay.
 */
public class ShadowDonkeyKong extends AbstractGame {

    private final Properties GAME_PROPS;
    private final Properties MESSAGE_PROPS;
    private final GameScreen screen;
    private final Game game;
    public static int currentFrame = 0;

    public ShadowDonkeyKong(Properties gameProps, Properties messageProps) {
        super(Integer.parseInt(gameProps.getProperty("window.width")),
                Integer.parseInt(gameProps.getProperty("window.height")),
                messageProps.getProperty("home.title"));

        this.GAME_PROPS = gameProps;
        this.MESSAGE_PROPS = messageProps;
        this.screen = new GameScreen(gameProps);
        this.game = new Game(gameProps);
    }

    /**
     * Render the relevant screen based on the keyboard input given by the user and the status of the gameplay.
     * @param input The current mouse/keyboard input.
     */
    @Override
    protected void update(Input input) {
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }
        switch (screen.getCurrentStatus()) {
            case GameScreen.HOME:
                screen.renderHomeScreen(GAME_PROPS, MESSAGE_PROPS);
                if (input.wasPressed(Keys.ENTER)) {
                    game.startLevel(1);
                    screen.setCurrentStatus(GameScreen.PLAY);
                } else if (input.wasPressed(Keys.NUM_2)){
                    game.startLevel(2);
                    screen.setCurrentStatus(GameScreen.PLAY);
                }
                break;

            case GameScreen.PLAY:
                currentFrame++;
                game.update(input, screen, currentFrame);
                break;

            case GameScreen.GAME_OVER:
                screen.renderGameEndScreen(GAME_PROPS, MESSAGE_PROPS, game, false);
                if (input.wasPressed(Keys.SPACE)) {
                    resetToHome();
                }
                break;

            case GameScreen.WIN:
                screen.renderGameEndScreen(GAME_PROPS, MESSAGE_PROPS, game, true);
                if (input.wasPressed(Keys.SPACE)) {
                    resetToHome();
                }
                break;
        }
    }

    /**
     * Resets the game state to the home screen.
     * Resets the current frame count and the total game score.
     */
    private void resetToHome() {
        screen.setCurrentStatus(GameScreen.HOME);
        currentFrame = 0;
        game.setTotalScore(0);
    }

    /**
     * The main entry point of the Shadow Donkey Kong game.
     *
     * This method loads the game properties and message files, initializes the game,
     * and starts the game loop.
     *
     * @param args Command-line arguments (not used in this game).
     */
    public static void main(String[] args) {
        Properties gameProps = IOUtils.readPropertiesFile("res/app.properties");
        Properties messageProps = IOUtils.readPropertiesFile("res/message.properties");
        ShadowDonkeyKong game = new ShadowDonkeyKong(gameProps, messageProps);
        game.run();
    }
}
