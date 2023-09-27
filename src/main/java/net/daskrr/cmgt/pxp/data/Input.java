package net.daskrr.cmgt.pxp.data;

import net.daskrr.cmgt.pxp.core.GameProcess;

/**
 * The Input is used to get what the user/player is doing, this class provides static methods to get Keys & Buttons pressed
 * during a frame execution. To be used, preferably, inside update() methods.
 */
public class Input
{
    /**
     * [Internal] The currently (this frame) pressed keys
     */
    public static Key[] keys = new Key[Key.values().length];
    /**
     * [Internal] The currently (this frame) pressed mouse buttons
     */
    public static MouseButton[] mouseButtons = new MouseButton[MouseButton.values().length];
    /**
     * [Internal] The currently (this frame) clicked mouse buttons
     */
    public static MouseButton[] mouseButtonsClicked = new MouseButton[MouseButton.values().length];

    /**
     * Gets whether a key is being pressed this frame
     * @param key the key to check
     * @return whether the key is being pressed
     */
    public static boolean getKey(Key key) {
        return keys[key.ordinal()] != null;
    }

    /**
     * Gets whether a mouse button is being pressed this frame
     * @param mouseButton the mouse button to check
     * @return whether the mouse button is pressed
     */
    public static boolean getMouseButton(MouseButton mouseButton) {
        return mouseButtons[mouseButton.ordinal()] != null;
    }
    /**
     * Gets whether a mouse button is being clicked
     * @param mouseButton the mouse button to check
     * @return whether the mouse button is clicked
     */
    public static boolean getMouseButtonClick(MouseButton mouseButton) {
        return mouseButtonsClicked[mouseButton.ordinal()] != null;
    }

    /**
     * Gets the keys from the PApplet
     * @param process the PApplet GameProcess
     * @deprecated due to usage of callbacks which are better in the context of Processing
     */
    @Deprecated
    public static void fetchKeys(GameProcess process) {
        // keys
        Key key = Key.fromCode(process.keyCode);
        if (key == null)
            return;

        keys[key.ordinal()] = key;

        // mouse buttons
        MouseButton button = MouseButton.fromCode(process.mouseButton);
        if (button == null)
            return;

        Input.mouseButtons[button.ordinal()] = button;
    }

    /**
     * Resets the buttons clicked (after every frame execution)
     */
    public static void reset() {
        // not resetting the keys, only the mouse
//        keys = new Key[Key.values().length];
        mouseButtonsClicked = new MouseButton[MouseButton.values().length];
    }
}
