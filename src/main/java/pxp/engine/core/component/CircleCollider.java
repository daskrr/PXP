package pxp.engine.core.component;

import pxp.engine.data.CollisionEvent;
import pxp.engine.data.Vector2;
import pxp.engine.data.collision.Circle;
import pxp.util.Pointer;

/**
 * The circle collider component represents a 2d circle that is able to detect other colliders and correct the position
 * of the object in a way that makes sure this collider does not overlap any others involved in a collision.
 */
public class CircleCollider extends Collider
{
    /**
     * Additive circle center offset
     */
    public Vector2 centerOffset;
    /**
     * The radius of the circle
     */
    public float radius;

    /**
     * Creates a blank Circle Collider
     */
    public CircleCollider() {
        this(new Vector2(), 0.0F);
    }

    /**
     * Creates a Circle Collider, given the center offset and the radius
     * @param centerOffset the center offset (number of units to add to the transform position)
     * @param radius the radius of the circle
     */
    public CircleCollider(Vector2 centerOffset, float radius) {
        this.centerOffset = centerOffset;
        this.radius = radius;
    }

    @Override
    public void start() {
        this.updateShape();
    }

    @Override
    protected void updateShape() {
        this.shape = new Circle(Vector2.add(this.transform().getWorldPosition(), this.centerOffset), this.radius);
    }

    @Override
    public boolean collisionCheck(Collider other, Pointer<Vector2> reflectVelocity, Pointer<Float> toi, Vector2 contact) {
        return this.shape.hitTest(other.shape, this.currentVelocity.clone(), reflectVelocity, toi, contact);
    }

    @Override
    public void gizmosDraw() {
        Vector2 pos = this.centerOffset;
        this.ctx().stroke(gizmosColor.getHex());
        this.ctx().ellipseMode(2);
        this.ctx().ellipse(pos.x, pos.y, this.radius, this.radius);
        this.ctx().ellipseMode(3);
        this.ctx().noStroke();
    }
}
