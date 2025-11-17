import bagel.*;

/**
 * Abstract base class representing a game object with a position.
 * Provides position getters and setters, and abstract methods for drawing and getting the object's image.
 */
public abstract class GameObject {
    protected double x, y;

    /**
     * Constructs a GameObject at the specified (x, y) position.
     *
     * @param x the initial x-coordinate
     * @param y the initial y-coordinate
     */
    public GameObject(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the current x-coordinate of the object.
     *
     * @return the x-coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the current y-coordinate of the object.
     *
     * @return the y-coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the x-coordinate of the object.
     *
     * @param x the new x-coordinate
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Sets the y-coordinate of the object.
     *
     * @param y the new y-coordinate
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Draws the game object on the screen.
     * Implementation depends on the concrete subclass.
     */
    public abstract void draw();

    /**
     * Returns the image representation of the game object.
     * Implementation depends on the concrete subclass.
     *
     * @return the image of the object
     */
    public abstract Image getImage();
}
