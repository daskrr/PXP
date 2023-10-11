package pxp.engine.data.geometry;

import pxp.engine.data.Vector2;
import pxp.util.Mathf;

public class Line
{
    public Vector2 a;
    public Vector2 b;

    public Line(Vector2 a, Vector2 b) {
        this.a = a;
        this.b = b;
    }

    /**
     * Calculates whether this line intersects the given line and sets the 2 vectors of pushBackPositions (A & B of the
     * line) to the closest position to the other line where this line doesn't intersect it
     * @param other the line to test against
     * @param pushBackPositions the out Vector2[] with 2 allocated indices for the A & B points of the line
     * @return whether the lines intersect
     */
    public boolean intersectTest(Line other, Vector2[] pushBackPositions) {
        Vector2 A = this.a.clone();
        Vector2 B = this.b.clone();

        Vector2 C = other.a.clone();
        Vector2 D = other.b.clone();

        float tTop      = (D.x - C.x) * (A.y - C.y) - (D.y - C.y) * (A.x - C.x);
        float uTop      = (C.y - A.y) * (A.x - B.x) - (C.x - A.x) * (A.y - B.y);
        float bottom    = (D.y - C.y) * (B.x - A.x) - (D.x - C.x) * (B.y - A.y);

        if (bottom != 0) {
            float t = tTop / bottom;
            float u = uTop / bottom;

            if (t >= 0 && t <= 1 && u >= 0 && u <= 1) {
                float x = Mathf.lerp(A.x,B.x, t);
                float y = Mathf.lerp(A.y,B.y, t);
                Vector2 intersect = new Vector2(x, y);

                // check which of A or B is closer to the intersection
                float dist1 = A.distance(intersect);
                float dist2 = B.distance(intersect);

                // if A is closer to intersection point, t doesn't need to be swapped
                // however, if it isn't, then we "rotate" it around
                if (dist1 > dist2)
                    t = 1f - t;

                pushBackPositions[0] = new Vector2(Mathf.lerpUnclamped(A.x,B.x, t), Mathf.lerpUnclamped(A.y,B.y, t));
                pushBackPositions[1] = new Vector2(Mathf.lerpUnclamped(A.x,B.x, 1f + t), Mathf.lerpUnclamped(A.y,B.y, 1f + t));

                return true;
            }
        }

        return false;
    }

    /**
     * Calculates whether this line intersects the given circle and sets the 2 vectors of pushBackPositions (A & B of the
     * line) to the closest position to the circle where the shapes do not intersect.<br/>
     * <i>The pushBackPositions array only needs to have 2 indices allocated, but they can be null.</i>
     * @param circle the circle to check against
     * @param pushBackPositions the out Vector2[] with 2 allocated indices for the A & B points of the line
     * @return whether the line intersects the circle
     */
    public boolean intersectTestCircle(Circle circle, Vector2[] pushBackPositions) {
        if (pushBackPositions.length != 2)
            throw new IllegalArgumentException("pushBackPosition needs to have a length of 2!");

        Vector2 P = this.a.clone();
        Vector2 Q = this.b.clone();

        Vector2 O = circle.center.clone();
        float rad = circle.radius;

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
            Vector2 Op = Util.pointOnLine(this,O);
            // distance to move is the amount left on the radius when subtracting the distance from O to O' (Op)
            float distToMove = rad - O.distance(Op);
            // direction to move in
            Vector2 direction = Vector2.vector(Op, O);
            direction.normalize(); // normalise it
            // multiply direction vector by distance to move
            direction.multiply(distToMove);
            Vector2 Pp = Vector2.add(P, direction);
            Vector2 Qp = Vector2.add(Q, direction);

            pushBackPositions[0] = Pp;
            pushBackPositions[1] = Qp;

            return true;
        }

        return false;
    }
}
