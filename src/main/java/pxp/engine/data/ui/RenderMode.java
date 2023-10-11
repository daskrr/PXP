package pxp.engine.data.ui;

/**
 * RenderMode determines how a Canvas is going to be rendered.
 */
public enum RenderMode
{
    /**
     * Canvas is stuck to the camera and scales according to its size.
     */
    CAMERA,
    /**
     * Canvas is an object in the world and doesn't stick to anything. Its elements are using world coordinates.
     */
    WORLD
}
