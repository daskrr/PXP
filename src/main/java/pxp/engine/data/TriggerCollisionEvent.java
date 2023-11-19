package pxp.engine.data;

import pxp.engine.data.collision.Collision;

/**
 * A trigger collision event is used to propagate the trigger collision to components
 */
public class TriggerCollisionEvent extends CollisionEvent
{
   public TriggerCollisionEvent(Time eventTime, Collision collision) {
      super(eventTime, collision);
   }

   /**
    * Converts a normal collision event to a trigger collision event (if only we had casting overrides)
    * @param colEvt the collision event to convert to a trigger
    * @return the converted trigger collision event
    */
   public static TriggerCollisionEvent convert(CollisionEvent colEvt) {
      return new TriggerCollisionEvent(colEvt.eventTime, colEvt.collision);
   }
}
