package pxp.engine.core.component;

import pxp.annotations.LinkFieldInInspector;
import pxp.annotations.LinkState;
import pxp.annotations.LinkType;
import pxp.engine.data.DrawEnvironment;

/**
 * The Canvas Component makes using the Processing methods possible and contained within a GameObject<br/>
 * There are two ways to use this component: Create a new instance of it and give it a DrawEnvironment to use, or create
 * a super class of it and override the render() method.
 * @see DrawEnvironment
 * @see DrawCanvas#render()
 */
public class DrawCanvas extends Renderer
{
    /**
     * The DrawEnvironment to use when rendering (Nullable)
     */
    public DrawEnvironment draw = null;

    /**
     * Creates a new Canvas component
     */
    public DrawCanvas() { }

    /**
     * Creates a new Canvas Component with a DrawEnvironment
     * @param draw the DrawEnvironment (recommended usage is interface implementation rather than lambda, as the lambda can't inherit PConstants)
     * @see processing.core.PConstants
     */
    public DrawCanvas(DrawEnvironment draw) {
        this.draw = draw;
    }

    @Override
    public void render() {
        super.render();

        if (draw != null)
            draw.accept(ctx());
    }
}
