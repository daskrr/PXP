package net.daskrr.cmgt.pxp.core;

/**
 * The Vector2 contains two float values that can represent position, sizes or anything
 */
public class Vector2
{
    /**
     * The x component of the vector
     */
    public float x = 0;
    /**
     * The y component of the vector
     */
    public float y = 0;

    /**
     * Creates a new Vector2 with x = 0, y = 0
     */
    public Vector2() { }

    /**
     * Creates a new Vector2 given both x and y components
     * @param x the x component of the vector
     * @param y the y component of the vector
     */
    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a new Vector2 from another Vector2, essentially cloning the provided vector
     * @param vec the vector to copy the values of
     */
    public Vector2(Vector2 vec) {
        this.x = vec.x;
        this.y = vec.y;
    }

    @Override
    public String toString() {
        return "Vector2[" + x + ", " + y + "]";
    }
}
