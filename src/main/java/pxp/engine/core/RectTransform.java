package pxp.engine.core;

import pxp.engine.core.component.PivotedRenderer;
import pxp.engine.data.Pivot;
import pxp.engine.data.Rect;
import pxp.engine.data.Vector2;
import pxp.engine.data.Vector3;
import pxp.engine.data.ui.Anchor;

/**
 * The RectTransform is an extension of the regular {@link Transform}, which adds the possibility to anchor the game object
 * to a parent (the parent must also have a RectTransform) and set pixel sizes.<br/>
 * This type of Transform is only supposed to be used for UI elements.
 */
public class RectTransform extends Transform
{
    /**
     * The size of this game object
     */
    public Vector2 size = new Vector2(-1f,-1f);

    /**
     * The anchor of the game object. This will place this game object relative to the size of the parent, given the anchoring position.<br/>
     * <b>NOTE: Setting an anchor changes the pivot of the object (if any) to match the anchor!</b>
     */
    public Anchor anchor = null;

    /**
     * The physical offset calculated from anchor
     */
    public Vector2 anchorTranslation = new Vector2();

    /**
     * Creates a new RectTransform with default position, rotation and scale
     */
    public RectTransform() { }

    /**
     * Creates a new RectTransform given a position and the default rotation and scale
     */
    public RectTransform(Vector2 position) {
        this(position, new Vector3());
    }

    /**
     * Creates a new RectTransform given a position, rotation and the default scale
     */
    public RectTransform(Vector2 position, Vector3 rotation) {
        this(position, rotation, new Vector2(1,1));
    }

    /**
     * Creates a new RectTransform given a position, scale and the default rotation
     */
    public RectTransform(Vector2 position, Vector2 scale) {
        this(position, new Vector3(), scale);
    }

    /**
     * Creates a new RectTransform given a position, rotation and scale
     */
    public RectTransform(Vector2 position, Vector3 rotation, Vector2 scale) {
        this(position, rotation, scale, new Vector2(-1f,-1f));
    }

    /**
     * Creates a new RectTransform given a position, rotation, scale and size
     */
    public RectTransform(Vector2 position, Vector3 rotation, Vector2 scale, Vector2 size) {
        this(position, rotation, scale, size, null);
    }

    /**
     * Creates a new RectTransform given a position, rotation, scale, size and anchor
     */
    public RectTransform(Vector2 position, Vector3 rotation, Vector2 scale, Vector2 size, Anchor anchor) {
        super(position, rotation, scale);

        this.size = size;
        this.anchor = anchor;
    }

    /**
     * Binds the {@link Transform#bind()} methods with the addition of calculating anchors and forcing pivots in order to
     * display elements correctly on the screen
     */
    @Override
    protected void bind(GameProcess context) {
        super.bind(context);

        // check if this rect transform has an anchor
        if (anchor == null) return;

        // check if parent exists and has rect transform and the rect transform size is not -1 (undetermined)
        if (gameObject.parent != null
        &&  gameObject.parent.transform instanceof RectTransform rectTransform
        && (rectTransform.size.x != -1f && rectTransform.size.y != -1f)) {
            Vector2 calcAnchor = this.anchor.calc.apply(rectTransform.size);
            // translate by the anchor calculation and store the amount
            this.anchorTranslation = new Vector2(calcAnchor);
            // context.translate(calcAnchor.x, calcAnchor.y); - moved to super (it was being translated while taking scale into account)

            // check if this has a pivoted renderer
            if (this.gameObject.renderer != null && this.gameObject.renderer instanceof PivotedRenderer pivRend)
                // change the pivot to match with the anchor
                pivRend.pivot = Pivot.valueOf(this.anchor.name());
        }
        else
            // reset anchor translation
            this.anchorTranslation = new Vector2();
    }

    /**
     * Returns the size of the RectTransform, scaled with {@link Transform#scale}
     * @return the scaled size
     */
    public Vector2 getScaledSize() {
        if (this.size.x == -1f || this.size.y == -1f) return this.size.clone();

        Vector2 totalScale = new Vector2(1,1);
        GameObject parent = this.gameObject.parent;
        while (parent != null) {
            totalScale.multiply(parent.transform.scale);
            parent = parent.parent;
        }

        // add this scale
        totalScale.multiply(this.scale);
        // multiply by total scale
        Vector2 size = this.size.clone();
        size.multiply(totalScale);

        return size;
    }

    /**
     * Creates a {@link Rect} from this rect transform, in its local coordinate space with a {@link Pivot#CENTER}.
     */
    public Rect getCenterRect() {
        return new Rect(new Vector2(), this.size.clone(), Pivot.CENTER);
    }

    /**
     * Creates a {@link Rect} from this rect transform, in its local coordinate space with a {@link Pivot#CENTER} and using scaled size
     */
    public Rect getCenterRectScaled() {
        return new Rect(new Vector2(), this.getScaledSize(), Pivot.CENTER);
    }

    /**
     * Checks if the <b>global position</b> provided is within this RectTransform
     * @param position the <b>global</b> position to check
     * @return true if it overlaps
     */
    public boolean checkOverlap(Vector2 position, Pivot pivot) {
        // create rect at position 0,0 because we're going to convert the left, top, bottom, right into world position
        // note: the position could technically be the pivot, but whatever...
        Rect rect = new Rect(new Vector2(), this.size.clone() /* unscaled because we convert local to world */, pivot);

        // get left top and right bottom into two PVectors for matrix multiplication
        // also convert them to world space
        Vector2 leftTop = this.localToWorld(new Vector2(rect.left(), rect.top()));
        Vector2 rightBottom = this.localToWorld(new Vector2(rect.right(), rect.bottom()));

//        System.out.println(position);
//        System.out.println("lt, rb");
//        System.out.println(leftTop);
//        System.out.println(rightBottom);
//        System.out.println();
//        System.out.println(position.x >= leftTop.x && position.x <= rightBottom.x);
//        System.out.println(position.y >= leftTop.y && position.y <= rightBottom.y);
//        System.out.println("------------------------------");

        // check overlap (only rect, doesn't account for rotation even if said rotation is applied)
        return  (position.x >= leftTop.x && position.x <= rightBottom.x) &&
                (position.y >= leftTop.y && position.y <= rightBottom.y);
    }
}
