package net.daskrr.cmgt.pxp.core;

public class Vector2
{
    public float x = 0;
    public float y = 0;

    public Vector2() { }
    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Vector2[" + x + ", " + y + "]";
    }
}
