import bagel.Window;

/**
 * An abstract class representing a horizontal projectile in the game.
 * A projectile travels in a specified horizontal direction
 * with a given speed, and deactivates when it has either traveled a maximum distance
 * or moved out of the screen bounds
 */
public abstract class Projectile extends GameObject implements Moveable {
    private final boolean facingLeft;
    private final double speed;
    private final static double MAX_DISTANCE = 300;
    private double distanceTraveled = 0;
    private boolean active = true;

    /**
     * Constructs a Projectile object with specified position, direction, and speed.
     *
     * @param x           The initial x-coordinate of the projectile.
     * @param y           The initial y-coordinate of the projectile.
     * @param facingLeft  True if the projectile should move left; false for right.
     * @param speed       The horizontal speed of the projectile.
     */
    public Projectile(double x, double y, boolean facingLeft, double speed) {
        super(x, y);
        this.facingLeft = facingLeft;
        this.speed = speed;
    }

    /**
     * Moves projectile horizontally based on its direction and speed.
     * Deactivates the projectile if it has traveled too far or left the screen.
     */
    public void move() {
        if (!active) return;

        double step = facingLeft ? -speed : speed;
        double nextX = x + step;

        x += step;
        distanceTraveled += Math.abs(step);

        if (distanceTraveled >= MAX_DISTANCE || nextX < 0 || nextX > Window.getWidth()) {
            active = false;
        }
    }

    /**
     * Checks if projectile is still active.
     *
     * @return true if the projectile is active; false otherwise.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the projectile's active state.
     *
     * @param active true to mark the bullet as active, false to deactivate it.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Returns whether the object is currently facing left.
     *
     * @return true if facing left; false if facing right.
     */
    public boolean isFacingLeft() {
        return facingLeft;
    }
}

