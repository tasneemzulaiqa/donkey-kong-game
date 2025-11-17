import bagel.Image;

/**
 * Represents the Blaster weapon in the game.
 * The Blaster provides a fixed number of bullets when collected.
 */
public class Blaster extends Weapon {
    private static final Image BLASTER = new Image("res/blaster.png");
    private final int BULLET_PER_BLASTER = 5;
    private final int bulletCount;

    /**
     * Constructs a Blaster weapon at the specified coordinates with a default bullet count.
     *
     * @param x The x-coordinate of the Blaster.
     * @param y The y-coordinate of the Blaster.
     */
    public Blaster(double x, double y){
        super(x,y);
        this.bulletCount = BULLET_PER_BLASTER;
    }

    /**
     * Returns the number of bullets available in the Blaster.
     *
     * @return The bullet count.
     */
    public int getBulletCount() {
        return bulletCount;
    }

    /**
     * Draws the Blaster at its position if it has not been collected.
     */
    @Override
    public void draw() {
        if (!isCollected()){
            BLASTER.draw(x,y);
        }
    }

    /**
     * Returns the image used to represent the Blaster.
     *
     * @return The Blaster image.
     */
    @Override
    public Image getImage() {
        return BLASTER;
    }
}
