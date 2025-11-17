/**
 * Interface for objects that can shoot projectiles.
 *
 * @param <T> The type of projectile that can be shot.
 */
public interface Shootable<T> {
    /**
     * Shoots a projectile in the specified direction.
     *
     * @param directionLeft true if shooting left, false if shooting right.
     * @return The projectile that was shot.
     */
    T shoot(boolean directionLeft);
}
