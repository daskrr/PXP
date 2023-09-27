package net.daskrr.cmgt.pxp.core.component;

import net.daskrr.cmgt.pxp.data.DrawEnvironment;

/**
 * The Canvas Component makes using the Processing methods possible and contained within a GameObject<br/>
 * There are two ways to use this component: Create a new instance of it and give it a DrawEnvironment to use, or create
 * a super class of it and override the render() method.
 * @see DrawEnvironment
 * @see Canvas#render()
 */
public class Canvas extends Renderer
{
    /**
     * The DrawEnvironment to use when rendering (Nullable)
     */
    private DrawEnvironment draw = null;

    /**
     * Creates a new Canvas component
     */
    public Canvas() { }

    /**
     * Creates a new Canvas Component with a DrawEnvironment
     * @param draw the DrawEnvironment (recommended usage is interface implementation rather than lambda, as the lambda can't inherit PConstants)
     * @see processing.core.PConstants
     */
    public Canvas(DrawEnvironment draw) {
        this.draw = draw;
    }

    @Override
    public void render() {
        if (draw != null)
            draw.accept(ctx());
    }
}
