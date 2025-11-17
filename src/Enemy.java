/**
 * Represents an abstract enemy in the game world.
 * Enemies are game objects that can be affected by gravity and can be destroyed.
 * This class provides basic gravity handling and destruction state.
 */
public abstract class Enemy extends GameObject implements AffectedByGravity, Destroyable{
    private boolean isDestroyed;
    private final static double ENEMY_GRAVITY = 0.4;
    private final static double ENEMY_TERMINAL_VELO = 5;
    private final Motion motion = new Motion(ENEMY_GRAVITY, ENEMY_TERMINAL_VELO);

    /**
     * Constructs a new Enemy at the given position.
     *
     * @param x The x-coordinate of the enemy.
     * @param y The y-coordinate of the enemy.
     */
    public Enemy(double x, double y) {
        super(x, y);
    }

    /**
     * Applies gravity to this enemy using the motion object and platform data.
     *
     * @param platforms Array of platforms to determine collision and grounded state.
     */
    public void applyGravity(Platform[] platforms) {
        motion.applyGravity(this, platforms);
    }

    /**
     * Marks this enemy as destroyed.
     */
    public void destroy() {
        isDestroyed = true;

    }

    /**
     * Returns whether this enemy has been destroyed.
     *
     * @return true if the enemy is destroyed, false otherwise.
     */
    public boolean isDestroyed() {
        return isDestroyed;
    }
}
