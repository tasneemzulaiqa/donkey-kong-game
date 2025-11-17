import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * A helper class to handle vertical motion under gravity for game objects.
 * It simulates gravity, applies terminal velocity, and performs collision detection
 * with platforms to determine when an object lands.
 */
public class Motion {
    private double vy = 0;
    private final double terminalVelocity;
    private final double gravity;

    /**
     * Constructs a Motion object with specified gravity and terminal velocity.
     *
     * @param gravity          The gravitational acceleration to apply each frame.
     * @param terminalVelocity The maximum falling speed allowed.
     */
    public Motion(double gravity, double terminalVelocity) {
        this.terminalVelocity = terminalVelocity;
        this.gravity = gravity;
    }

    /**
     * Applies gravity to the given object and updates its vertical position.
     * If the object is falling and collides with the top of a platform, its vertical
     * motion is stopped and its position is snapped to the platform surface.
     *
     * @param obj       The game object affected by gravity.
     * @param platforms An array of platforms to check for landing collisions.
     * @return true if the object has landed on a platform, false otherwise.
     */
    public boolean applyGravity(GameObject obj, Platform[] platforms) {
        vy = Math.min(vy + gravity, terminalVelocity);
        double newY = obj.getY() + vy;

        // Check for collision with each platform
        for (Platform plat : platforms) {
            Rectangle objBox = obj.getImage().getBoundingBoxAt(new Point(obj.getX(), newY));
            Rectangle platBox = plat.getImage().getBoundingBoxAt(new Point(plat.getX(), plat.getY()));

            double objBottom = newY + obj.getImage().getHeight() / 2.0;
            double prevBottom = obj.getY() + obj.getImage().getHeight() / 2.0;
            double platformTop = plat.getY() - plat.getImage().getHeight() / 2.0;

            boolean isFalling = vy > 0;
            boolean crossedPlatformTop = prevBottom <= platformTop && objBottom >= platformTop;
            boolean horizontalOverlap = objBox.right() > platBox.left() && objBox.left() < platBox.right();

            // Snap to the platform top and stop falling when reaching a platform
            if (isFalling && crossedPlatformTop && horizontalOverlap) {

                obj.setY(platformTop - obj.getImage().getHeight() / 2.0);
                vy = 0;
                return true;
            }
        }
        obj.setY(newY);
        return false;
    }

    /**
     * Returns the current vertical velocity.
     *
     * @return The vertical velocity.
     */
    public double getVelocityY() {
        return vy;
    }

    /**
     * Sets the vertical velocity, replacing the current value.
     *
     * @param vy The new vertical velocity to apply.
     */
    public void setVelocityY(double vy) {
        this.vy = vy;
    }
}
