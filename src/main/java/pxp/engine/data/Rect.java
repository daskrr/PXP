package pxp.engine.data;

/**
 * Rectangle class, stores a position, size and pivot, which it can apply to get the four edges
 */
public class Rect
{
    public Vector2 position;
    public Vector2 size;
    public Pivot pivot;

    public Rect(Vector2 position, Vector2 size, Pivot pivot) {
        this.position = position;
        this.size = size;
        this.pivot = pivot;
    }

    /**
     * Top of the rect in the y-axis
     */
    public float top() {
        return this.position.y + pivot.calc.apply(size).y;
    }
    /**
     * Bottom of the rect in the y-axis
     */
    public float bottom() {
        return this.position.y + size.y + pivot.calc.apply(size).y;
    }
    /**
     * Left of the rect in the x-axis
     */
    public float left() {
        return this.position.x + pivot.calc.apply(size).x;
    }
    /**
     * Right of the rect in the x-axis
     */
    public float right() {
        return this.position.x + size.x + pivot.calc.apply(size).x;
    }

    @Override
    public String toString() {
        return "Rect [top: " + top() + ", bottom: " + bottom() + ", left: " + left() + ", right: " + right() + "]";
    }
}
