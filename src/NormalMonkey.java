import bagel.*;

/**
 * A concrete implementation of Monkey representing a normal monkey enemy.
 * The normal monkey follows a predefined route and displays directional sprites based on its movement.
 */
public class NormalMonkey extends Monkey {
    private static final Image NORMAL_MONKEY_RIGHT = new Image("res/normal_monkey_right.png");
    private static final Image NORMAL_MONKEY_LEFT = new Image("res/normal_monkey_left.png");

    /**
     * Constructs a NormalMonkey with given position, facing direction, movement route, and platform references.
     *
     * @param x          The initial x-coordinate of the monkey.
     * @param y          The initial y-coordinate of the monkey.
     * @param facingLeft Whether the monkey starts by facing left.
     * @param route      Array of pixel distances that the monkey moves.
     * @param platforms  The platforms available in the level for edge detection and grounding.
     */
    public NormalMonkey(double x, double y, boolean facingLeft, int[] route, Platform[] platforms){
        super(x, y, facingLeft, route, platforms);
    }

    /**
     * Draws the monkey on screen using the correct directional sprite.
     * Only draws the monkey if it is not destroyed.
     */
    public void draw() {
        if (!isDestroyed()) {
            getImage().draw(x, y);
        }
    }

    /**
     * Retrieves the current image of the monkey based on its facing direction.
     *
     * @return The {@link Image} representing the monkey's current orientation.
     */
    public Image getImage() {
        return (isFacingLeft() ? NORMAL_MONKEY_LEFT : NORMAL_MONKEY_RIGHT);
    }
}