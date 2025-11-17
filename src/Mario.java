import bagel.*;
import bagel.util.*;
import java.util.ArrayList;

/**
 * Represents the player character Mario.
 * Mario can move left and right, jump, climb ladders, and shoot bullets
 * (when equipped with a Blaster). He can also equip different weapons such as
 * Hammer or Blaster which affect his appearance and capabilities.
 * This class handles Mario's movement physics including gravity and climbing,
 * user input controls, weapon management, bullet shooting, and rendering.
 */
public class Mario extends GameObject implements AffectedByGravity, Shootable<Bullet>, Moveable {

    // Mario images
    private static final Image MARIO_LEFT = new Image("res/mario_left.png");
    private static final Image MARIO_RIGHT = new Image("res/mario_right.png");
    private static final Image MARIO_HAMMER_LEFT = new Image("res/mario_hammer_left.png");
    private static final Image MARIO_HAMMER_RIGHT = new Image("res/mario_hammer_right.png");
    private static final Image MARIO_BLASTER_RIGHT = new Image("res/mario_blaster_right.png");
    private static final Image MARIO_BLASTER_LEFT = new Image("res/mario_blaster_left.png");

    // Motion and Physics
    private static final double MARIO_TERMINAL_VELOCITY = 10;
    private static final double MARIO_GRAVITY = 0.2;
    private final Motion motion = new Motion(MARIO_GRAVITY, MARIO_TERMINAL_VELOCITY);
    private static final double SPEED = 3.5;
    private static final double JUMP_VELOCITY = -5;
    private static final double CLIMB_SPEED = 2.0;
    private boolean isClimbing = false;
    private boolean onGround = false;
    private boolean hasScoredThisJump = false;
    private boolean facingLeft = false;

    private Weapon currentWeapon = null;
    private final ArrayList<Bullet> bullets = new ArrayList<>();
    private Input input;
    private int bulletCount = 0;

    /**
     * Constructs a Mario instance positioned at (x, y).
     *
     * @param x Initial horizontal position.
     * @param y Initial vertical position.
     */
    public Mario(double x, double y) {
        super(x, y);
    }

    /**
     * Updates Mario's state for the current frame.
     * Handles input-based movement, climbing, jumping, gravity application,
     * weapon ammo management, bullet shooting, and constrains Mario within screen bounds.
     *
     * @param input     Current input state.
     * @param platforms Array of platforms for collision and gravity.
     * @param ladders   Array of ladders for climbing logic.
     */
    public void update(Input input, Platform[] platforms, Ladder[] ladders) {
        this.input = input;
        move();
        climb(input, ladders);
        handleJump(input);
        applyGravity(platforms);

        // Handle mario icon if bullet runs out
        if (currentWeapon instanceof Blaster && bulletCount <= 0) {
            currentWeapon = null;
        }

        // Handle shooting
        if (input.wasPressed(Keys.S) && currentWeapon instanceof Blaster) {
            shoot(facingLeft);
            bulletCount--;
        }
        for (Bullet bullet : bullets) {
            bullet.move();
        }
        // limit mario movement to within screen
        x = Math.max(0, Math.min(x, Window.getWidth()));
    }

    /**
     * Applies gravity to Mario unless he is climbing.
     *
     * @param platforms Platforms used to detect ground collisions.
     */
    @Override
    public void applyGravity(Platform[] platforms) {
        if (!isClimbing) {
            onGround =  motion.applyGravity(this, platforms);
        }
    }

    /**
     * Moves Mario horizontally based on input.
     */
    @Override
    public void move() {
        if (input == null) return;
        if (input.isDown(Keys.LEFT)) {
            x -= SPEED;
            facingLeft = true;
            isClimbing = false;
        } else if (input.isDown(Keys.RIGHT)) {
            x += SPEED;
            facingLeft = false;
            isClimbing = false;
        }
    }

    /**
     * Allows Mario to climb up or down ladders if aligned.
     *
     * @param input   The current input state.
     * @param ladders Array of ladders in the level.
     */
    private void climb(Input input, Ladder[] ladders) {
        boolean climbUp = input.isDown(Keys.UP);
        boolean climbDown = input.isDown(Keys.DOWN);
        isClimbing = false;

        Rectangle marioBox = getImage().getBoundingBoxAt(new Point(x, y));

        for (Ladder ladder : ladders) {
            Rectangle ladderBox = ladder.getImage().getBoundingBoxAt(new Point(ladder.getX(), ladder.getY()));
            boolean isHorizontallyAligned = marioBox.centre().x > ladderBox.left() && marioBox.centre().x < ladderBox.right();
            boolean isVerticallyAligned = marioBox.bottom() >= ladderBox.top() && marioBox.top() <= ladderBox.bottom();

            if ((climbUp || climbDown) && isHorizontallyAligned && isVerticallyAligned) {
                isClimbing = true;
            }

            if (!onGround && marioBox.intersects(ladderBox)) {
                isClimbing = true;
            }
        }

        if (isClimbing) {
            motion.setVelocityY(0);
            if (climbUp) {
                y -= CLIMB_SPEED;
                alignLadderEdge(true, ladders);
            } else if (climbDown) {
                y += CLIMB_SPEED;
                alignLadderEdge(false, ladders);
            }
        }
    }

    /**
     * Aligns Mario's position to the edge of the ladder when reaching top or bottom,
     * and setting Mario on the ground.
     *
     * @param climbingUp True if climbing up, false if climbing down.
     * @param ladders   Array of ladders in the level.
     */
    private void alignLadderEdge(boolean climbingUp, Ladder[] ladders) {
        Rectangle marioBox = getImage().getBoundingBoxAt(new Point(x, y));

        for (Ladder ladder : ladders) {
            Rectangle ladderBox = ladder.getImage().getBoundingBoxAt(new Point(ladder.getX(), ladder.getY()));
            if (isAlignedWithLadder(ladder)) {
                if (climbingUp && marioBox.bottom() <= ladderBox.top()) {
                    y = ladderBox.top() - getImage().getHeight() / 2.0;
                    isClimbing = false;
                    onGround = true;
                    break;
                } else if (!climbingUp && marioBox.bottom() >= ladderBox.bottom()) {
                    y = ladderBox.bottom() - getImage().getHeight() / 2.0;
                    isClimbing = false;
                    onGround = true;
                    break;
                }
            }
        }
    }

    /**
     * Checks if Mario is horizontally aligned with the ladder.
     *
     * @param ladder Ladder to check alignment against.
     * @return True if Mario is horizontally overlapping the ladder.
     */
    private boolean isAlignedWithLadder(Ladder ladder) {
        Rectangle ladderBox = ladder.getImage().getBoundingBoxAt(new Point(ladder.getX(), ladder.getY()));
        Rectangle marioBox = getImage().getBoundingBoxAt(new Point(x, y));
        return marioBox.right() > ladderBox.left() && marioBox.left() < ladderBox.right();
    }

    /**
     * Handles jumping logic when Mario is on ground and not climbing.
     *
     * @param input Current input state.
     */
    private void handleJump(Input input) {
        if (input.wasPressed(Keys.SPACE) && onGround && !isClimbing) {
            motion.setVelocityY(JUMP_VELOCITY);
            onGround = false;
        }
    }

    /**
     * Draws Mario and all active bullets on the screen.
     */
    public void draw() {
        getImage().draw(x, y);
        for (Bullet bullet : bullets) {
            if (bullet.isActive()) {
                bullet.draw();
            }
        }
    }

    /**
     * Returns the current image to display for Mario, based on direction and weapon held.
     *
     * @return The Image representing Mario's current state.
     */
    @Override
    public Image getImage() {
        if (currentWeapon instanceof Blaster) {
            return facingLeft ? MARIO_BLASTER_LEFT : MARIO_BLASTER_RIGHT;
        } else if (currentWeapon instanceof Hammer) {
            return facingLeft ? MARIO_HAMMER_LEFT : MARIO_HAMMER_RIGHT;
        } else {
            return facingLeft ? MARIO_LEFT : MARIO_RIGHT;
        }
    }

    /**
     * Creates and adds a new bullet fired by Mario.
     *
     * @param facingLeft True if Mario is facing left; bullet direction follows.
     * @return The Bullet object created.
     */
    public Bullet shoot(boolean facingLeft) {
        Bullet bullet = new Bullet(this.x, this.y, facingLeft);
        bullets.add(bullet);
        return bullet;
    }

    /**
     * Sets the current weapon Mario is holding.
     *
     * @param weapon The weapon to equip Mario with
     */
    public void setCurrentWeapon(Weapon weapon) {
        this.currentWeapon = weapon;
    }

    /**
     * Gets the current weapon Mario is holding.
     *
     * @return The equipped Weapon
     */
    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    /**
     * Sets the number of bullets Mario currently has available to shoot.
     *
     * @param count The bullet count (ammo).
     */
    public void setBulletCount(int count) {
        this.bulletCount = count;
    }

    /**
     * Gets the number of bullets Mario currently has available to shoot.
     *
     * @return The current bullet count.
     */
    public int getBulletCount() {
        return bulletCount;
    }

    /**
     * Gets the list of bullets fired by Mario.
     *
     * @return An ArrayList of Bullet objects currently active or inactive.
     */
    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    /**
     * Checks whether Mario is currently on the ground.
     *
     * @return True if Mario is on ground, false otherwise.
     */
    public boolean isOnGround() {
        return onGround;
    }

    /**
     * Gets Mario's current vertical velocity.
     *
     * @return The vertical velocity
     */
    public double getVerticalVelocity() {
        return motion.getVelocityY();
    }

    /**
     * Checks whether Mario has scored during the current jump.
     *
     * @return True if Mario has scored during this jump, false otherwise.
     */
    public boolean hasScoredThisJump() {
        return hasScoredThisJump;
    }

    /**
     * Sets whether Mario has scored during the current jump.
     *
     * @param scored True if Mario has scored this jump, false otherwise.
     */
    public void setHasScoredThisJump(boolean scored) {
        this.hasScoredThisJump = scored;
    }
}

