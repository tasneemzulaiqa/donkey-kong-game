import bagel.Image;

/**
 * Represents a Banana thrown by SmartMonkey
 * The banana travels horizontally in the specified direction and disappears
 * after covering a certain maximum distance.
 */
public class Banana extends Projectile {
    private static final Image BANANA = new Image("res/banana.png");
    private static final double SPEED = 1.8;

    /**
     * Constructs a Banana object at the given position with a specified direction.
     *
     * @param x          The initial x-coordinate of the banana.
     * @param y          The initial y-coordinate of the banana.
     * @param facingLeft True if the banana should move left; false for right.
     */
    public Banana(double x, double y, boolean facingLeft){
        super(x,y, facingLeft, SPEED);
    }

    /**
     * Draws the banana image at its current position if it is still active.
     */
    @Override
    public void draw() {
        if (isActive()) {
            BANANA.draw(x, y);
        }
    }

    /**
     * Returns the image associated with this banana.
     *
     * @return banana image.
     */
    @Override
    public Image getImage() {
        return BANANA;
    }
}
