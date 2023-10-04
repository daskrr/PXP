package net.daskrr.cmgt.pxp.data;

import java.util.function.Function;

/**
 * The Pivot point (typically of a sprite) which determines where the sprite's origin will be placed relative to the transform position
 */
public enum Pivot
{
    CENTER(size -> {
        Vector2 pivot = new Vector2();
        pivot.x = -size.x / 2;
        pivot.y = -size.y / 2;

        return pivot;
    }),
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
    LEFT_BOTTOM(size -> {
        Vector2 pivot = new Vector2();
        pivot.y = -size.y;

        return pivot;
    }),
    LEFT_CENTER(size -> {
        Vector2 pivot = new Vector2();
        pivot.y = -size.y / 2;

        return pivot;
    }),
    LEFT_TOP(size -> {
        Vector2 pivot = new Vector2();

        return pivot;
    }),
    RIGHT_BOTTOM(size -> {
        Vector2 pivot = new Vector2();
        pivot.x = -size.x;
        pivot.y = -size.y;

        return pivot;
    }),
    RIGHT_CENTER(size -> {
        Vector2 pivot = new Vector2();
        pivot.x = -size.x;
        pivot.y = -size.y / 2;

        return pivot;
    }),
    RIGHT_TOP(size -> {
        Vector2 pivot = new Vector2();
        pivot.x = -size.x;
        pivot.y = -size.y;

        return pivot;
    });

    public Function<Vector2, Vector2> calculatePivot;

    Pivot(Function<Vector2, Vector2> calculatePivot) {
        this.calculatePivot = calculatePivot;
    }
}
