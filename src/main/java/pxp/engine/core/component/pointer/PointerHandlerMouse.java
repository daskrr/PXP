package pxp.engine.core.component.pointer;

import processing.event.MouseEvent;
import pxp.engine.core.component.Component;
import pxp.engine.core.RectTransform;
import pxp.engine.data.Pivot;
import pxp.engine.data.Vector2;

/**
 * Interface used to make a {@link Component} handle mouse button pointer events. (except dragging)<br/>
 * Events:
 * <blockquote>
 *    - mouse click<br/>
 *    - mouse scroll<br/>
 *    - mouse down<br/>
 *    - mouse up
 * </blockquote>
 * <br/>
 * This comes with methods to be implemented that determine whether the mouse overlaps the game object, which can be
 * determined manually, or automatically at the developer's choice<br/>
 * <i>(for automatic detection, use {@link RectTransform#checkOverlap(Vector2, Pivot)})</i><br/><br/>
 * This also contains methods for handling hover state, which must be implemented.
 */
public interface PointerHandlerMouse extends PointerHandlerBase
{
    /**
     * Called one when a single click is performed (determined by Processing) (on mouse release / up)
     * @param event the Processing {@link MouseEvent}
     */
    void mouseClick(MouseEvent event);
    /**
     * Called every frame the mouse wheel is used
     * @param event the Processing {@link MouseEvent}
     */
    void mouseScroll(MouseEvent event);
    /**
     * Called every frame when hovered
     * @param event the Processing {@link MouseEvent}
     */
    void mouseDown(MouseEvent event);
    /**
     * Called every frame when hovered
     * @param event the Processing {@link MouseEvent}
     */
    void mouseUp(MouseEvent event);
}
