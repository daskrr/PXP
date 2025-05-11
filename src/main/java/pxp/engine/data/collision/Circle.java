package pxp.engine.data.collision;

import pxp.engine.data.Vector2;
import pxp.util.Mathf;
import pxp.util.Pointer;

// this file was lost, this is a decompiled file of the latest found version
// this happened to multiple files (including BoundingBox and others)

/**
 * A shape that represents a geometrical circle with a radius
 */
public class Circle extends Shape
{
    public Vector2 center;
    public float radius;

    /**
     * Creates a circle given the center and radius (both in world units)
     * @param center the center of the circle
     * @param radius the radius of the circle
     */
    public Circle(Vector2 center, float radius) {
        this.center = center;
        this.radius = radius;
    }

    @Override
    public boolean hitTest(Shape other, Vector2 velocity, Pointer<Vector2> reflectVelocity, Pointer<Float> toiOut, Vector2 contactPoint) {
        if (other instanceof Circle circle)
            return this.hitTest(circle, velocity, reflectVelocity, toiOut, contactPoint);
        else if (other instanceof Box box) {
            Pointer<Vector2> _reflectVelocity = new Pointer<>(null);
            boolean hit = box.hitTest(this, velocity, _reflectVelocity, toiOut, contactPoint);

            if (_reflectVelocity.value != null)
                _reflectVelocity.value.multiply(-1.0F);

            reflectVelocity.value = _reflectVelocity.value;
            return hit;
        }

        return false;
    }

    /**
     * Checks if this circle, given a velocity, collides with another circle, returning whether the collision happened
     * and giving out a reflection velocity and a toi (in the specific case of Circle, TOI shouldn't be used)
     * @param other the other circle to test against
     * @param velocity the velocity of this circle
     * @param reflectVelocity the velocity to subtract from the current velocity in order to get the correct position
     *                        will be set from null to a vector, which lets the collider know to use this instead of toi
     * @param toiOut the time of impact that can be used to lerp from previous position to current position (discrete)
     *               to get the correct position
     * @param contactPoint the point where the circles intersect
     * @return whether the collision happened
     */
    private boolean hitTest(Circle other, Vector2 velocity, Pointer<Vector2> reflectVelocity, Pointer<Float> toiOut, Vector2 contactPoint) {
        /*
         * Note: This code was lost, then recovered from compiled java binaries, ergo it lacks comments.
         */

        Vector2 C1 = this.center.clone();
        Vector2 C2 = other.center.clone();

        float radii = this.radius + other.radius;
        float distance = C1.distance(C2);

        if (distance <= radii) {
            float overlap = radii - distance;
            Vector2 reflect = Vector2.vector(C1, C2).getNormalized();
            reflect.multiply(overlap);

            reflectVelocity.value = reflect;

            Vector2 prevPos = Vector2.subtract(C1, velocity);
            Vector2 recalculatedVelocity = Vector2.subtract(velocity, reflect);
            Vector2 correctPosition = Vector2.add(prevPos, recalculatedVelocity);

            float t = this.radius / correctPosition.distance(C2);
            float x = Mathf.lerp(correctPosition.x, C2.x, t);
            float y = Mathf.lerp(correctPosition.y, C2.y, t);

            contactPoint.set(x, y);

            return true;
        }
        else if (distance - 0.01F <= radii) {
            reflectVelocity.value = new Vector2();
            return true;
        }

        return false;
    }

    /**
     * Unused method to calculate a tangent force synchronized with the velocity that would neatly slide a circle along
     * another circle using the tangent formed between the circles (the perpendicular of C1 -> Contact Point)
     * @param velocity the velocity of this circle
     * @param contactPoint the contact point between the circles
     * @return the tangent velocity
     * @deprecated This method is unused.
     */
    @Deprecated
    public Vector2 calcTangentForce(Vector2 velocity, Vector2 contactPoint) {
        /*
         * Note: This code was lost, then recovered from compiled java binaries, ergo it lacks comments.
         *       This method is now unused and deprecated. It used to allow two circle colliders to slide when touching one another,
         *       however this was only required due to the wrongful calculation of the collision. This was fixed, ergo the method is no longer needed.
         */

        Vector2 O = this.center.clone();
        Vector2 C = contactPoint.clone();

        Vector2 OC = Vector2.vector(O, C);
        OC.normalize();

//        GameProcess.getInstance().fill(255.0F, 255.0F, 0.0F);
//        GameProcess.getInstance().ellipse(C.x, C.y, 0.25F, 0.25F);

        Vector2 CPx = new Vector2(-OC.y, OC.x);
        CPx.add(C);

        Vector2 CPy = new Vector2(OC.y, -OC.x);
        CPy.add(C);

        int sign;
        Vector2 OLy;
        Vector2 Iy;
        float topX;
        float topY;
        float bottom;
        Vector2 CIy;

        if (Mathf.abs(velocity.x) > Mathf.abs(velocity.y)) {
            sign = Mathf.sign(velocity.x);
            OLy = new Vector2(O.x + (float) sign, O.y);

//            GameProcess.getInstance().stroke(255.0F, 0.0F, 0.0F);
//            GameProcess.getInstance().line(O.x, O.y, OLy.x + (float) (sign * 2500), OLy.y);

            Iy = new Vector2();

            topX = (O.x * OLy.y - O.y * OLy.x) * (C.x - CPx.x) - (O.x - OLy.x) * (C.x * CPx.y - C.y * CPx.x);
            topY = (O.x * OLy.y - O.y * OLy.x) * (C.y - CPx.y) - (O.y - OLy.y) * (C.x * CPx.y - C.y * CPx.x);
            bottom = (O.x - OLy.x) * (C.y - CPx.y) - (O.y - OLy.y) * (C.x - CPx.x);

            if (bottom == 0.0F)
                return new Vector2();
            else {
                Iy.x = topX / bottom;
                Iy.y = topY / bottom;

//                GameProcess.getInstance().fill(255.0F, 0.0F, 0.0F);
//                GameProcess.getInstance().ellipse(Iy.x, Iy.y, 0.25F, 0.25F);

                CIy = Vector2.vector(C, Iy);
                CIy.normalize();
                CIy.multiply(Mathf.abs(velocity.x));

//                GameProcess.getInstance().stroke(0.0F, 0.0F, 255.0F);
//                GameProcess.getInstance().line(C.x, C.y, Iy.x, Iy.y);

                return CIy;
            }
        }
        else {
            sign = Mathf.sign(velocity.y);
            OLy = new Vector2(O.x, O.y + (float) sign);

//            GameProcess.getInstance().stroke(255.0F, 0.0F, 0.0F);
//            GameProcess.getInstance().line(O.x, O.y, OLy.x, OLy.y + (float) (sign * 2500));

            Iy = new Vector2();
            topX = (O.x * OLy.y - O.y * OLy.x) * (C.x - CPy.x) - (O.x - OLy.x) * (C.x * CPy.y - C.y * CPy.x);
            topY = (O.x * OLy.y - O.y * OLy.x) * (C.y - CPy.y) - (O.y - OLy.y) * (C.x * CPy.y - C.y * CPy.x);
            bottom = (O.x - OLy.x) * (C.y - CPy.y) - (O.y - OLy.y) * (C.x - CPy.x);

            if (bottom == 0.0F)
                return new Vector2();
            else {
                Iy.x = topX / bottom;
                Iy.y = topY / bottom;

//                GameProcess.getInstance().fill(255.0F, 0.0F, 0.0F);
//                GameProcess.getInstance().ellipse(Iy.x, Iy.y, 0.25F, 0.25F);

                CIy = Vector2.vector(C, Iy);
                CIy.normalize();
                CIy.multiply(Mathf.abs(velocity.y));

//                GameProcess.getInstance().stroke(0.0F, 0.0F, 255.0F);
//                GameProcess.getInstance().line(C.x, C.y, Iy.x, Iy.y);

                return CIy;
            }
        }
    }
}
