import pxp.engine.data.Vector2;
import pxp.engine.data.geometry.Circle;
import pxp.engine.data.geometry.Line;
import pxp.logging.Debug;
import pxp.util.Mathf;

public class CollisionTest
{
    public static void lineLine() {
        Vector2 A = new Vector2(4.20902f, 6.3703f);
        Vector2 B = new Vector2(5.73947f, 13.56344f);

        Vector2 C = new Vector2(2.02265f, 12.33907f);
        Vector2 D = new Vector2(8.64734f, 9.84662f);

        float tTop      = (D.x - C.x) * (A.y - C.y) - (D.y - C.y) * (A.x - C.x);
        float uTop      = (C.y - A.y) * (A.x - B.x) - (C.x - A.x) * (A.y - B.y);
        float bottom    = (D.y - C.y) * (B.x - A.x) - (D.x - C.x) * (B.y - A.y);

        if (bottom != 0) {
            float t = tTop / bottom;
            float u = uTop / bottom;
            if (t >= 0 && t <= 1 && u >= 0 && u <= 1) {
                float x = Mathf.lerp(A.x,B.x, t);
                float y = Mathf.lerp(A.y,B.y, t);
                System.out.println("[" + x + ", " + y + "]");
            }
            else
                System.out.println("No intersection");
        }
        else
            System.out.println("No intersection");
    }

    private static float triangleArea(Vector2 A, Vector2 B, Vector2 C) {
        Vector2 AB = new Vector2(B.x - A.x, B.y - A.y);
        Vector2 AC = new Vector2(C.x - A.x, C.y - A.y);
        float cross = AB.x * AC.y - AB.y * AC.x;

        return Mathf.abs(cross) / 2f;
    }

    static Vector2 pointOnLine(Vector2 A, Vector2 B, Vector2 P) {
        Vector2 AP = Vector2.subtract(P, A);
        Vector2 AB = Vector2.subtract(B, A);

        float t = AP.dot(AB) / AB.dot(AB);
        t = Mathf.clamp01(t);

        return Vector2.add(A, Vector2.multiply(AB, t));
    }

    public static void lineCircle() {
        Vector2 P = new Vector2(4, 11);
        Vector2 Q = new Vector2(1, 8);

        Vector2 O = new Vector2(4, 8);
        float rad = 5f;

        float minDist = 0;
        float maxDist = Mathf.max(O.distance(P), O.distance(Q));

        Vector2 OP = Vector2.vector(O, P);
        Vector2 QP = Vector2.vector(Q, P);

        Vector2 OQ = Vector2.vector(O, Q);
        Vector2 PQ = Vector2.vector(P, Q);
        if (OP.dot(QP) > 0 && OQ.dot(PQ) > 0)
            minDist = ( 2 * triangleArea(O,P,Q) ) / P.distance(Q);
        else
            minDist = Mathf.min(O.distance(P), O.distance(Q));

//        System.out.println(triangleArea(O,P,Q));
//        System.out.println(OQ.dot(PQ));

        if ((minDist <= rad && maxDist >= rad) || (O.distance(P) < rad || O.distance(Q) < rad)) {
            System.out.println("collides");
            Vector2 pointOnLine = pointOnLine(P,Q,O);
            System.out.println("collides at: " + pointOnLine);
        }
        else
            System.out.println("doesn't collide");
    }

    public static void main(String[] args) {
//        lineCircle();
//        Circle c1 = new Circle(new Vector2(4, 8), 2.5f);
//        Line line = new Line(new Vector2(1, 8), new Vector2(4, 11));
//
//        Vector2 pushBack = new Vector2();
//        System.out.println(line.intersectTestCircle(c1, pushBack));
//        System.out.println(pushBack);

//        Vector2 A = new Vector2(11,9);
//        Vector2 B = new Vector2(13,9);
//
//        float t = .25f;
//
//        float x = Mathf.lerp(A.x, B.x, t);
//        float y = Mathf.lerp(A.y, B.y, t);
//
//        float xR = Mathf.lerp(B.x, A.x, 1f - t);
//        float yR = Mathf.lerp(B.y, A.y, 1f - t);
//
//        Debug.log(xR + ", " + yR);

//        Line line1 = new Line(new Vector2(16.22763f, 8.25505f), new Vector2(19.97755f, 5.69046f));
//        Line line2 = new Line(new Vector2(18.12741f, 9.97779f), new Vector2(15.31663f, 5.53035f));

//        Line line1 = new Line(new Vector2(17.64354f, 7.86174f), new Vector2(12.64854f, 7.96662f));
//        Line line2 = new Line(new Vector2(19.02011f, 10.22158f), new Vector2(15.11326f, 6.24918f));
//
//        Vector2[] positions = new Vector2[2];
//        line1.intersectTest(line2, positions);
//
//        Debug.log(positions);

//        Circle c1 = new Circle(new Vector2(14, 26), 2.75047f);
//        Circle c2 = new Circle(new Vector2(18.51195f, 28.37353f), 3.58717f);
//
//        Vector2 pushBack = new Vector2();
//        c1.intersectTest(c2, pushBack);
//
//        Debug.log(pushBack);

        Line line1 = new Line(new Vector2(32.17702f, 30.63338f), new Vector2(26.32734f, 29.84386f));
        Line line2 = new Line(new Vector2(28.78564f, 31.72796f), new Vector2(34.00729f, 28.62368f));

        Vector2[] positions = new Vector2[2];
        line1.intersectTest(line2, positions);

        float dX = line1.a.x - positions[0].x;
        float dY = line1.a.y - positions[0].y;

        Debug.log(dX + " " + dY);

//        Debug.log(positions);
    }


}