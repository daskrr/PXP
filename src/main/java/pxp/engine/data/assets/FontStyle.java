package pxp.engine.data.assets;

public enum FontStyle
{
    NORMAL(0),
    BOLD(1),
    ITALIC(2),
    BOLD_ITALIC(3);

    public final int index;

    FontStyle(int index) {
        this.index = index;
    }
}
