import bagel.Image;

/**
 * Represents a Hammer weapon in the game.
 * Extends the Weapon class and provides drawing and image retrieval functionality.
 */
public class Hammer extends Weapon{
    private static final Image HAMMER = new Image("res/hammer.png");

    /**
     * Constructs a Hammer at the specified (x, y) position.
     *
     * @param x the x-coordinate of the hammer
     * @param y the y-coordinate of the hammer
     */
    public Hammer(double x, double y){
        super(x, y);
    }

    /**
     * Draws the hammer image on screen if it has not been collected.
     */
    @Override
    public void draw() {
        if (!isCollected()) {
            HAMMER.draw(x, y);
        }
    }

    /**
     * Returns the image representing the hammer.
     *
     * @return the hammer image
     */
    public Image getImage() {
        return HAMMER;
    }
}

