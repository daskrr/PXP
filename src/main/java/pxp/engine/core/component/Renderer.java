package pxp.engine.core.component;

import pxp.annotations.HideInInspector;
import pxp.annotations.LinkFieldInInspector;
import pxp.annotations.LinkState;
import pxp.annotations.LinkType;
import pxp.engine.core.component.ui.Canvas;
import pxp.engine.core.component.ui.Interactable;
import pxp.engine.data.BlendMode;
import pxp.engine.data.Color;
import pxp.engine.data.GameSettings;

/**
 * Base for renderer components
 * @see SpriteRenderer
 * @see DrawCanvas
 */
public abstract class Renderer extends Component
{
    /**
     * Blends the pixels in the display window according to a defined mode<br/>
     * <i>Defaulted to {@link BlendMode#BLEND}</i>
     */
    @HideInInspector(isInstance = Interactable.class)
    public BlendMode blendMode = BlendMode.BLEND;

    /**
     * The color tint to apply to the rendered object
     */
    @HideInInspector(isInstance = Interactable.class)
    public Color color = Color.white();

    /**
     * The sorting layer of this renderer
     * @see GameSettings#sortingLayers
     */
    @HideInInspector(isInstance = Canvas.class)
    protected String sortingLayer = "Default";
    /**
     * The order in the sorting layer, in case there are multiple objects in the same sorting layer.
     */
    protected int orderInLayer = 0;

    /**
     * [Internal] Specifies whether the super class renderer handles its own reset or not<br/>
     * <i>Defaulted to false, as in the renderer does not handle its own reset and it is handled automatically after the
     * render method is executed</i>
     */
    public boolean handlesReset = false;

    /**
     * The rendering method which sets some basic stuff up
     */
    public void render() {
        ctx().blendMode(this.blendMode.mode);
        // for sprites
        ctx().tint(this.color.getHex());
        // for anything else
        ctx().fill(this.color.getHex());
    }

    /**
     * Needs to be used after rendering to reset some settings
     */
    public void reset() {
        ctx().noTint();
        ctx().noFill();
        ctx().blendMode(BLEND);
    }

    @LinkFieldInInspector(name = "sortingLayer", type = LinkType.SETTER, state = LinkState.BOTH)
    public void setSortingLayer(String layer) {
        this.sortingLayer = layer;
        if (started && gameObject != null && gameObject.scene != null) {
            this.gameObject.scene.unregisterSortingLayer(gameObject);
            this.gameObject.scene.registerSortingLayer(gameObject);
        }
    }
    @LinkFieldInInspector(name = "orderInLayer", type = LinkType.SETTER, state = LinkState.BOTH)
    public void setOrderInLayer(int order) {
        this.orderInLayer = order;
        if (started && gameObject != null && gameObject.scene != null) {
            this.gameObject.scene.unregisterSortingLayer(gameObject);
            this.gameObject.scene.registerSortingLayer(gameObject);
        }
    }

    @LinkFieldInInspector(name = "sortingLayer", type = LinkType.GETTER, state = LinkState.BOTH)
    public String getSortingLayer() {
        return this.sortingLayer;
    }
    @LinkFieldInInspector(name = "orderInLayer", type = LinkType.GETTER, state = LinkState.BOTH)
    public int getOrderInLayer() {
        return this.orderInLayer;
    }
}
