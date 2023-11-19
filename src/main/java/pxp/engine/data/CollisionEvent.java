package pxp.engine.data;

import pxp.engine.data.collision.Collision;

/**
 * A collision event is used to propagate the collision to components
 */
public class CollisionEvent
{
   public Time eventTime;
   public Collision collision;

   public CollisionEvent(Time eventTime, Collision collision) {
      this.eventTime = eventTime;
      this.collision = collision;
   }

   public enum Time {
      ENTER,
      STAY,
      EXIT
   }
}
