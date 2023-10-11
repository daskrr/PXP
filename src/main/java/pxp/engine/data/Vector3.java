package pxp.engine.data;

/**
 * The Vector3 contains three float values that can represent position, sizes or anything
 */
public class Vector3
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
     * The z component of the vector
     */
    public float z = 0;

    /**
     * Creates a new Vector3 with x = 0, y = 0, z = 0
     */
    public Vector3() { }

    /**
     * Creates a new Vector3 given all x, y and z components
     * @param x the x component of the vector
     * @param y the y component of the vector
     * @param z the z component of the vector
     */
    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public Vector3 clone() {
        return new Vector3(this.x, this.y, this.z);
    }

    @Override
    public String toString() {
        return "Vector3[" + x + ", " + y + ", " + z + "]";
    }
}
