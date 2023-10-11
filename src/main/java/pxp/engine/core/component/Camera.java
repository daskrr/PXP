package pxp.engine.core.component;

import pxp.annotations.LinkFieldInInspector;
import pxp.annotations.LinkState;
import pxp.annotations.LinkType;
import pxp.annotations.SyncGameObject;
import pxp.engine.core.GameObject;
import pxp.engine.data.Pivot;
import pxp.engine.data.Rect;
import pxp.engine.data.Vector2;
import pxp.engine.data.Vector3;

/**
 * The Camera Component represents the eyes through which the game is seen. This component is required for the game to run.<br/>
 * The Camera can be repositioned, which will be reflected in the view of the game.
 */
public class Camera extends Component
{
    /**
     * Represents half of the height of the game
     */
    private float orthoSize = 7f;
    /**
     * Represents how many units fit on half of the size and half of the with of the window
     */
    private Vector2 unitSize;

    /**
     * The camera's position on the Z axis (Camera is looking at -Z by default)
     */
    public float cameraZ = 10f;

    /**
     * The unique name of the game object that the camera should follow<br/>
     * <i>Note: if this is null or the object cannot be found, instead of throwing an exception, the camera will stay stationary.</i><br/>
     * <i>Note: if this is assigned, but the game object cannot be found, the camera will try to find it <b>every frame</b>.</i>
     */
    @SyncGameObject
    private String follow = null;

    /**
     * The game object instance to follow
     */
    private GameObject followGameObject = null;

    /**
     * Creates a blank camera component
     */
    public Camera() { }

    /**
     * Creates a camera component given an orthographic size
     * @param orthoSize a float which represents half of the height of the game
     */
    public Camera(float orthoSize) {
        this.orthoSize = orthoSize;
    }

    @Override
    public void start() {
        calcUnitSize();

        this.findTarget();
    }

    @Override
    public void update() {
        this.findTarget();
    }

    /**
     * Sets the orthographic size of the camera
     * @param orthoSize a float which represents half of the height of the game
     */
    @LinkFieldInInspector(name = "orthoSize", type = LinkType.SETTER, state = LinkState.BOTH)
    public void setOrthoSize(float orthoSize) {
        this.orthoSize = orthoSize;

        if (started)
            calcUnitSize();
    }
    @LinkFieldInInspector(name = "orthoSize", type = LinkType.GETTER, state = LinkState.BOTH)
    public float getOrthoSize() {
        return this.orthoSize;
    }

    @LinkFieldInInspector(name = "follow", type = LinkType.SETTER, state = LinkState.BOTH)
    public void setFollowing(String name) {
        this.follow = name;
        this.followGameObject = null;
    }
    @LinkFieldInInspector(name = "follow", type = LinkType.GETTER, state = LinkState.BOTH)
    public String getFollowing() {
        return this.follow;
    }

    /**
     * Gets the instance of the {@link Camera#follow} as a GameObject
     * @return the game object or null
     */
    public GameObject getFollowGameObject() {
        return this.followGameObject;
    }

    /**
     * Creates a rect that represents the camera bounds in world coordinates
     * @return the rect of the camera
     */
    public Rect getRect() {
        calcUnitSize();
        Vector2 size = new Vector2(this.unitSize);
        size.multiply(2);

        return new Rect(transform().position, size, Pivot.CENTER);
    }

    /**
     * Calculates the world position from a point on the screen
     * @param screenPosition the position in screen units (pixels)
     * @return the world position (accounting for camera position)
     */
    public Vector2 screenToWorldPosition(Vector2 screenPosition) {
        screenPosition = screenPosition.clone(); // prevent fuckery

        // calc position (argument) for screen
        screenPosition.subtract(new Vector2(ctx().width / 2f, ctx().height / 2f));

        // find scale factor from ortho size to scale down the screen position to game units (world)
        float scaleFactor = this.orthoSize / (ctx().height / 2f);

        // apply the transformation
        screenPosition.multiply(scaleFactor);

        // add camera position after scaling down the screen pos to world, since we don't want the camera position to be scaled down
        screenPosition.add(transform().position);

        return screenPosition.clone();
    }

    /**
     * Calculates the unit size
     * @see Camera#unitSize
     */
    private void calcUnitSize() {
        Vector2 screenSize = ctx().windowSize;
        float aspect = screenSize.x / screenSize.y;

        float heightUnits = orthoSize;
        float widthUnits = heightUnits * aspect;

        this.unitSize = new Vector2(widthUnits, heightUnits);
    }

    /**
     * Internal method used to apply the camera's view (matrices) to the game window
     */
    public void applyCamera() {
        calcUnitSize();

        // moved from update because sometimes the object that the camera is following would execute the movement after
        // the camera copied its position, causing the camera to lag behind by a frame (which is noticeable with big position changes)
        this.follow();

        ctx().beginCamera();

        Vector3 eye = new Vector3(
            transform().position.x,
            transform().position.y,
            cameraZ
        );

        ctx().camera(
            eye.x, eye.y, eye.z,
            eye.x, eye.y, -10,
            0, 1, 0
        );

        ctx().ortho(
            - unitSize.x,
              unitSize.x,
            - unitSize.y,
              unitSize.y,
            10, -1000f
        );
        ctx().endCamera();
    }

    /**
     * Looks for the target if the target name is set and continues doing so until it finds it (can consume some processing power)
     */
    private void findTarget() {
        // try and get the object to follow
        if (follow == null) return;
        GameObject target = gameObject.scene.getGameObjectDeep(follow);
        if (target == null) return;

        this.followGameObject = target;
    }

    /**
     * Updates the camera's position based on the follow target's
     */
    private void follow() {
        if (followGameObject == null) return;
        transform().position = followGameObject.transform.position.clone();
    }
}
