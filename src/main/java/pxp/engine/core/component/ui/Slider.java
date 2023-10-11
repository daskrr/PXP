package pxp.engine.core.component.ui;

import processing.event.MouseEvent;
import pxp.annotations.*;
import pxp.engine.core.GameObject;
import pxp.engine.core.RectTransform;
import pxp.engine.core.component.PivotedRenderer;
import pxp.engine.core.component.pointer.PointerHandlerDrag;
import pxp.engine.data.Pivot;
import pxp.engine.data.Rect;
import pxp.engine.data.Vector2;
import pxp.engine.data.ui.Anchor;
import pxp.engine.data.ui.InteractableTransition;
import pxp.logging.Debug;
import pxp.util.Mathf;

/**
 * The slider component is used as a link between user input and a fillable rect container that can (but is not mandatory to)
 * be a slider-like UI element.<br/><br/>
 * Using the component at its maximum capacity should be done by placing it on a parent Game Object that contains:
 * <blockquote>
 *     - a background for when the slider is not at 100% width/height<br/>
 *     - a fill rect (Image) that is dynamically changed by this component<br/>
 *     - a handle for the user to interact with and drag which will change the aforementioned fill rect<br/>
 * </blockquote>
 * <i>If the aforementioned elements do not exist as children of the parent Game Object of this component, functionality will break!</i>
 * <br/><br/>
 * The slider also provides a direction of the slider which determines which side is filled (with the fill rect), as well
 * as a min and max value (floating point or integral) for ease of use, removing the need for the developer to calculate
 * the percentage themselves.<br/><br/>
 * <b>The parent game object of this Slider component must have a {@link RectTransform} with a size greater than [0,0]!</b>
 */
@RequiresRectTransform
public class Slider extends Interactable implements PointerHandlerDrag
{
    /**
     * The unique name of the game object which represents the fillable rectangle.<br/>
     * The game object needs to have a {@link RectTransform} attached to it.<br/><br/>
     * <i>If a rect transform is not provided, one will be automatically created and adjusted to the size of the parent
     * game object's RectTransform (of this component)</i>
     */
    @SyncGameObject
    private String fillRect = null;

    /**
     * The unique name of the game object which represents the handle for the slider.<br/>
     * Leave this as null if a handle is not desired.<br/>
     */
    @SyncGameObject
    private String handle = null;

    /**
     * The direction of the slide, which determines from where to where the fill rect goes.
     */
    private SliderDirection direction = SliderDirection.LEFT_TO_RIGHT;

    /**
     * The minimum value of the slider
     */
    public float minValue = 0f;

    /**
     * The maximum value of the slider
     */
    public float maxValue = 1f;

    /**
     * Whether the value of the slider is represented in integral numbers (<code>int</code>) or decimal numbers (<code>float</code>)
     */
    public boolean wholeNumbers = false;

    /**
     * The current value of the slider
     */
    @ValueSliderFloat(minField = "minValue", maxField = "maxValue")
    private float value = 0f;

    /**
     * Whether the mouse is being dragged
     */
    private boolean isDragging = false;

    /**
     * Whether the mouse is being dragged using the handle
     */
    private boolean isDraggingHandle = false;

    /**
     * The RectTransform instance for the fill rect
     */
    private RectTransform fillRectTransform = null;
    /**
     * The GameObject instance for the handle
     */
    private GameObject handleObject = null;

    /**
     * Creates a blank Slider component
     */
    public Slider() { }

    /**
     * Creates a blank Slider component
     */
    public Slider(InteractableTransition transition) {
        super(transition);
    }
    /**
     * Creates a blank Slider component
     */
    public Slider(InteractableTransition transition, String fillRect) {
        this(transition, fillRect, null);
    }
    /**
     * Creates a blank Slider component
     */
    public Slider(InteractableTransition transition, String fillRect, String handle) {
        super(transition);
        this.fillRect = fillRect;
        this.handle = handle;
    }

    @Override
    public void start() {
        if (fillRect == null)
            throw new RuntimeException("Fill Rect cannot be null");

        // fill
        this._getFillRect();

        // handle
        this._getHandle();

        // update the direction, which, in turn, updates the fill rect & handle
        updateDirection();
    }

    @LinkFieldInInspector(name = "direction", type = LinkType.GETTER, state = LinkState.BOTH)
    public SliderDirection getDirection() {
        return this.direction;
    }
    @LinkFieldInInspector(name = "direction", type = LinkType.SETTER, state = LinkState.BOTH)
    public void setDirection(SliderDirection direction) {
        this.direction = direction;

        if (this.started)
            updateDirection();
    }

    @LinkFieldInInspector(name = "value", type = LinkType.GETTER, state = LinkState.BOTH)
    public float getValue() {
        return this.value;
    }
    @LinkFieldInInspector(name = "value", type = LinkType.SETTER, state = LinkState.BOTH)
    public void setValue(float value) {
        this.value = wholeNumbers ? Mathf.floor(value) : value;

        if (this.started) {
            updateFill();
            updateHandle();
        }
    }

    @LinkFieldInInspector(name = "fillRect", type = LinkType.GETTER, state = LinkState.BOTH)
    public String getFillRect() {
        return this.fillRect;
    }
    @LinkFieldInInspector(name = "fillRect", type = LinkType.SETTER, state = LinkState.BOTH)
    public void setFillRect(String fillRect) {
        this.fillRect = fillRect;

        if (this.started)
            this._getFillRect();
    }

    @LinkFieldInInspector(name = "handle", type = LinkType.GETTER, state = LinkState.BOTH)
    public String getHandle() {
        return this.handle;
    }
    @LinkFieldInInspector(name = "handle", type = LinkType.SETTER, state = LinkState.BOTH)
    public void setHandle(String handle) {
        this.handle = handle;

        if (this.started)
            this._getHandle();
    }

    /**
     * Looks for the fill rect and saves it<br/>
     * Converts the object's Transform into a RectTransform if it is not a rect transform already.
     */
    private void _getFillRect() {
        GameObject fill = gameObject.getGameObject(this.fillRect);
        if (fill == null)
            throw new RuntimeException("Fill Rect could not be found. Are you sure the fill rect is a child of this game object?");
        if (!(fill.transform instanceof RectTransform)) {
            fill.transform = fill.transform.toRectTransform();
            ((RectTransform) fill.transform).size = rectTransform().size.clone();
        }

        this.fillRectTransform = (RectTransform) fill.transform;
    }
    /**
     * Looks for the handle object and saves it
     */
    private void _getHandle() {
        if (this.handle == null) return;

        GameObject handle = gameObject.getGameObject(this.handle);
        if (handle == null)
            Debug.warn("Slider[" + gameObject.name + "] Could not find handle. Are you sure the handle is a child of this game object?");

        if (handle != null) {
            if (!(handle.transform instanceof RectTransform)) {
                handle.transform = handle.transform.toRectTransform();

                // determine size based on direction
                float size = 1f;
                switch (this.direction) {
                    case LEFT_TO_RIGHT, RIGHT_TO_LEFT -> size = rectTransform().size.x;
                    case TOP_TO_BOTTOM, BOTTOM_TO_TOP -> size = rectTransform().size.y;
                }

                // set size
                ((RectTransform) handle.transform).size = new Vector2(size, size);
                Debug.log("Slider[" + gameObject.name + "] Handle did not have a RectTransform, " +
                        "so one was automatically provided with size: [" + size + ", " + size + "]");
            }

            // force pivot center
            if (handle.renderer != null)
                if (handle.renderer instanceof PivotedRenderer pvRend)
                    if (pvRend.pivot != Pivot.CENTER) {
                        pvRend.pivot = Pivot.CENTER;
                        Debug.log("Slider[" + gameObject.name + "] Handle pivot was forced to CENTER.");
                    }
        }

        this.handleObject = handle;
    }

    /**
     * Updates the {@link Slider#fillRectTransform} object, based on the current value
     */
    private void updateFill() {
        float percent = toPercent(this.minValue, this.maxValue, this.value);
        switch (this.direction) {
            case LEFT_TO_RIGHT, RIGHT_TO_LEFT -> this.fillRectTransform.size.x = rectTransform().size.x * percent;
            case TOP_TO_BOTTOM, BOTTOM_TO_TOP -> this.fillRectTransform.size.y = rectTransform().size.y * percent;
        }
    }
    /**
     * Updates the {@link Slider#handleObject} object, based on the current value
     */
    private void updateHandle() {
        if (this.handleObject == null) return;

        // get percent and calculate a rect for this object with pivot center
        float percent = toPercent(this.minValue, this.maxValue, this.value);
        Rect rect = rectTransform().getCenterRect();

        // depending on direction, calculate where the handle will be using #fromPercent
        switch (this.direction) {
            case LEFT_TO_RIGHT -> this.handleObject.transform.position.x = fromPercent(rect.left(), rect.right(), percent);
            case RIGHT_TO_LEFT -> this.handleObject.transform.position.x = fromPercent(rect.right(), rect.left(), percent);
            case TOP_TO_BOTTOM -> this.handleObject.transform.position.y = fromPercent(rect.top(), rect.bottom(), percent);
            case BOTTOM_TO_TOP -> this.handleObject.transform.position.y = fromPercent(rect.bottom(), rect.top(), percent);
        }
    }

    /**
     * Updates the {@link Slider#fillRectTransform}'s anchor, based on the current direction<br/>
     * Also updates fill & handle (using {@link Slider#updateFill()}, {@link Slider#updateHandle()})
     */
    private void updateDirection() {
        // simply just change the anchor of the fill rect, since after changing its size it will automatically go the correct direction
        switch (this.direction) {
            case LEFT_TO_RIGHT -> this.fillRectTransform.anchor = Anchor.CENTER_LEFT;
            case RIGHT_TO_LEFT -> this.fillRectTransform.anchor = Anchor.CENTER_RIGHT;
            case TOP_TO_BOTTOM -> this.fillRectTransform.anchor = Anchor.TOP_CENTER;
            case BOTTOM_TO_TOP -> this.fillRectTransform.anchor = Anchor.BOTTOM_CENTER;
        }

        // also update fill and handle
        updateFill();
        updateHandle();
    }

    private float toPercent(float min, float max, float input) {
        float range = max - min;
        float correctedStartValue = input - min;

        return correctedStartValue / range;
    }
    private float fromPercent(float min, float max, float percent) {
        return (percent * (max - min)) + min;
    }

    /**
     * Overridden to accommodate for half of the handle not being draggable
     */
    @Override
    public boolean checkOverlap(Vector2 mousePos) {
        Vector2 worldPos = this.gameObject.scene.getCamera().screenToWorldPosition(mousePos);

        // create rect at position 0,0 because we're going to convert the left, top, bottom, right into world position
        Rect rect = new Rect(new Vector2(), rectTransform().size.clone(), this.pivot);

        // accommodate for handle, if one exists
        Vector2 handleSize = new Vector2();
        if (handleObject != null)
            handleSize = ((RectTransform) handleObject.transform).size;

        // get left top and right bottom into two PVectors for matrix multiplication
        // also convert them to world space
        // also depending on the direction, accommodate for handle
        Vector2 leftTop = new Vector2();
        Vector2 rightBottom = new Vector2();
        switch (this.direction) {
            case LEFT_TO_RIGHT, RIGHT_TO_LEFT -> {
                leftTop = rectTransform().localToWorld(new Vector2(
                        rect.left() - handleSize.x / 2f,
                        rect.top()
                ));
                rightBottom = rectTransform().localToWorld(new Vector2(
                        rect.right() + handleSize.x / 2f,
                        rect.bottom()
                ));
            }
            case TOP_TO_BOTTOM, BOTTOM_TO_TOP -> {
                leftTop = rectTransform().localToWorld(new Vector2(
                        rect.left(),
                        rect.top() - handleSize.y / 2f
                ));
                rightBottom = rectTransform().localToWorld(new Vector2(
                        rect.right(),
                        rect.bottom() + handleSize.y / 2f
                ));
            }
        }

        // check overlap (only rect, doesn't account for rotation even if said rotation is applied)
        return  (worldPos.x >= leftTop.x && worldPos.x <= rightBottom.x) &&
                (worldPos.y >= leftTop.y && worldPos.y <= rightBottom.y);
    }

    @Override
    public void setDragging(boolean dragging) {
        this.isDragging = dragging;
    }
    @Override
    public boolean isDragging() {
        return isDragging;
    }

    private boolean checkHandleOverlap(Vector2 mousePos) {
        Vector2 worldPos = this.gameObject.scene.getCamera().screenToWorldPosition(mousePos);
        return ((RectTransform) handleObject.transform).checkOverlap(worldPos, Pivot.CENTER);
    }

    private float calcValueFromMousePos(Vector2 mousePos) {
        mousePos = mousePos.clone(); // prevent fuckery

        // convert mouse position to world
        mousePos = gameObject.scene.getCamera().screenToWorldPosition(mousePos);
        // then world to local
        mousePos = transform().worldToLocal(mousePos);

        Rect rect = rectTransform().getCenterRectScaled();
        float minValue = 0f;
        float maxValue = 0f;
        float value = 0f;

        // depending on the direction store x or y of mouse pos and store min and max (from rect of this object)
        switch (this.direction) {
            case LEFT_TO_RIGHT -> {
                value = mousePos.x;
                minValue = rect.left();
                maxValue = rect.right();
            }
            case RIGHT_TO_LEFT -> {
                value = mousePos.x;
                maxValue = rect.left();
                minValue = rect.right();
            }
            case TOP_TO_BOTTOM -> {
                value = mousePos.y;
                minValue = rect.top();
                maxValue = rect.bottom();
            }
            case BOTTOM_TO_TOP -> {
                value = mousePos.y;
                maxValue = rect.top();
                minValue = rect.bottom();
            }
        }

        // calculate to percent and from percent to get final value
        float percent = toPercent(minValue, maxValue, value);
        float finalValue = fromPercent(this.minValue, this.maxValue, percent);

        // limit value
        return Mathf.clamp(finalValue, this.minValue, this.maxValue);
    }

    @Override
    public void mouseDrag(MouseEvent event) {
        Vector2 mousePos = new Vector2(event.getX(), event.getY());

        // check if what's being dragged is the handle
        if (!isDraggingHandle) return;

        // calculate the value
        float value = this.calcValueFromMousePos(mousePos);

        // finally, set the value
        this.setValue(value);
    }

    @Override
    public void mouseDown(MouseEvent event) {
        super.mouseDown(event);

        Vector2 mousePos = new Vector2(event.getX(), event.getY());

        // check if what's being clicked isn't the handle
        if (checkHandleOverlap(mousePos)) return;

        // calculate the value
        float value = this.calcValueFromMousePos(mousePos);

        // finally, set the value
        this.setValue(value);
    }

    @Override
    public void mouseDragStart(MouseEvent event) {
        Vector2 mousePos = new Vector2(event.getX(), event.getY());

        // check if what's being dragged is the handle
        if (!checkHandleOverlap(mousePos)) return;

        this.isDraggingHandle = true;
    }

    @Override
    public void mouseDragStop(MouseEvent event) {
        Vector2 mousePos = new Vector2(event.getX(), event.getY());

        // check if what's being dragged is the handle
        if (!checkHandleOverlap(mousePos)) return;

        this.isDraggingHandle = false;
    }
}
