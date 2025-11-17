import bagel.*;

/**
 * An abstract class representing a Monkey enemy that can move along a predefined pixel-based route.
 */
public abstract class Monkey extends Enemy implements Moveable {
    private final int[] route;
    private int currentMove;
    private double distanceMoved;
    private boolean facingLeft;
    private final static double SPEED = 0.5;
    private final Platform[] platforms;

    /**
     * Constructs a Monkey with a specified starting position, direction, movement route, and platform references.
     *
     * @param x          The initial x-coordinate.
     * @param y          The initial y-coordinate.
     * @param facingLeft Whether the monkey starts facing left.
     * @param route      An array of movement distances per segment.
     * @param platforms  The platforms available in the level for walking and edge detection.
     */
    public Monkey(double x, double y, boolean facingLeft, int[] route, Platform[] platforms) {
        super(x, y);
        this.facingLeft = facingLeft;
        this.route = route;
        this.distanceMoved = 0;
        this.currentMove = 0;
        this.platforms = platforms;
    }

    /**
     * Moves the monkey along its predefined route. It turns around when reaching the end of a segment,
     * the edge of the screen, or the edge of a platform.
     */
    public void move() {
        double horizontalStep = facingLeft ? -SPEED : SPEED;
        double nextX = x + horizontalStep;

        // Turn around if grounded and either at the edge of screen or platform
        if (isGrounded() && (nextX < 0 || nextX > Window.getWidth() || isEdgeOfPlatform(nextX))) {
            facingLeft = !facingLeft;
            distanceMoved = 0;
            return;
        }

        x += horizontalStep;
        distanceMoved += Math.abs(horizontalStep);

        // If reached current step distance
        if (distanceMoved >= route[currentMove]) {
            distanceMoved = 0;
            currentMove++;

            // Loop back to start of route if at the end
            if (currentMove >= route.length) {
                currentMove = 0;
            }

            facingLeft = !facingLeft;
        }
    }

    /**
     * Checks whether the monkey is currently grounded on a platform.
     *
     * @return true if grounded on a platform; false otherwise.
     */
    private boolean isGrounded() {
        return getStandingPlatform() != null;
    }

    /**
     * Returns the platform the monkey is currently standing on.
     *
     * @return The Platform the monkey is standing on, or null if not grounded.
     */
    private Platform getStandingPlatform() {
        double footY = this.y + this.getImage().getHeight() / 2.0;

        for (Platform plat : platforms) {
            double platTop = plat.getY() - plat.getImage().getHeight() / 2.0;

            // Check vertical proximity between monkey's feet and platform top
            boolean closeVertically = Math.abs(footY - platTop) <= 5;

            // Check horizontal overlap
            double platLeft = plat.getX() - plat.getImage().getWidth() / 2.0;
            double platRight = plat.getX() + plat.getImage().getWidth() / 2.0;
            boolean withinHorizontally = x >= platLeft && x <= platRight;

            // If both vertical and horizontal conditions are satisfied, return the platform
            if (closeVertically && withinHorizontally) {
                return plat;
            }
        }
        return null;
    }

    /**
     * Determines if the monkey would reach the edge of its current platform on the next move.
     *
     * @param nextX The x-coordinate the monkey would move to.
     * @return true if the monkey would step off the platform; false otherwise.
     */
    private boolean isEdgeOfPlatform(double nextX) {
        Platform standingPlat = getStandingPlatform();
        if (standingPlat == null) {
            return true;
        }
        double platLeft = standingPlat.getX() - standingPlat.getImage().getWidth() / 2.0;
        double platRight = standingPlat.getX() + standingPlat.getImage().getWidth() / 2.0;

        return nextX < platLeft || nextX > platRight;
    }

    /**
     * Returns whether the monkey is currently facing left.
     *
     * @return true if facing left; false if facing right.
     */
    public boolean isFacingLeft() {
        return facingLeft;
    }
}