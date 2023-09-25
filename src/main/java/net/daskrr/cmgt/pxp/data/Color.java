package net.daskrr.cmgt.pxp.data;

public class Color
{
    private int argb = 0x000000;

    public Color() {}

    public Color (int argb) {
        this.argb = argb;
    }

    public Color (int r, int g, int b) {
        this(toHex(r,g,b, -1));
    }

    public Color (int r, int g, int b, int a) {
        this(toHex(r,g,b,a));
    }

    public int getHex() {
        return argb;
    }
    public int getA() {
        return argb >> 24 & 0xFF;
    }
    public int getR() {
        return argb >> 16 & 0xFF;
    }
    public int getG() {
        return argb >> 8 & 0xFF;
    }
    public int getB() {
        return argb & 0xFF;
    }

    private static int toHex(int r, int g, int b, int a) {
        if (a < 0) a = 255;

        r = (r & 0xFF);
        g = (g & 0xFF);
        b = (b & 0xFF);
        a = (a & 0xFF);

        return (a << 24) + (r << 16) + (g << 8) + (b);
    }
}
