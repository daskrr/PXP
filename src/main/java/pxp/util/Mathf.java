package pxp.util;

/**
 * Contains several functions for doing floating point Math
 */
public class Mathf
{
    /**
     * Constant PI
     */
    public static final float PI = (float) Math.PI;

    /**
     * Returns the absolute value of specified number
     */
    public static int abs (int value) {
        return (value<0)?-value:value;
    }

    /**
     * Returns the absolute value of specified number
     */
    public static float abs(float value) {
        return (value<0)?-value:value;
    }

    /**
     * Returns the acosine of the specified number
     */
    public static float acos(float f) {
        return (float)Math.acos(f);
    }

    /**
     * Returns the arctangent of the specified number
     */
    public static float atan(float f) {
        return (float)Math.atan (f);
    }

    /**
     * Returns the angle whose tangent is the quotent of the specified values
     */
    public static float atan2(float y, float x) {
        return (float)Math.atan2 (y, x);
    }

    /**
     * Returns the smallest integer as a float bigger greater than or equal to the specified number
     */
    public static float ceil(float a) {
        return (float)Math.ceil (a);
    }

    /**
     * Returns the smallest integer bigger greater than or equal to the specified number
     */
    public static int ceilToInt(float a) {
        return (int)Math.ceil (a);
    }

    /**
     * Returns the cosine of the specified number
     */
    public static float cos(float f) {
        return (float)Math.cos (f);
    }

    /**
     * Returns the hyperbolic cosine of the specified number
     */
    public static float cosh(float value) {
        return (float)Math.cosh (value);
    }

    /**
     * Returns e raised to the given number
     */
    public static float exp(float f) {
        return (float)Math.exp (f);
    }

    /**
     * Returns the largest integer as a float less than or equal to the specified value
     */
    public static float floor(float f) {
        return (float) Math.floor (f);
    }

    /**
     * Returns the largest integer less than or equal to the specified value
     */
    public static int floorToInt(float f) {
        return (int)Math.floor (f);
    }

    /**
     * Returns the natural logarithm of the specified number
     */
    public static float log(float f) {
        return (float)Math.log (f);
    }

    /**
     * Returns the log10 of the specified number
     */
    public static float log10(float f) {
        return (float)Math.log10(f);
    }

    /**
     * Returns the biggest of the two specified values
     */
    public static float max(float value1, float value2) {
        return Math.max(value2, value1);
    }

    /**
     * Returns the biggest of the two specified values
     */
    public static int max(int value1, int value2) {
        return Math.max(value2, value1);
    }

    /**
     * Returns the smallest of the two specified values
     */
    public static float min(float value1, float value2) {
        return Math.min(value2, value1);
    }

    /**
     * Returns the smallest of the two specified values
     */
    public static int min(int value1, int value2) {
        return Math.min(value2, value1);
    }

    /**
     * Returns x raised to the power of y
     */
    public static float pow(float x, float y) {
        return (float)Math.pow (x, y);
    }

    /**
     * Returns the nearest integer to the specified value
     */
    public static int round(float f) {
        return (int)Math.round (f);
    }

    /**
     * Returns a value indicating the sign of the specified number (-1=negative, 0=zero, 1=positive)
     */
    public static int sign(float f) {
        if (f < 0) return -1;
        if (f > 0) return 1;
        return 0;
    }

    /**
     * Returns a value indicating the sign of the specified number (-1=negative, 0=zero, 1=positive)
     */
    public static int sign(int f) {
        if (f < 0) return -1;
        if (f > 0) return 1;
        return 0;
    }

    /**
     * Returns the sine of the specified number
     */
    public static float sin(float f) {
        return (float)Math.sin (f);
    }

    /**
     * Returns the hyperbolic sine of the specified number
     */
    public static float sinh(float value) {
        return (float)Math.sinh (value);
    }

    /**
     * Returns the square root of the specified number
     */
    public static float sqrt(float f) {
        return (float)Math.sqrt (f);
    }

    /**
     * Returns the tangent of the specified number
     */
    public static float tan(float f) {
        return (float)Math.tan (f);
    }

    /**
     * Returns the hyperbolic tangent of the specified number
     */
    public static float tanh(float value) {
        return (float)Math.tanh (value);
    }

    /**
     * Clamps f in the range [min,max]
     * @return min if f < min, max if f > max, and f otherwise.
     */
    public static float clamp(float f, float min, float max) {
        return f < min ? min : (f > max ? max : f);
    }

    /**
     * Clamps f in the range 0,1.
     */
    public static float clamp01(float f) {
        return f < 0 ? 0 : (f > 1 ? 1 : f);
    }

    /**
     * Interpolates between a and b by t. t is clamped between 0 and 1.
     * @param a stating value
     * @param b target value
     * @param t time
     */
    public static float lerp(float a, float b, float t) {
        return a + (b - a) * clamp01(t);
    }

    /**
     * Interpolates between a and b by t. t is not clamped.
     * @param a stating value
     * @param b target value
     * @param t time
     */
    public static float lerpUnclamped(float a, float b, float t) {
        return a + (b - a) * t;
    }

    /**
     * Converts from degrees to radians
     */
    public static float degreesToRadians(float angle) {
        return (float) (angle * Math.PI / 180);
    }
}


