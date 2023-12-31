package pxp.engine.data.collision;

import pxp.engine.data.Vector2;
import pxp.util.Mathf;

public class GeometryUtil {
   public static float triangleArea(Vector2 A, Vector2 B, Vector2 C) {
      Vector2 AB = new Vector2(B.x - A.x, B.y - A.y);
      Vector2 AC = new Vector2(C.x - A.x, C.y - A.y);
      float cross = AB.x * AC.y - AB.y * AC.x;
      return Mathf.abs(cross) / 2.0F;
   }
}
