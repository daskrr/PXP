package pxp.engine.data;

/**
 * The filter to use when rescaling images<br/>
 * <a href="https://en.wikipedia.org/wiki/Texture_filtering">Texture Filtering - Wikipedia</a>
 * @see processing.opengl.Texture
 */
public enum TextureFilter
{
    TEX2D,
    TEXRECT,
    /**
     * For pixel art, (aka. Nearest Neighbour / hard edges & no pixel interpolation)
     */
    POINT,
    /**
     * Used for normal images
     */
    LINEAR,
    /**
     * Used for normal images
     */
    BILINEAR,
    /**
     * Used for normal images
     */
    TRILINEAR,
    MAX_UPDATES,
    MIN_MEMORY;
}
