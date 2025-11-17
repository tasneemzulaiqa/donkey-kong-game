import bagel.*;

/**
 * Represents the Donkey Kong enemy in the game.
 * Donkey Kong is a stationary enemy with health that can be reduced.
 * When health reaches 0, Donkey Kong is marked as destroyed.
 */
public class DonkeyKong extends Enemy implements AffectedByGravity, Destroyable {
    private static final Image DONKEY_KONG = new Image("res/donkey_kong.png");
    private static final int DEFAULT_HEALTH = 5;
    private int health;

    /**
     * Constructs a new DonkeyKong at the specified position with default health.
     *
     * @param x The x-coordinate of Donkey Kong.
     * @param y The y-coordinate of Donkey Kong.
     */
    public DonkeyKong(double x, double y) {
        super(x, y);
        this.health = DEFAULT_HEALTH;
    }

    /**
     * Returns the current health of Donkey Kong.
     *
     * @return The current health value.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Sets Donkey Kong's health to a new value.
     *
     * @param health The new health value.
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Reduces Donkey Kong's health by 1.
     * If health reaches 0 or below, marks Donkey Kong as destroyed.
     */
    public void reduceHealth() {
        health--;
        if (health <= 0) {
            destroy();
        }
    }

    /**
     * Draws Donkey Kong at its current position.
     */
    @Override
    public void draw() {
        DONKEY_KONG.draw(x, y);
    }

    /**
     * Returns the image representing Donkey Kong.
     *
     * @return The Donkey Kong image.
     */
    public Image getImage() {
        return DONKEY_KONG;
    }

}
