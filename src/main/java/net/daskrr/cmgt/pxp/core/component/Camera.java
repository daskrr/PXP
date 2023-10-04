package net.daskrr.cmgt.pxp.core.component;

import net.daskrr.cmgt.pxp.core.GameObject;
import net.daskrr.cmgt.pxp.data.Vector2;
import net.daskrr.cmgt.pxp.data.Vector3;

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
    public String followGameObject = null;

    /**
     * The game object instance to follow
     */
    private GameObject followTarget = null;

    /**
     * Creates a camera component
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
        this.follow();
    }

    /**
     * Sets the orthographic size of the camera
     * @param orthoSize a float which represents half of the height of the game
     */
    public void setOrthoSize(float orthoSize) {
        this.orthoSize = orthoSize;
        calcUnitSize();
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

        ctx().beginCamera();
//        Vector3 eye = new Vector3(
//            ctx().width/2.0f + transform().position.x,
//            ctx().height/2.0f + transform().position.y,
//            (ctx().height/2.0f) / GameProcess.tan(PI*30.0f / 180.0f)
//        );
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

    private void findTarget() {
        // try and get the object to follow
        if (followGameObject == null) return;
        GameObject target = gameObject.scene.getGameObject(followGameObject);
        if (target == null) return;

        this.followTarget = target;
    }

    private void follow() {
        if (followTarget == null) return;
        transform().position = followTarget.transform.position.clone();
    }
}
