package net.daskrr.cmgt.pxp.core.component;

import net.daskrr.cmgt.pxp.core.Vector2;
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

    // TODO REMOVE THIS (TESTING)
    @Override
    public void update() {
//        this.orthoSize += Time.deltaTime * 25f;
//        this.calcUnitSize();

//        transform().position.x -= Time.deltaTime * 3;
//        transform().position.y -= Time.deltaTime * 3;
//
//        if (Input.getMouseButton(MouseButton.MB1))
//            System.out.println("enter");
    }
}
