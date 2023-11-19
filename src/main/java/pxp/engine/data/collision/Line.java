package pxp.engine.data.collision;

import pxp.engine.data.Vector2;
import pxp.util.Mathf;

public class Line {
   public Vector2 a;
   public Vector2 b;

   public Line(Vector2 a, Vector2 b) {
      this.a = a;
      this.b = b;
   }

   /**
    * Unused
    */
   public boolean segmentIntersectTest(Line other, Vector2 intersectionOut) {
      Vector2 A = this.a.clone();
      Vector2 B = this.b.clone();
      Vector2 C = other.a.clone();
      Vector2 D = other.b.clone();
      float tTop = (D.x - C.x) * (A.y - C.y) - (D.y - C.y) * (A.x - C.x);
      float uTop = (C.y - A.y) * (A.x - B.x) - (C.x - A.x) * (A.y - B.y);
      float bottom = (D.y - C.y) * (B.x - A.x) - (D.x - C.x) * (B.y - A.y);
      if (bottom != 0.0F) {
         float t = tTop / bottom;
         float u = uTop / bottom;
         if (t >= 0.0F && t <= 1.0F && u >= 0.0F && u <= 1.0F) {
            float x = Mathf.lerp(A.x, B.x, t);
            float y = Mathf.lerp(A.y, B.y, t);
            intersectionOut.set(new Vector2(x, y));
            return true;
         }
      }

      return false;
   }

   /** @deprecated */
   @Deprecated
   public boolean intersectTest(Line other, Vector2[] pushBackPositions) {
      Vector2 A = this.a.clone();
      Vector2 B = this.b.clone();
      Vector2 C = other.a.clone();
      Vector2 D = other.b.clone();
      float tTop = (D.x - C.x) * (A.y - C.y) - (D.y - C.y) * (A.x - C.x);
      float uTop = (C.y - A.y) * (A.x - B.x) - (C.x - A.x) * (A.y - B.y);
      float bottom = (D.y - C.y) * (B.x - A.x) - (D.x - C.x) * (B.y - A.y);
      if (bottom != 0.0F) {
         float t = tTop / bottom;
         float u = uTop / bottom;
         if (t >= 0.0F && t <= 1.0F && u >= 0.0F && u <= 1.0F) {
            float x = Mathf.lerp(A.x, B.x, t);
            float y = Mathf.lerp(A.y, B.y, t);
            Vector2 intersect = new Vector2(x, y);
            float dist1 = A.distance(intersect);
            float dist2 = B.distance(intersect);
            if (dist1 > dist2) {
               t = 1.0F - t;
            }

            pushBackPositions[0] = new Vector2(Mathf.lerpUnclamped(A.x, B.x, t), Mathf.lerpUnclamped(A.y, B.y, t));
            pushBackPositions[1] = new Vector2(Mathf.lerpUnclamped(A.x, B.x, 1.0F + t), Mathf.lerpUnclamped(A.y, B.y, 1.0F + t));
            return true;
         }
      }

      return false;
   }

   /**
    * Tests if a circle intersects with a line, and gives out a reflect velocity
    * @param circle the circle to check against
    * @param reflect the OUT reflect velocity vector
    * @return whether this line intersects with the circle
    */
   public boolean intersectTestCircle(Circle circle, Vector2 reflect) {
      Vector2 P = this.a.clone();
      Vector2 Q = this.b.clone();
      Vector2 O = circle.center.clone();

      float rad = circle.radius;
      float maxDist = Mathf.max(O.distance(P), O.distance(Q));

      Vector2 OP = Vector2.vector(O, P);
      Vector2 QP = Vector2.vector(Q, P);
      Vector2 OQ = Vector2.vector(O, Q);
      Vector2 PQ = Vector2.vector(P, Q);

      float minDist;
      if (OP.dot(QP) > 0.0F && OQ.dot(PQ) > 0.0F)
         minDist = 2.0F * GeometryUtil.triangleArea(O, P, Q) / P.distance(Q);
      else
         minDist = Mathf.min(O.distance(P), O.distance(Q));

      if ((!(minDist <= rad) || !(maxDist >= rad)) && !(O.distance(P) < rad) && !(O.distance(Q) < rad))
         return false;
      else {
         Vector2 Op = this.pointOnLine(O);

         float distToMove = rad - O.distance(Op);

         Vector2 direction = Vector2.vector(Op, O);
         direction.normalize();
         direction.multiply(distToMove);

         reflect.set(direction);
         return true;
      }
   }

   public Vector2 pointOnLine(Vector2 P) {
      Vector2 A = this.a.clone();
      Vector2 B = this.b.clone();
      P = P.clone();

      Vector2 AP = Vector2.subtract(P, A);
      Vector2 AB = Vector2.subtract(B, A);

      float t = AP.dot(AB) / AB.dot(AB);
      t = Mathf.clamp01(t);

      return Vector2.add(A, Vector2.multiply(AB, t));
   }
}
