package net.daskrr.cmgt.pxp.core.component;

import net.daskrr.cmgt.pxp.data.DrawEnvironment;

public class Canvas extends Renderer
{
    private DrawEnvironment draw = null;

    public Canvas() { }

    public Canvas(DrawEnvironment draw) {
        this.draw = draw;
    }

    @Override
    public void render() {
        if (draw != null)
            draw.accept(ctx());
    }
}
