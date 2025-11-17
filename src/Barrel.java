import bagel.*;

/**
 * Represents a barrel object in the game.
 * Barrels are affected by gravity and can be destroyed.
 */
public class Barrel extends GameObject implements AffectedByGravity, Destroyable {

    private static final Image BARREL = new Image("res/barrel.png");
    private boolean isDestroyed = false;
    private final Motion motion;
    private static final double BARREL_GRAVITY = 0.4;
    private static final double BARREL_TERMINAL_VELOCITY = 5;

    /**
     * Constructs a new Barrel object at the specified coordinates.
     *
     * @param x The x-coordinate of the barrel.
     * @param y The y-coordinate of the barrel.
     */
    public Barrel(double x, double y) {
        super(x, y);
        this.motion = new Motion(BARREL_GRAVITY, BARREL_TERMINAL_VELOCITY);
    }

    /**
     * Draws the barrel on screen if it is not destroyed.
     */
    @Override
    public void draw() {
        if (!isDestroyed) {
            BARREL.draw(x, y);
        }
    }

    /**
     * Returns the image representing the barrel.
     *
     * @return The {@link Image} of the barrel.
     */
    public Image getImage() {
        return BARREL;
    }

    /**
     * Applies gravity to the barrel.
     *
     * @param platforms An array of Platform objects that the barrel may land on.
     */
    @Override
    public void applyGravity(Platform[] platforms) {
        if (!isDestroyed) {
            motion.applyGravity(this, platforms);
        }
    }

    /**
     * Marks the barrel as destroyed so it no longer updates or renders.
     */
    public void destroy() {
        isDestroyed = true;

    }

    /**
     * Returns whether the barrel has been destroyed.
     *
     * @return true if the barrel is destroyed, false otherwise.
     */
    public boolean isDestroyed() {
        return isDestroyed;
    }
}

