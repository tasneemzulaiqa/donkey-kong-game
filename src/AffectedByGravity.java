/**
 * Interface for game objects that are affected by gravity.
 */
public interface AffectedByGravity {
    /**
     * Applies gravity to the object and updates its vertical position.
     *
     * @param platforms An array of platforms to use for collision detection.
     */
    void applyGravity(Platform[] platforms);
}
