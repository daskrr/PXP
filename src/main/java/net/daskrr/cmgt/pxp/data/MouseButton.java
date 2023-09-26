package net.daskrr.cmgt.pxp.data;

public enum MouseButton
{
    MB1(37),
    MB2(39),
    MB3(3);

    public final int code;

    MouseButton(int code) {
        this.code = code;
    }

    public static MouseButton fromCode(int code) {
        for (MouseButton mouseBtn : values())
            if (mouseBtn.code == code)
                return mouseBtn;

        return null;
    }
}
