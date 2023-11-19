package pxp.engine.data.collision;

import pxp.engine.data.Pivot;
import pxp.engine.data.Rect;
import pxp.engine.data.Vector2;
import pxp.util.Mathf;
import pxp.util.Pointer;

/**
 * An axis aligned rectangle with the ability to calculate its size and hit test other {@link Shape}s.
 */
public class BoundingBox extends Shape {
    public final Vector2 center;
    public final Vector2 halfSize;
    private final Rect rect;

    /**
     * Creates a bounding box given the center and half of the size (both in world units)
     * @param center the center of the rectangle
     * @param halfSize the size of the rectangle (x - width, y - height)
     */
    public BoundingBox(Vector2 center, Vector2 halfSize) {
        this.center = center;
        this.halfSize = halfSize;
        this.rect = new Rect(center, Vector2.multiply(halfSize, 2.0F), Pivot.CENTER);
    }

    @Override
    public boolean hitTest(Shape other, Vector2 velocity, Pointer<Vector2> reflectVelocity, Pointer<Float> toi, Vector2 contactPoint) {
        if (other instanceof BoundingBox aabb)
            return this.hitTest(aabb, velocity, reflectVelocity, toi, contactPoint);
        else if (other instanceof Circle circle)
            return this.hitTest(circle, velocity, reflectVelocity, toi, contactPoint);

        return false;
    }

    /**
     * Checks if this Bounding Box, given a velocity, intersects another Bounding Box and gives out the time of impact.<br/>
     * <i>This doesn't provide a reflectVelocity or a contact point, since they are axis aligned, therefore the points
     * would be the entirety of the line overlap</i>
     * @param aabb the other bounding box
     * @param velocity this aabb's velocity
     * @param reflectVelocity the out velocity
     * @param toiOut the out time of impact
     * @param contactPoint the out contact point
     * @return whether the bounding boxes collide
     */
    private boolean hitTest(BoundingBox aabb, Vector2 velocity, Pointer<Vector2> reflectVelocity, Pointer<Float> toiOut, Vector2 contactPoint) {
        /*
         * Note: This code was lost, then recovered from compiled java binaries, ergo it lacks comments.
         */

        Vector2 O1 = this.center.clone();
        Vector2 O2 = aabb.center.clone();

        boolean collideX = false;
        boolean collideY = false;

        float tX = 0.0F;
        float tY = 0.0F;

        float minDistanceX = this.halfSize.x + aabb.halfSize.x;

        float distanceX = Mathf.abs(O1.x - O2.x);
        float overlapX = minDistanceX - distanceX;

        if (overlapX > 0.0F) {
            collideX = true;

            if (velocity.x != 0.0F)
                tX = 1.0F - overlapX / Mathf.abs(velocity.x);
        }

        float minDistanceY = this.halfSize.y + aabb.halfSize.y;

        float distanceY = Mathf.abs(O1.y - O2.y);
        float overlapY = minDistanceY - distanceY;

        if (overlapY > 0.0F) {
            collideY = true;

            if (velocity.y != 0.0F)
                tY = 1.0F - overlapY / Mathf.abs(velocity.y);
        }

        float t = Mathf.max(tX, tY);
        float x = Mathf.lerpUnclamped(velocity.x, 0.0F, t);
        float y = Mathf.lerpUnclamped(velocity.y, 0.0F, t);

        reflectVelocity.value = new Vector2(x, y);

        if ((collideX || !(overlapX + 0.01F > 0.0F) || !collideY) && (collideY || !(overlapY + 0.01F > 0.0F) || !collideX) &&
            (collideX || !(overlapX + 0.01F > 0.0F) || collideY || !(overlapY + 0.01F > 0.0F)))
            return collideX && collideY;
        else {
            reflectVelocity.value = new Vector2();
            return true;
        }
    }

    /**
     * Checks if this Bounding Box, given a velocity, intersects a circle and gives out the reflection velocity
     * <i>This doesn't provide a time of impact or a contact point.</i>
     * @param circle the circle
     * @param velocity this aabb's velocity
     * @param reflectVelocity the out velocity
     * @param toiOut the out time of impact
     * @param contactPoint the out contact point
     * @return whether the bounding box and the circle collide
     */
    private boolean hitTest(Circle circle, Vector2 velocity, Pointer<Vector2> reflectVelocity, Pointer<Float> toiOut, Vector2 contactPoint) {
        /*
         * Note: This code was lost, then recovered from compiled java binaries, ergo it lacks comments.
         */

        Line top = new Line(this.rect.leftTop(), this.rect.rightTop());
        Line right = new Line(this.rect.rightTop(), this.rect.rightBottom());
        Line bottom = new Line(this.rect.rightBottom(), this.rect.leftBottom());
        Line left = new Line(this.rect.leftBottom(), this.rect.leftTop());

        Vector2 reflect1 = new Vector2();
        boolean intersects1 = top.intersectTestCircle(circle, reflect1);
        Vector2 reflect2 = new Vector2();
        boolean intersects2 = right.intersectTestCircle(circle, reflect2);
        Vector2 reflect3 = new Vector2();
        boolean intersects3 = bottom.intersectTestCircle(circle, reflect3);
        Vector2 reflect4 = new Vector2();
        boolean intersects4 = left.intersectTestCircle(circle, reflect4);

        boolean intersects = intersects1 || intersects2 || intersects3 || intersects4;

        if (!intersects) {
            toiOut.value = 0.0F;
            return false;
        }
        else {
            Vector2 reflect = new Vector2();
            Vector2 C = circle.center.clone();

            if (this.rect.left() <= C.x && C.x <= this.rect.right() && this.rect.top() <= C.y && C.y <= this.rect.bottom())
                reflect = Vector2.add(reflect1, reflect2, reflect3, reflect4);
            else {
                if (intersects1)
                    reflect = reflect1;

                if (intersects2)
                    reflect = Vector2.max(reflect2, reflect);

                if (intersects3)
                    reflect = Vector2.max(reflect3, reflect);

                if (intersects4)
                    reflect = Vector2.max(reflect4, reflect);
            }

            if (velocity.getMagnitude() > 0.0F)
                reflectVelocity.value = reflect;

            return true;
        }
    }
}
