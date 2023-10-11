package pxp.engine.data;

import processing.core.PConstants;

public enum BlendMode
{
    /**
     * linear interpolation of colors: C = A*factor + B. This is the default.
     */
    BLEND(PConstants.BLEND),

    /**
     * additive blending with white clip: C = min(A*factor + B, 255)
     */
    ADD(PConstants.ADD),

    /**
     * subtractive blending with black clip: C = max(B - A*factor, 0)
     */
    SUBTRACT(PConstants.SUBTRACT),

    /**
     * only the darkest color succeeds: C = min(A*factor, B)
     */
    DARKEST(PConstants.DARKEST),

    /**
     * only the lightest color succeeds: C = max(A*factor, B)
     */
    LIGHTEST(PConstants.LIGHTEST),

    /**
     * subtract colors from underlying image.
     */
    DIFFERENCE(PConstants.DIFFERENCE),

    /**
     * similar to DIFFERENCE, but less extreme.
     */
    EXCLUSION(PConstants.EXCLUSION),

    /**
     * multiply the colors, result will always be darker.
     */
    MULTIPLY(PConstants.MULTIPLY),

    /**
     * opposite multiply, uses inverse values of the colors.
     */
    SCREEN(PConstants.SCREEN),

    /**
     * the pixels entirely replace the others and don't utilize alpha (transparency) values
     */
    REPLACE(PConstants.REPLACE);

    public final int mode;

    BlendMode(int mode) {
        this.mode = mode;
    }
}
