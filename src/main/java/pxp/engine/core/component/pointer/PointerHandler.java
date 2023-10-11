package pxp.engine.core.component.pointer;

import pxp.engine.core.component.Component;
import pxp.engine.core.RectTransform;
import pxp.engine.data.Pivot;
import pxp.engine.data.Vector2;

/**
 * Interface used to make a {@link Component} handle pointer events.<br/>
 * Events:
 * <blockquote>
 *    - hover (continuous)<br/>
 *    - hover enter<br/>
 *    - hover exit<br/>
 *    - mouse click<br/>
 *    - mouse drag (continuous)<br/>
 *    - mouse drag start<br/>
 *    - mouse drag stop<br/>
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
public interface PointerHandler extends PointerHandlerDrag, PointerHandlerMouse, PointerHandlerHover, PointerHandlerBase { }
