package pxp.engine.core.component.ui;

import pxp.engine.core.RectTransform;
import pxp.engine.core.component.Component;

/**
 * The UIComponent is a base class for UI elements which provides some basic functionality for those.
 */
public class UIComponent extends Component
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
}
