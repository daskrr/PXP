package pxp.engine.data;

/**
 * Color object to maintain a single standard of colors
 */
public class Color
{
    public static Color white() { return new Color(255,255,255,255); }
    public static Color black() { return new Color(0,0,0,255); }
    public static Color transparent() { return new Color(0,0,0,0); }

    /**
     * The hex value of the color
     */
    private int argb = 0x000000;

    /**
     * Creates a new Color (black [#000000])
     */
    public Color() {}

    /**
     * Creates a new Color given an ARGB hex
     * @param argb the ARGB (Alpha, Red, Green, Blue) hex
     */
    public Color (int argb) {
        this.argb = argb;
    }

    /**
     * Creates a new Color given RGB integers
     * @param r the red component of the color
     * @param g the green component of the color
     * @param b the blue component of the color
     */
    public Color (int r, int g, int b) {
        this(toHex(r,g,b, -1));
    }

    /**
     * Creates a new Color given RGBA integers
     * @param r the red component of the color
     * @param g the green component of the color
     * @param b the blue component of the color
     * @param a the alpha component of the color
     */
    public Color (int r, int g, int b, int a) {
        this(toHex(r,g,b,a));
    }

    /**
     * Gets the hex as an integer
     * @return the hex of the colour
     */
    public int getHex() {
        return argb;
    }

    /**
     * Returns the alpha component of the colour
     * @return the alpha value
     */
    public int getA() {
        return argb >> 24 & 0xFF;
    }
    /**
     * Returns the red component of the colour
     * @return the red value
     */
    public int getR() {
        return argb >> 16 & 0xFF;
    }
    /**
     * Returns the green component of the colour
     * @return the green value
     */
    public int getG() {
        return argb >> 8 & 0xFF;
    }
    /**
     * Returns the blue component of the colour
     * @return the blue value
     */
    public int getB() {
        return argb & 0xFF;
    }

    /**
     * Sets the alpha component of the colour
     */
    public void setA(int a) {
        this.argb = toHex(getR(), getG(), getB(), a);
    }
    /**
     * Sets the red component of the colour
     */
    public void setR(int r) {
        this.argb = toHex(r, getG(), getB(), getA());
    }
    /**
     * Sets the green component of the colour
     */
    public void setG(int g) {
        this.argb = toHex(getR(), g, getB(), getA());
    }
    /**
     * Sets the blue component of the colour
     */
    public void setB(int b) {
        this.argb = toHex(getR(), getG(), b, getA());
    }

    /**
     * Converts a set of integers representing RGBA into a hex integer
     * @param r the red component of the color
     * @param g the green component of the color
     * @param b the blue component of the color
     * @param a the alpha component of the color
     * @return the hex integer from RGBA values
     */
    private static int toHex(int r, int g, int b, int a) {
        if (a < 0) a = 255;

        r = (r & 0xFF);
        g = (g & 0xFF);
        b = (b & 0xFF);
        a = (a & 0xFF);

        return (a << 24) + (r << 16) + (g << 8) + (b);
    }
}
