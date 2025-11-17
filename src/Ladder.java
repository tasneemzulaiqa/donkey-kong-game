import bagel.Image;
/**
 * Represents a ladder in the game, which is a static visual object but implements AffectedByGravity
 */
public class Ladder extends GameObject implements AffectedByGravity{
    private static final Image LADDER = new Image("res/ladder.png");
    private final Motion motion;
    private static final double LADDER_TERMINAL_VELOCITY = 5;
    private static final double LADDER_GRAVITY = 0.25;

    /**
     * Constructs a ladder at the specified position.
     *
     * @param x The x-coordinate of the ladder.
     * @param y The y-coordinate of the ladder.
     */
    public Ladder(double x, double y){
        super(x, y);
        this.motion = new Motion(LADDER_GRAVITY, LADDER_TERMINAL_VELOCITY);
    }

    /**
     * Draws the ladder at its current position.
     */
    @Override
    public void draw(){
        LADDER.draw(x,y);
    }

    /**
     * Applies gravity to the ladder using its motion object.
     *
     * @param platforms Array of platforms to use for collision detection.
     */
    @Override
    public void applyGravity(Platform[] platforms) {
        motion.applyGravity(this, platforms);
    }

    /**
     * Returns the image used to represent the ladder.
     *
     * @return The ladder's image.
     */
    public Image getImage() {
        return LADDER;
    }
}
