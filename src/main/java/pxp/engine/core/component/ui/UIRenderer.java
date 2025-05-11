package pxp.engine.core.component.ui;

import pxp.engine.core.RectTransform;
import pxp.engine.core.component.PivotedRenderer;

/**
 * The UIRenderer is a base class for all renderable UI elements which provides some basic functionality for those.
 */
public abstract class UIRenderer extends PivotedRenderer
{
    /**
     * Forces a {@link RectTransform} in case one isn't present
     */
    @Override
    public void awake() {
        // force rect transform
        if (transform() instanceof RectTransform) return;

        gameObject.transform = transform().toRectTransform();
    }

//    @Override
//    public void setSortingLayer(String layer) {
//        throw new IllegalStateException("Cannot set sorting layer of a UI element!");
//    }

    /**
     * Internal method to bypass sorting layer restriction for UI elements and set the sorting layer (used by Canvas)
     * @param layer the layer to set
     */
    protected void _setSortingLayer(String layer) {
        super.setSortingLayer(layer);
    }
}
