package pxp.engine.data;

import pxp.engine.core.GameProcess;

import java.util.HashMap;
import java.util.Map;

/**
 * The Input is used to get what the user/player is doing, this class provides static methods to get Keys & Buttons pressed
 * during a frame execution. To be used, preferably, inside update() methods.
 */
public class Input
{
    /**
     * All the Input instances
     */
    private static final Map<Integer, Input> inputInstances = new HashMap<>();

    /**
     * Get the main time instance
     */
    public static Input getDefault() {
        return inputInstances.get(GameProcess.getInstance().hashCode());
    }

    /**
     * Gets the instance of input assigned to the specified game process
     * @param process the game process
     * @return the Input instance or null
     */
    public static Input get(GameProcess process) {
        return inputInstances.get(process.hashCode());
    }

    /**
     * Creates the default instance
     */
    public static void createDefault() {
        if (inputInstances.containsKey(GameProcess.getInstance().hashCode()))
            throw new IllegalStateException("Default exists already!");

        new Input(GameProcess.getInstance());
    }

    /**
     * [Internal] The currently (this frame) pressed keys
     */
    public Key[] keys = new Key[Key.values().length];
    /**
     * [Internal] The currently (this frame) pressed mouse buttons
     */
    public MouseButton[] mouseButtons = new MouseButton[MouseButton.values().length];
    /**
     * [Internal] The currently (this frame) clicked mouse buttons
     */
    public MouseButton[] mouseButtonsClicked = new MouseButton[MouseButton.values().length];

    public Vector2 mousePos = new Vector2();

    /**
     * Whether the scroll wheel is being used this frame
     */
    public boolean isScrolling = false;

    /**
     * How much scrolling is being done this frame
     */
    public float scrollAmount = 0f;


    private Input(GameProcess process) {
        inputInstances.put(process.hashCode(), this);
    }

    /**
     * Gets whether a key is being pressed this frame
     * @param key the key to check
     * @return whether the key is being pressed
     */
    public boolean _getKey(Key key) {
        return keys[key.ordinal()] != null;
    }

    /**
     * Gets whether a key is being pressed this frame (and sets the key's pressed status to false)
     * @param key the key to check
     * @return whether the key is being pressed
     */
    public boolean _getKeyOnce(Key key) {
        boolean isPressed = keys[key.ordinal()] != null;
        if (isPressed)
            keys[key.ordinal()] = null;

        return isPressed;
    }

    /**
     * Gets whether a mouse button is being pressed this frame
     * @param mouseButton the mouse button to check
     * @return whether the mouse button is pressed
     */
    public boolean _getMouseButton(MouseButton mouseButton) {
        return mouseButtons[mouseButton.ordinal()] != null;
    }
    /**
     * Gets whether a mouse button is being clicked
     * @param mouseButton the mouse button to check
     * @return whether the mouse button is clicked
     */
    public boolean _getMouseButtonClick(MouseButton mouseButton) {
        return mouseButtonsClicked[mouseButton.ordinal()] != null;
    }

    /**
     * Retrieves the mouse position
     * @return the mouse position as a {@link Vector2}
     */
    public Vector2 _getMousePos() {
        return mousePos;
    }

    /**
     * Resets the buttons clicked (after every frame execution)
     */
    public void _reset() {
        // not resetting the keys, only the mouse
//        keys = new Key[Key.values().length];
        mouseButtonsClicked = new MouseButton[MouseButton.values().length];
        scrollAmount = 0;
        isScrolling = false;
    }


    // =============== STATIC METHODS ===============

    /**
     * Whether the scroll wheel is being used this frame
     */
    public static boolean isScrolling() {
        return getDefault().isScrolling;
    }
    /**
     * How much scrolling is being done this frame
     */
    public static float getScrollAmount() {
        return getDefault().scrollAmount;
    }

    /**
     * Gets whether a key is being pressed this frame<br/>
     * <i>(this can happen for multiple frames until Processing fires the keyReleased event)<br/>
     * For single key press, use {@link Input#getKeyOnce(Key)}</i>
     * @param key the key to check
     * @return whether the key is being pressed
     */
    public static boolean getKey(Key key) {
        return getDefault()._getKey(key);
    }

    /**
     * Gets whether a key is being pressed this frame (and sets the key's pressed status to false)
     * @param key the key to check
     * @return whether the key is being pressed
     */
    public static boolean getKeyOnce(Key key) {
        return getDefault()._getKeyOnce(key);
    }

    /**
     * Gets whether a mouse button is being pressed this frame
     * @param mouseButton the mouse button to check
     * @return whether the mouse button is pressed
     */
    public static boolean getMouseButton(MouseButton mouseButton) {
        return getDefault()._getMouseButton(mouseButton);
    }
    /**
     * Gets whether a mouse button is being clicked
     * @param mouseButton the mouse button to check
     * @return whether the mouse button is clicked
     */
    public static boolean getMouseButtonClick(MouseButton mouseButton) {
        return getDefault()._getMouseButtonClick(mouseButton);
    }

    /**
     * Retrieves the mouse position
     * @return the mouse position as a {@link Vector2}
     */
    public static Vector2 getMousePos() {
        return getDefault()._getMousePos();
    }

    /**
     * Resets the buttons clicked (after every frame execution)
     */
    public static void reset() {
        getDefault()._reset();
    }
}
