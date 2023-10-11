package pxp.engine.data;

import pxp.util.Mathf;
import processing.core.PVector;

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

    /**
     * Returns this vector with a magnitude of 1.<br/>
     * When normalized, a vector keeps the same direction but its length is 1.0.<br/>
     * Note that the current vector is unchanged and a new normalized vector is returned.<br/>
     * If you want to normalize the current vector, use the #normalize() function.
     * @return a new normalized vector from this one
     * @see Vector2#normalize()
     */
    public Vector2 getNormalized() {
        return clone().normalize();
    }

    /**
     * Returns the length of this vector.<br/>
     * The length of the vector is square root of (x*x+y*y).<br/>
     * If you only need to compare magnitudes of some vectors, you can compare squared magnitudes of them using #getSqrtMagnitude()
     * (computing squared magnitudes is faster).
     * @return the magnitude
     */
    public float getMagnitude() {
        return Mathf.sqrt(getSqrtMagnitude());
    }

    /**
     * Returns the squared length of this vector.<br/>
     * Calculating the squared magnitude instead of the magnitude is much faster.<br/>
     * Often if you are comparing magnitudes of two vectors you can just compare their squared magnitudes.
     * @return the squared magnitude
     */
    public float getSqrtMagnitude() {
        return x*x + y*y;
    }

    /**
     * Sets this vector's x and y to the specified values
     * @param x the x component to set
     * @param y the y component to set
     */
    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }
    /**
     * Sets this vector's x and y to the ones of another vector
     */
    public void set(Vector2 other) {
        this.x = other.x;
        this.y = other.y;
    }

    /**
     * Calculates the dot product of this vector and another
     * @param other the other vector
     * @return the dot product
     */
    public float dot(Vector2 other) {
        return this.x * other.x + this.y * other.y;
    }

    /**
     * Adds another vector to this one
     * @param other vector to add
     */
    public void add(Vector2 other) {
        this.x += other.x;
        this.y += other.y;
    }
    /**
     * Adds a number to both x and y
     * @param n the number to add
     */
    public void add(float n) {
        this.x += n;
        this.y += n;
    }

    /**
     * Subtracts another vector from this one
     * @param other vector to subtract
     */
    public void subtract(Vector2 other) {
        this.x -= other.x;
        this.y -= other.y;
    }
    /**
     * Subtracts a number from both x and y
     * @param n the number to subtract
     */
    public void subtract(float n) {
        this.x -= n;
        this.y -= n;
    }
    /**
     * Multiplies this vector by a number
     * @param n the number to multiply with
     */
    public void multiply(float n) {
        this.x *= n;
        this.y *= n;
    }
    /**
     * Multiplies this vector by another vector (x1*x2, y1*y2)
     * @param vec the vector to multiply with
     */
    public void multiply(Vector2 vec) {
        this.x *= vec.x;
        this.y *= vec.y;
    }
    /**
     * Divides this vector by a number (non-zero)
     * @param n the number to divide by
     */
    public void divide(float n) {
        if (n == 0f) return;

        this.x /= n;
        this.y /= n;
    }

    /**
     * Makes this vector have a magnitude of 1.<br/>
     * When normalized, a vector keeps the same direction but its length is 1.0.<br/>
     * Note that this function will change the current vector. If you want to keep the current vector unchanged, use #getNormalized() method.
     * @return the current vector after normalization
     */
    public Vector2 normalize() {
        float magnitude = getMagnitude();
        if (magnitude > Float.MIN_VALUE) {
            x /= magnitude;
            y /= magnitude;
        }
        else {
            x = 0f;
            y = 0f;
        }

        return this;
    }

    /**
     * Clamps the x and y of this vector between 0 and 1
     * @return the current vector after clamping
     */
    public Vector2 clamp01() {
        x = Mathf.max(0, Mathf.min(1, x));
        y = Mathf.max(0, Mathf.min(1, y));
        return this;
    }

    /**
     * Calculates the distance between two Vector2s
     * @param other the vector to calculate the distance to
     * @return the positive distance between the two vectors
     */
    public float distance(Vector2 other) {
        return Mathf.sqrt(Mathf.pow(other.x - this.x, 2) + Mathf.pow(other.y - this.y, 2));
    }

    /**
     * Adds two vectors together
     */
    public static Vector2 add(Vector2 vec1, Vector2 vec2) {
        vec1 = vec1.clone();
        vec1.x += vec2.x;
        vec1.y += vec2.y;

        return vec1;
    }
    /**
     * Adds a number to both x and y
     */
    public static Vector2 add(Vector2 vec, float n) {
        vec = vec.clone();
        vec.x += n;
        vec.y += n;

        return vec;
    }

    /**
     * Subtracts one vector from the other
     */
    public static Vector2 subtract(Vector2 vec, Vector2 other) {
        vec = vec.clone();
        vec.x -= other.x;
        vec.y -= other.y;

        return vec;
    }
    /**
     * Subtracts a number from both x and y
     */
    public static Vector2 subtract(Vector2 vec, float n) {
        vec = vec.clone();
        vec.x -= n;
        vec.y -= n;

        return vec;
    }
    /**
     * Multiplies a vector by a number
     */
    public static Vector2 multiply(Vector2 vec, float n) {
        vec = vec.clone();
        vec.x *= n;
        vec.y *= n;

        return vec;
    }
    /**
     * Multiplies a vector by another vector (x1*x2, y1*y2)
     */
    public static Vector2 multiply(Vector2 vec1, Vector2 vec2) {
        vec1 = vec1.clone();
        vec1.x *= vec2.x;
        vec1.y *= vec2.y;

        return vec1;
    }
    /**
     * Divides a vector by a number (non-zero)
     */
    public static Vector2 divide(Vector2 vec, float n) {
        if (n == 0f) throw new RuntimeException("Cannot divide by 0!");

        vec = vec.clone();

        vec.x /= n;
        vec.y /= n;

        return vec;
    }

    public static Vector2 vector(Vector2 point1, Vector2 point2) {
        Vector2 vec = point2.clone();
        vec.subtract(point1);
        return vec;
    }

    /**
     * Linearly interpolate between 2 vectors by time. Time is clamped between 0 and 1.
     * @param initial starting vector
     * @param target target vector
     * @param time the time in seconds
     * @return the current interpolated vector based on time
     */
    public static Vector2 lerp(Vector2 initial, Vector2 target, float time) {
        return new Vector2(Mathf.lerp(initial.x, target.x, time), Mathf.lerp(initial.y, target.y, time));
    }
    /**
     * Linearly interpolate between 2 vectors by time. Time is not clamped
     * @param initial starting vector
     * @param target target vector
     * @param time the time in seconds
     * @return the current interpolated vector based on time
     */
    public static Vector2 lerpUnclamped(Vector2 initial, Vector2 target, float time) {
        return new Vector2(Mathf.lerpUnclamped(initial.x, target.x, time), Mathf.lerpUnclamped(initial.y, target.y, time));
    }

    public PVector toPVector() {
        return new PVector(this.x, this.y, 0);
    }

    public static Vector2 fromPVector(PVector vec) {
        return new Vector2(vec.x, vec.y);
    }


    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Vector2 vec)) return false;

        return this.x == vec.y && this.y == vec.y;
    }

    @Override
    public String toString() {
        return "Vector2[" + x + ", " + y + "]";
    }

    @Override
    public Vector2 clone() {
        return new Vector2(this.x, this.y);
    }
}
