package pxp.engine.core.component.ui;

import pxp.annotations.LinkFieldInInspector;
import pxp.annotations.LinkState;
import pxp.annotations.LinkType;
import pxp.engine.core.GameObject;
import pxp.engine.core.RectTransform;
import pxp.engine.data.Vector2;
import pxp.engine.data.ui.RenderMode;

/**
 * The UI Canvas is an area where UI elements live. It helps to anchor and scale elements to whatever sizes are necessary.
 */
public class Canvas extends UIRenderer
{
    /**
     * The sorting layer of the canvas (when using {@link RenderMode#CAMERA})
     */
    public static final String CANVAS_LAYER = "_UICanvas";

    /**
     * Stores the sorting layer this Canvas had before the render mode is set dynamically.
     */
    private String initialSortingLayer = "Default";

    /**
     * The render mode of the Canvas
     * @see RenderMode
     */
    private RenderMode renderMode = RenderMode.CAMERA;

    /**
     * Creates a default Canvas component
     */
    public Canvas() { }

    /**
     * Creates a Canvas component given a RenderMode
     * @param renderMode the desired render mode
     */
    public Canvas(RenderMode renderMode) {
        this.renderMode = renderMode;
    }

    @Override
    public void start() {
        // set the sorting layer of all children of this game object to be on the Canvas layer
        if (renderMode == RenderMode.CAMERA)
            for (GameObject child : gameObject.getChildrenDeep())
                // only set the sorting layer for UI elements
                if (child.renderer != null && child.renderer instanceof UIRenderer uiRenderer)
                    uiRenderer._setSortingLayer(CANVAS_LAYER);
    }

    @Override
    public void render() {
        super.render();

        if (renderMode == RenderMode.CAMERA) {
            // force rect transform with preset width and height
            if (!(gameObject.transform instanceof RectTransform))
                gameObject.transform = new RectTransform(new Vector2(0,0));

            rectTransform().size = new Vector2(ctx().width, ctx().height);

            // calculate scale factor from screen & camera
            float orthoSize = gameObject.scene.getCamera().getOrthoSize();
            float screenSize = ctx().height;
            float scaleFactor = (orthoSize * 2) / screenSize;

            // apply scale
            gameObject.transform.scale.x = scaleFactor;
            gameObject.transform.scale.y = scaleFactor;

            // follow camera
            transform().position = gameObject.scene.getCamera().transform().position;
        }
    }

    /**
     * Sets the render mode
     * @param mode the render mode of the canvas
     * @see Canvas#renderMode
     */
    @LinkFieldInInspector(name = "renderMode", type = LinkType.SETTER, state = LinkState.BOTH)
    public void setRenderMode(RenderMode mode) {
        this.renderMode = mode;

        if (!started) return;

        if (renderMode == RenderMode.CAMERA) {
            // store the previously specified sorting layer to use later if the render mode changes
            if (!sortingLayer.equals(CANVAS_LAYER))
                initialSortingLayer = sortingLayer;

            super._setSortingLayer(CANVAS_LAYER);
        }
        else
            super._setSortingLayer(initialSortingLayer);
    }

    @LinkFieldInInspector(name = "renderMode", type = LinkType.GETTER, state = LinkState.BOTH)
    public RenderMode getRenderMode() {
        return this.renderMode;
    }

    @Override
    public void setSortingLayer(String layer) {
        throw new IllegalStateException("Cannot set sorting layer of Canvas!");
    }
}
