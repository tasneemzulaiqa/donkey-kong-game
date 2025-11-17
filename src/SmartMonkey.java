import bagel.*;
import java.util.ArrayList;

/**
 * Represents a SmartMonkey enemy that can move along a predefined route and shoot bananas periodically.
 * Extends Monkey and implements Shootable with Banana as the type.
 */
public class SmartMonkey extends Monkey implements Shootable<Banana> {
    private static final Image SMART_MONKEY_RIGHT = new Image("res/intelli_monkey_right.png");
    private static final Image SMART_MONKEY_LEFT = new Image("res/intelli_monkey_left.png");
    private final ArrayList<Banana> bananas = new ArrayList<>();
    private int lastShotFrame = 0;
    private static final int SHOOT_DELAY = 300;

    /**
     * Constructs a SmartMonkey with given position, direction, movement route, and platform references.
     *
     * @param x         X-coordinate of the monkey.
     * @param y         Y-coordinate of the monkey.
     * @param facingLeft True if initially facing left, false if right.
     * @param route     Array of distances for movement in a repeating pattern.
     * @param platforms Array of platforms used for grounded and edge detection.
     */
    public SmartMonkey(double x, double y, boolean facingLeft, int[] route, Platform[] platforms){
        super(x, y, facingLeft, route, platforms);
    }

    /**
     * Updates the SmartMonkey’s state: moves it, potentially shoots, and updates all bananas.
     *
     * @param currentFrame The current game frame count used to control shooting delay.
     */
    public void update(int currentFrame) {
        move();
        if (currentFrame - lastShotFrame >= SHOOT_DELAY) {
            shoot(isFacingLeft());
            lastShotFrame = currentFrame;
        }
        for (Banana banana : bananas) {
            banana.move();
        }
    }

    /**
     * Draws the SmartMonkey and all bananas it has shot if not destroyed.
     */
    public void draw() {
        if (!isDestroyed()){
            getImage().draw(x, y);
            for (Banana banana : bananas) {
                banana.draw();
            }
        }
    }

    /**
     * Shoots a banana in the direction the monkey is currently facing.
     *
     * @param facingLeft Direction in which to shoot the banana.
     * @return Banana object.
     */
    public Banana shoot(boolean facingLeft) {
        Banana banana = new Banana(this.x, this.y, facingLeft);
        bananas.add(banana);
        return banana;
    }

    /**
     * Returns the list of bananas currently managed by SmartMonkey.
     *
     * @return An ArrayList of Banana objects.
     */
    public ArrayList<Banana> getBananas() {
        return bananas;
    }

    /**
     * Returns the image representing the SmartMonkey, based on its facing direction.
     *
     * @return The appropriate Image
     */
    public Image getImage() {
        return (isFacingLeft() ? SMART_MONKEY_LEFT : SMART_MONKEY_RIGHT);
    }
}
