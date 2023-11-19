package pxp.engine.data.collision;

import pxp.engine.data.Vector2;
import pxp.util.Pointer;

/**
 * Abstract Shape class
 */
public abstract class Shape {
   /**
    * Checks if this shape collides with another
    * @param other the other shape to test against
    * @param velocity the velocity of this shape
    * @param reflectVelocity the out velocity vector to correct the position of this shape
    * @param toiOut the out time of impact
    * @param contactPoint the contact point of the collision (if one can be established)
    * @return whether the shapes hit
    */
   public abstract boolean hitTest(Shape other, Vector2 velocity, Pointer<Vector2> reflectVelocity, Pointer<Float> toiOut, Vector2 contactPoint);
}
