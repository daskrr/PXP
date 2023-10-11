package pxp.engine.data.geometry;

import pxp.engine.data.Vector2;
import pxp.util.Mathf;

public class Util
{
    public static float triangleArea(Vector2 A, Vector2 B, Vector2 C) {
        Vector2 AB = new Vector2(B.x - A.x, B.y - A.y);
        Vector2 AC = new Vector2(C.x - A.x, C.y - A.y);
        float cross = AB.x * AC.y - AB.y * AC.x;

        return Mathf.abs(cross) / 2f;
    }

    public static Vector2 pointOnLine(Line line, Vector2 P) {
        Vector2 A = line.a.clone();
        Vector2 B = line.b.clone();
        P = P.clone();

        Vector2 AP = Vector2.subtract(P, A);
        Vector2 AB = Vector2.subtract(B, A);

        float t = AP.dot(AB) / AB.dot(AB);
        t = Mathf.clamp01(t);

        return Vector2.add(A, Vector2.multiply(AB, t));
    }
}
