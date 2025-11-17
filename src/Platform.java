import bagel.Image;

/**
 * Represents a static platform in the game world.
 * Platforms can be stood upon by characters and objects.
 */
public class Platform extends GameObject{
    private static final Image PLATFORM = new Image("res/platform.png");

    /**
     * Creates a new Platform at the specified (x, y) position.
     *
     * @param x The horizontal position of the platform's center.
     * @param y The vertical position of the platform's center.
     */
    public Platform(double x, double y){
        super(x, y);
    }

    /**
     * Draws the platform at its current position.
     */
    @Override
    public void draw(){
        PLATFORM.draw(x,y);
    }

    /**
     * Gets the image used to represent the platform.
     *
     * @return The platform image.
     */
    public Image getImage() {
        return PLATFORM;
    }
}

