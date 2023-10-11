package pxp.engine.data.ui;

import pxp.engine.data.Vector2;

import java.util.function.Function;

/**
 * Anchor is a set of positions where objects can be placed depending on a parent. This should only be used with UI elements.
 */
public enum Anchor
{
    TOP_LEFT(size ->        new Vector2(-size.x / 2f,       -size.y / 2f)),
    TOP_CENTER(size ->      new Vector2(0f,                 -size.y / 2f)),
    TOP_RIGHT(size ->       new Vector2(size.x / 2f,        -size.y / 2f)),
    BOTTOM_LEFT(size ->     new Vector2(-size.x / 2f,       size.y / 2f)),
    BOTTOM_CENTER(size ->   new Vector2(0f,                 size.y / 2f)),
    BOTTOM_RIGHT(size ->    new Vector2(size.x / 2f,        size.y / 2f)),
    CENTER_LEFT(size ->     new Vector2(-size.x / 2f,       0)),
    CENTER(size ->   new Vector2(0,                  0)),
    CENTER_RIGHT(size ->    new Vector2(size.x / 2f,        0));

    public final Function<Vector2, Vector2> calc;

    Anchor(Function<Vector2, Vector2> calc) {
        this.calc = calc;
    }
}
