package net.daskrr.cmgt.pxp.data;

/**
 * The basic mouse buttons (left, right and middle click)
 */
public enum MouseButton
{
    MB1(37),
    MB2(39),
    MB3(3);

    public final int code;

    MouseButton(int code) {
        this.code = code;
    }

    /**
     * Retrieves a mouse button based on its code
     * @param code the code
     * @return the mouse button or null
     */
    public static MouseButton fromCode(int code) {
        for (MouseButton mouseBtn : values())
            if (mouseBtn.code == code)
                return mouseBtn;

        return null;
    }
}
