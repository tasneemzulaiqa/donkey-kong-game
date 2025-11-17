import bagel.*;

/**
 * Represents a bullet  fired by Mario.
 * Bullets travel horizontally and deactivate after a certain distance
 * or if they go out of bounds.
 */
public class Bullet extends Projectile {
    private static final Image BULLET_RIGHT = new Image("res/bullet_right.png");
    private static final Image BULLET_LEFT = new Image("res/bullet_left.png");
    private static final double SPEED = 3.8;

    /**
     * Constructs a new Bullet at the given position and direction.
     *
     * @param x           The initial x-coordinate of the bullet.
     * @param y           The initial y-coordinate of the bullet.
     * @param facingLeft  True if the bullet should move left; false for right.
     */
    public Bullet(double x, double y, boolean facingLeft){
        super(x, y, facingLeft, SPEED);
    }

    /**
     * Draws the bullet on the screen using the appropriate image.
     */
    @Override
    public void draw() {
        getImage().draw(x,y);
    }

    /**
     * Returns the image of the bullet depending on its facing direction.
     *
     * @return The bullet image.
     */
    @Override
    public Image getImage() {
        return (isFacingLeft() ? BULLET_LEFT : BULLET_RIGHT);
    }
}
