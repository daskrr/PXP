import pxp.engine.data.Pivot;
import pxp.engine.data.Rect;
import pxp.engine.data.Vector2;

// I need to up my game on unit testing jeez...

public class UnitTest
{
    public static void main(String[] args) {
        Rect rect = new Rect(new Vector2(3,3), new Vector2(4.1875f, 2.5625f), Pivot.CENTER);
        System.out.println(rect.top());
        System.out.println(rect.left());
        System.out.println();
        System.out.println(rect.bottom());
        System.out.println(rect.right());
    }
}
