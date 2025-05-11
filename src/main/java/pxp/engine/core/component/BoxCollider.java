package pxp.engine.core.component;

import pxp.engine.data.Vector2;
import pxp.engine.data.collision.BoundingBox;
import pxp.engine.data.collision.Box;
import pxp.util.Pointer;

/**
 * The box collider component represents a 2d rectangle that is able to detect other colliders and correct the position
 * of the object in a way that makes sure this collider does not overlap any others involved in a collision.<br/>
 * This rectangle is axis aligned, therefore cannot be rotated. It is defined by a center and half of its size on the x- or y-axis.
 */
public class BoxCollider extends Collider
{
    /**
     * Additive circle center offset
     */
    public Vector2 centerOffset;
    /**
     * Half of the size of the axis aligned bounding box
     */
    public Vector2 halfSize;

    /**
     * Creates a blank Box Collider
     */
    public BoxCollider() {
        this(new Vector2(), new Vector2());
    }

    /**
     * Creates a Box Collider, given the center offset and half of the size
     * @param centerOffset the center offset (number of units to add to the transform position)
     * @param halfSize half of the size of the axis aligned bounding box
     */
    public BoxCollider(Vector2 centerOffset, Vector2 halfSize) {
        this.centerOffset = centerOffset;
        this.halfSize = halfSize;
    }

    @Override
    public void start() {
        this.updateShape();
    }

    @Override
    protected void updateShape() {
        this.shape = new Box(this.centerOffset, this.halfSize, this.transform());
    }

    @Override
    public boolean collisionCheck(Collider other, Pointer<Vector2> reflectVelocity, Pointer<Float> toi, Vector2 contact) {
        return this.shape.hitTest(other.shape, this.currentVelocity.clone(), reflectVelocity, toi, contact);
    }

    @Override
    public void gizmosDraw() {
        Vector2 pos = this.centerOffset;
        this.ctx().stroke(gizmosColor.getHex());
        this.ctx().rectMode(3);
        this.ctx().rect(pos.x, pos.y, this.halfSize.x * 2.0F, this.halfSize.y * 2.0F);
        this.ctx().rectMode(0);
        this.ctx().noStroke();
    }
}
