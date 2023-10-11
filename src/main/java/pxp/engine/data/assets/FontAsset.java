package pxp.engine.data.assets;

import pxp.util.ArrayUtil;
import processing.core.PApplet;
import processing.core.PFont;

public class FontAsset extends Asset
{
    public static final int NORMAL = 0;
    public static final int BOLD = 1;
    public static final int ITALIC = 2;
    public static final int BOLD_ITALIC = 3;

    /**
     * The array of either the paths to the 4 styles of the font or the names of those
     */
    private String[] fontIds;

    private int size = 32;
    private boolean smooth = true;

    private PFont[] fonts;

    /**
     * Creates a font from names or paths of each style of the font (the fonts need to exist in the data directory if
     * they are paths or locally on the machine if they are names)<br/>
     * <i>Leave the paths null of any style if it is not needed or doesn't exist</i>
     * @param normal the font name or path of the normal styled font
     * @param bold the font name or path of the bold styled font
     * @param italic the font name or path of the italic styled font
     * @param boldItalic the font name or path of the bold & italic styled font
     */
    public FontAsset(String normal, String bold, String italic, String boldItalic) {
        this(normal, bold, italic, boldItalic, 32, true);
    }

    /**
     * Creates a font from names or paths of each style of the font (the fonts need to exist in the data directory if
     * they are paths or locally on the machine if they are names)<br/>
     * <i>Leave the paths null of any style if it is not needed or doesn't exist</i>
     * @param normal the font name or path of the normal styled font
     * @param bold the font name or path of the bold styled font
     * @param italic the font name or path of the italic styled font
     * @param boldItalic the font name or path of the bold & italic styled font
     */
    public FontAsset(String normal, String bold, String italic, String boldItalic, int size, boolean smooth) {
        super(normal);

        fontIds = new String[4];
        fontIds[0] = normal;
        fontIds[1] = bold;
        fontIds[2] = italic;
        fontIds[3] = boldItalic;

        fonts = new PFont[4];

        this.size = size;
        this.smooth = smooth;
    }

    /**
     * Adds one or more styles to this font<br/>
     * <b>IMPORTANT: this will not do anything if it is called after the game has loaded!</b>
     * @param styles the styles to add
     */
    public void add(String... styles) {
        this.fontIds = ArrayUtil.concat(fontIds, styles);
        fonts = new PFont[fontIds.length];
    }

    @Override
    protected void load(PApplet processing) {
        for (int i = 0; i < fontIds.length; i++)
            if (fontIds[i] != null)
                fonts[i] = processing.createFont(fontIds[i], this.size, this.smooth);
    }

    /**
     * Retrieves the font style
     * @param style the font style
     * @return the font or null
     */
    public PFont getPFont(FontStyle style) {
        return this.fonts[style.index];
    }

    /**
     * Retrieves the font style at index style
     * @param style the index of the style of the font
     * @return the font or null
     * @throws IndexOutOfBoundsException if the specified style index exceeds the bounds of the fonts array
     */
    public PFont getPFont(int style) {
        if (style >= this.fonts.length)
            throw new IndexOutOfBoundsException("The style index specified is out of bounds!");

        return this.fonts[style];
    }
}
