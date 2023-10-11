package pxp.engine.data.geometry;

import pxp.engine.data.Vector2;
import pxp.util.Mathf;

public class Circle
{
    public Vector2 center;
    public float radius;

    public Circle(Vector2 center, float radius) {
        this.center = center;
        this.radius = radius;
    }

    /**
     * Calculates whether this circle intersects the given circle and sets the x,y of pushBackPosition to the closest
     * position where the circle does not intersect the other circle.
     * @param circle the circle to test against
     * @param pushBackPosition an out Vector2 that represents the no-intersection position of this circle
     * @return whether the circles intersect
     */
    public boolean intersectTest(Circle circle, Vector2 pushBackPosition) {
        Vector2 C1 = this.center.clone();
        Vector2 C2 = circle.center.clone();

        float radii = this.radius + circle.radius;

        if (C1.distance(C2) <= radii) {
            float distance = C1.distance(C2);
            float overlap = radii - distance;
            float t = (overlap / distance) * -1f;

            float x = Mathf.lerpUnclamped(C1.x, C2.x, t);
            float y = Mathf.lerpUnclamped(C1.y, C2.y, t);

            pushBackPosition.set(x, y);

            return true;
        }

        return false;
    }

    /**
     * Calculates whether this circle intersects the given line and sets the x,y of pushBackPosition to a position
     * where the circle does not intersect the line
     * @param line the line segment to check against
     * @param pushBackPosition the out Vector2 of a position where the circle does not intersect the line
     * @return whether this circle intersects the line
     */
    public boolean intersectTestLine(Line line, Vector2 pushBackPosition) {
        Vector2 P = line.a.clone();
        Vector2 Q = line.b.clone();

        Vector2 O = this.center.clone();
        float rad = this.radius;

        float minDist;
        float maxDist = Mathf.max(O.distance(P), O.distance(Q));

        Vector2 OP = Vector2.vector(O, P);
        Vector2 QP = Vector2.vector(Q, P);

        Vector2 OQ = Vector2.vector(O, Q);
        Vector2 PQ = Vector2.vector(P, Q);
        if (OP.dot(QP) > 0 && OQ.dot(PQ) > 0)
            minDist = ( 2 * Util.triangleArea(O,P,Q) ) / P.distance(Q);
        else
            minDist = Mathf.min(O.distance(P), O.distance(Q));

        if ((minDist <= rad && maxDist >= rad) || (O.distance(P) < rad || O.distance(Q) < rad)) {
            Vector2 pointOnLine = Util.pointOnLine(line,O);
            float dist = O.distance(pointOnLine);
            float radT = rad / dist;
            float pushBackT = 1f - radT;

            float x = Mathf.lerpUnclamped(O.x,pointOnLine.x, pushBackT);
            float y = Mathf.lerpUnclamped(O.y,pointOnLine.y, pushBackT);

            pushBackPosition.set(x, y);

            return true;
        }

        return false;
    }
}
