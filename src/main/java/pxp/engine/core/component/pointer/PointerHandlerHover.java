package pxp.engine.core.component.pointer;

import processing.event.MouseEvent;
import pxp.engine.core.component.Component;
import pxp.engine.core.RectTransform;
import pxp.engine.data.Pivot;
import pxp.engine.data.Vector2;

/**
 * Interface used to make a {@link Component} handle hover pointer events.<br/>
 * Events:
 * <blockquote>
 *    - hover (continuous)<br/>
 *    - hover enter<br/>
 *    - hover exit
 * </blockquote>
 * <br/>
 * This comes with methods to be implemented that determine whether the mouse overlaps the game object, which can be
 * determined manually, or automatically at the developer's choice<br/>
 * <i>(for automatic detection, use {@link RectTransform#checkOverlap(Vector2, Pivot)})</i><br/><br/>
 * This also contains methods for handling hover state, which must be implemented.
 */
public interface PointerHandlerHover extends PointerHandlerBase
{
    /**
     * Called every frame when hovered
     * @param event the Processing {@link MouseEvent}
     */
    void mouseHover(MouseEvent event);
    /**
     * Called once when hover state changes to <code>true</code>
     * @param event the Processing {@link MouseEvent}
     */
    void mouseHoverEnter(MouseEvent event);
    /**
     * Called once when hover state changes to <code>false</code>
     * @param event the Processing {@link MouseEvent}
     */
    void mouseHoverExit(MouseEvent event);
}
