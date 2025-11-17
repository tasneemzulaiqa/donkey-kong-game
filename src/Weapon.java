/**
 * Abstract class representing a weapon in the game.
 * Weapons can be collected by the player.
 */
public abstract class Weapon extends GameObject{
    private boolean isCollected;

    /**
     * Constructs a Weapon at the specified coordinates.
     *
     * @param x The x-coordinate of the weapon.
     * @param y The y-coordinate of the weapon.
     */
    public Weapon(double x, double y) {
        super(x, y);
    }

    /**
     * Marks the weapon as collected.
     */
    public void collect(){
        isCollected = true;
    }

    /**
     * Checks whether the weapon has been collected.
     *
     * @return true if the weapon is collected, false otherwise.
     */
    public boolean isCollected(){
        return isCollected;
    }
}
