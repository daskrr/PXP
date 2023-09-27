package net.daskrr.cmgt.pxp.core.component;

/**
 * Base for renderer components
 * @see SpriteRenderer
 * @see Canvas
 */
public abstract class Renderer extends Component
{
    /**
     * The sorting layer of this renderer
     * @see net.daskrr.cmgt.pxp.data.GameSettings#sortingLayers
     */
    public String sortingLayer = "Default";
    /**
     * The order in the sorting layer, in case there are multiple objects in the same sorting layer.
     */
    public int orderInLayer = 0;

    /**
     * The abstract rendering method
     */
    public abstract void render();
}
