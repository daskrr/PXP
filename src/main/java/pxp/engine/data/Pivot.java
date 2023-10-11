package pxp.engine.data;

import java.util.function.Function;

/**
 * The Pivot point (typically of a sprite) which determines where the sprite's origin will be placed relative to the transform position
 */
public enum Pivot
{
    TOP_LEFT(size -> {
        Vector2 pivot = new Vector2();

        return pivot;
    }),
    TOP_CENTER(size -> {
        Vector2 pivot = new Vector2();
        pivot.x = -size.x / 2;

        return pivot;
    }),
    TOP_RIGHT(size -> {
        Vector2 pivot = new Vector2();
        pivot.x = -size.x;

        return pivot;
    }),
    BOTTOM_LEFT(size -> {
        Vector2 pivot = new Vector2();
        pivot.y = -size.y;

        return pivot;
    }),
    BOTTOM_CENTER(size -> {
        Vector2 pivot = new Vector2();
        pivot.x = -size.x / 2;
        pivot.y = -size.y;

        return pivot;
    }),
    BOTTOM_RIGHT(size -> {
        Vector2 pivot = new Vector2();
        pivot.x = -size.x;
        pivot.y = -size.y;

        return pivot;
    }),
    CENTER_LEFT(size -> {
        Vector2 pivot = new Vector2();
        pivot.y = -size.y / 2;

        return pivot;
    }),
    CENTER(size -> {
        Vector2 pivot = new Vector2();
        pivot.x = -size.x / 2;
        pivot.y = -size.y / 2;

        return pivot;
    }),
    CENTER_RIGHT(size -> {
        Vector2 pivot = new Vector2();
        pivot.x = -size.x;
        pivot.y = -size.y / 2;

        return pivot;
    });

    public final Function<Vector2, Vector2> calc;

    Pivot(Function<Vector2, Vector2> calc) {
        this.calc = calc;
    }
}
