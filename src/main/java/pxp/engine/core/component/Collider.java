package pxp.engine.core.component;

import pxp.engine.data.CollisionEvent;
import pxp.engine.data.Color;
import pxp.engine.data.LayerMask;
import pxp.engine.data.TriggerCollisionEvent;
import pxp.engine.data.Vector2;
import pxp.engine.data.collision.Shape;
import pxp.util.Mathf;
import pxp.util.Pointer;

/**
 * The base Collider for all types of colliders to use.<br/>
 * Handles basic functions that are required by all types of colliders.
 */
public abstract class Collider extends Component
{
    protected static final Color gizmosColor = new Color(3, 252, 40);

    /**
     * The shape of the collider (e.g.: circle or rect (AABB))
     */
    protected Shape shape;
    /**
     * Whether this collider is a trigger (only registers collisions, but doesn't stop them)
     */
    public boolean trigger = false;
    /**
     * The layer to collide with (there can only be one layer)
     */
    public int layer = LayerMask.nameToId("Default");

    /**
     * The velocity of this frame, calculated by the transform
     */
    protected Vector2 currentVelocity = new Vector2();

    /**
     * Called by the CollisionManager to update the shape and velocity every frame
     */
    public void collisionUpdate() {
        this.currentVelocity = this.transform().calculateVelocity();
        this.updateShape();
    }

    /**
     * Called when a component of the shape is updated (handled by each collider type)
     */
    protected abstract void updateShape();

    /**
     * Called when a collision happens (enter or stay)
     * @param event the collision event
     * @param reflectVelocity the velocity to subtract from current velocity in order to correct the position
     * @param toi the time of impact (expressed as a float from 0-1 (but can exceed the interval))
     */
    public void collide(CollisionEvent event, Pointer<Vector2> reflectVelocity, float toi) {
        if (this.trigger)
            event = TriggerCollisionEvent.convert(event);

        if (!this.trigger && !event.collision.otherCollider.trigger || this.trigger && event.collision.otherCollider.trigger)
            this.gameObject.propagateCollisionEvent(event);

        if (this.trigger)
            return;
        if (event.collision.otherCollider.trigger)
            return;

        if (reflectVelocity.value != null) {
            if (!(this.currentVelocity.getMagnitude() > 0.0F))
                return;

            Vector2 finalVelocity = Vector2.subtract(this.currentVelocity, reflectVelocity.value);
            this.transform().position = Vector2.add(this.transform().lastPosition, finalVelocity);
            this.currentVelocity = finalVelocity;
        }
//        else {
//            float x = Mathf.lerpUnclamped(this.transform().lastPosition.x, this.transform().position.x, toi);
//            float y = Mathf.lerpUnclamped(this.transform().lastPosition.y, this.transform().position.y, toi);
//            this.transform().position = new Vector2(x, y);
//        }

        this.updateShape();
    }

    /**
     * Called when a collision stops (exit)
     * @param event the collision event
     */
    public void stoppedColliding(CollisionEvent event) {
        if (this.trigger)
            event = TriggerCollisionEvent.convert(event);

        if (!this.trigger && !event.collision.otherCollider.trigger || this.trigger && event.collision.otherCollider.trigger)
            this.gameObject.propagateCollisionEvent(event);
    }

    /**
     * The hit test to be performed every frame
     * @param other the other collider
     * @param reflectVelocity the velocity to subtract from current velocity in order to correct the position
     * @param toi time of impact (out - passed as a {@link Pointer<Float>})
     * @param contact the contact point of the shapes
     * @return whether the collision happened
     */
    public abstract boolean collisionCheck(Collider other, Pointer<Vector2> reflectVelocity, Pointer<Float> toi, Vector2 contact);
}
