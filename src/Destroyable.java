/**
 * An interface for game objects that can be destroyed.
 */
public interface Destroyable {
    /**
     * Destroys the object, triggering any logic related to its removal or deactivation.
     */
    void destroy();

    /**
     * Checks whether the object has been destroyed.
     *
     * @return true if the object is destroyed; false otherwise.
     */
    boolean isDestroyed();
}
