package pxp.annotations;

/**
 * Used with {@link LinkFieldInInspector} to specify when the link (method) can be used by the Inspector.
 */
public enum LinkState
{
    /**
     * The link can only be used in the Inspector while the game <b>is not</b> running.
     * Use this for methods that don't require communication with other components or game objects, or that don't perform changes that would not be possible outside the game running.
     */
    INSPECTOR,
    /**
     * The link can only be used in the while the game <b>is</b> running.
     * Use this for methods that need the game to run in order to function correctly, for example they require communicating with another component or another game object.
     */
    RUNTIME,
    /**
     * The link can be used at any time.
     * Use this for methods that have standalone functionality (e.g.: mathematical calculations)
     */
    BOTH
}
