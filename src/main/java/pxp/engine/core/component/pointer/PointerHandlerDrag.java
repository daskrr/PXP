package pxp.engine.core.component.pointer;

import processing.event.MouseEvent;
import pxp.engine.core.component.Component;
import pxp.engine.core.RectTransform;
import pxp.engine.data.Pivot;
import pxp.engine.data.Vector2;

/**
 * Interface used to make a {@link Component} handle drag pointer events.<br/>
 * Events:
 * <blockquote>
 *    - mouse drag (continuous)<br/>
 *    - mouse drag start<br/>
 *    - mouse drag stop
 * </blockquote>
 * <br/>
 * This comes with methods to be implemented that determine whether the mouse overlaps the game object, which can be
 * determined manually, or automatically at the developer's choice<br/>
 * <i>(for automatic detection, use {@link RectTransform#checkOverlap(Vector2, Pivot)})</i><br/><br/>
 * This also contains methods for handling hover state, which must be implemented.
 */
public interface PointerHandlerDrag extends PointerHandlerBase
{
    /**
     * Sets the state of dragging (set only the first time mouseDrag() is called)
     * @param dragging whether the mouse is being dragged
     */
    void setDragging(boolean dragging);

    /**
     * Retrieves the dragging state
     * @return the dragging state
     */
    boolean isDragging();

    /**
     * Called every frame the mouse is dragged across the screen (on top of this PointerHandler overlap area)<br/>
     * This will still fire until the mouse is released once dragging has been established.
     * @param event the Processing {@link MouseEvent}
     */
    void mouseDrag(MouseEvent event);
    /**
     * Called once when dragging starts.
     * @param event the Processing {@link MouseEvent}
     */
    void mouseDragStart(MouseEvent event);
    /**
     * Called once when dragging stops.
     * @param event the Processing {@link MouseEvent}
     */
    void mouseDragStop(MouseEvent event);
}
