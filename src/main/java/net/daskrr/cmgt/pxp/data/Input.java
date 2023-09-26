package net.daskrr.cmgt.pxp.data;

import net.daskrr.cmgt.pxp.core.GameProcess;

public class Input
{
    public static Key[] keys = new Key[Key.values().length];
    public static MouseButton[] mouseButtons = new MouseButton[MouseButton.values().length];
    public static MouseButton[] mouseButtonsClicked = new MouseButton[MouseButton.values().length];

    public static boolean getKey(Key key) {
        return keys[key.ordinal()] != null;
    }

    public static boolean getMouseButton(MouseButton mouseButton) {
        return mouseButtons[mouseButton.ordinal()] != null;
    }
    public static boolean getMouseButtonClick(MouseButton mouseButton) {
        return mouseButtonsClicked[mouseButton.ordinal()] != null;
    }

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

//    @Deprecated
    public static void reset() {
        // not resetting the keys, only the mouse
//        keys = new Key[Key.values().length];
        mouseButtonsClicked = new MouseButton[MouseButton.values().length];
    }
}
