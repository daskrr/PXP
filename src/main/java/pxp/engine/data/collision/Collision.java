package pxp.engine.data.collision;

import pxp.engine.core.component.Collider;
import pxp.engine.data.Vector2;

/**
 * A collision info object. Holds data of a collision.
 */
public class Collision
{
   /**
    * The main collider
    */
   public Collider collider;
   /**
    * The incoming collider that was collided with
    */
   public Collider otherCollider;
   /**
    * The contact point of the collision<br/>
    * <b>Can't be null, but will be a Vector2 zero!</b>
    */
   public Vector2 contactPoint;

   /**
    * Creates a collision info object
    * @param collider the main collider
    * @param otherCollider the other collider
    */
   public Collision(Collider collider, Collider otherCollider) {
      this.collider = collider;
      this.otherCollider = otherCollider;
      this.contactPoint = new Vector2();
   }
}
