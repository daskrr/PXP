package pxp.engine.core.component.pointer;

import pxp.engine.data.Vector2;

/**
 * [Internal] The base for handling pointer events
 */
public interface PointerHandlerBase
{
    /**
     * Checks whether the mousePos provided overlaps the desired area
     * @param mousePos the mouse position (will be automatically provided)
     * @return whether the mouse position overlaps the desired area
     */
    boolean checkOverlap(Vector2 mousePos);

    /**
     * Sets the state of hover
     * @param hovering whether the desired area is being hovered
     */
    void setHovering(boolean hovering);

    /**
     * Retrieves the hover state
     * @return the hover state
     */
    boolean isHovering();
}
