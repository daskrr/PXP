package net.daskrr.cmgt.pxp.core.component;

import net.daskrr.cmgt.pxp.core.GameProcess;
import net.daskrr.cmgt.pxp.core.Time;
import net.daskrr.cmgt.pxp.core.Vector2;
import net.daskrr.cmgt.pxp.data.Input;
import net.daskrr.cmgt.pxp.data.MouseButton;
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
    @Deprecated
    private float orthoSize = 7f;
    /**
     * Represents how many units fit on half of the size and half of the with of the window
     */
    @Deprecated
    private Vector2 unitSize;

    /**
     * Creates a camera component
     */
    public Camera() { }

    /**
     * Creates a camera component given an orthographic size
     * @param orthoSize a float which represents half of the height of the game
     */
    @Deprecated
    public Camera(float orthoSize) {
        this.orthoSize = orthoSize;
    }

    @Override
    public void start() {
//        calcUnitSize();
    }

    /**
     * Sets the orthographic size of the camera
     * @param orthoSize a float which represents half of the height of the game
     */
    @Deprecated
    public void setOrthoSize(float orthoSize) {
        this.orthoSize = orthoSize;
        calcUnitSize();
    }

    /**
     * Calculates the unit size
     * @see Camera#unitSize
     */
    @Deprecated
    private void calcUnitSize() {
        Vector2 screenSize = ctx().settings.size;
        float aspect = screenSize.x / screenSize.y;

        System.out.println(screenSize);

        float heightUnits = orthoSize;
        float widthUnits = heightUnits * aspect;

        this.unitSize = new Vector2(widthUnits, heightUnits);
    }

    /**
     * Internal method used to apply the camera's view (matrices) to the game window
     */
    public void applyCamera() {
        ctx().beginCamera();
        Vector3 eye = new Vector3(
            ctx().width/2.0f + transform().position.x,
            ctx().height/2.0f + transform().position.y,
            (ctx().height/2.0f) / GameProcess.tan(PI*30.0f / 180.0f)
        );
        ctx().camera(
            eye.x, eye.y, eye.z,
            eye.x, eye.y, 0,
            0, 1, 0
        );

        // Unfortunately couldn't implement due to the complexity of manually forcing matrix multiplication for coordinates
        // the ortho works, however the positions of objects are still in SCREEN pixels, therefore the calculations
        // required to make this work would be too much for the scope of this project.
        // The camera still has a position, so everything should be fine, the sprites (which would have had ppi) can be
        // rescaled using the scale vector and will achieve almost the same result.
        // The missing element here is keeping the same size of the game in any window size, however this can be somewhat
        // alleviated by just making/testing the game in fullscreen or just setting a fixed 1080p or 2K/4K resolutions.
//        ctx().ortho(
//            - unitSize.x,
//              unitSize.x,
//            - unitSize.y,
//              unitSize.y
//        );
        ctx().endCamera();
    }

    // TODO REMOVE THIS (TESTING)
    @Override
    public void update() {
        transform().position.x -= Time.deltaTime * 3;
        transform().position.y -= Time.deltaTime * 3;

        if (Input.getMouseButton(MouseButton.MB1))
            System.out.println("enter");
    }
}
