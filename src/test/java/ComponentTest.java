import pxp.engine.core.GameObject;
import pxp.engine.core.Transform;
import pxp.engine.core.component.*;
import pxp.engine.core.component.pointer.PointerHandler;
import pxp.engine.core.component.pointer.PointerHandlerDrag;
import pxp.engine.core.component.pointer.PointerHandlerMouse;
import pxp.engine.data.Pivot;
import pxp.engine.data.Vector2;
import pxp.engine.data.Input;
import pxp.engine.data.Key;
import pxp.engine.data.assets.AssetManager;
import pxp.engine.data.assets.SpriteAsset;
import processing.event.MouseEvent;
import pxp.logging.Debug;

public class ComponentTest extends Component implements PointerHandlerMouse, PointerHandlerDrag
{
    private Animator animator;
    private SoundEmitter soundEmitter;

    @Override
    public void start() {
        this.gameObject.setPersistent(true);

        this.animator = getComponentOfType(Animator.class);
        this.soundEmitter = getComponentOfType(SoundEmitter.class);

        this.animator.getAnimation("full").reverse();
    }

    @Override
    public void update() {
        if (Input.getKey(Key.D)) {
            // move to the right
            transform().position.x += .13f;
            animator.play("walking");
            ((SpriteRenderer) gameObject.renderer).flipX = false;
            this.soundEmitter.play();
        }
        else if (Input.getKey(Key.A)) {
            // move to the left
            transform().position.x -= .13f;
            animator.play("walking");
            ((SpriteRenderer) gameObject.renderer).flipX = true;
            this.soundEmitter.play();
        }

        if (Input.getKey(Key.W)) {
            // move to the right
            transform().position.y -= .13f;
            animator.play("climbing");
            this.soundEmitter.play();
        }
        else if (Input.getKey(Key.S)) {
            // move to the left
            transform().position.y += .13f;
            animator.play("climbing");
            this.soundEmitter.play();
        }

        if (!Input.getKey(Key.W) && !Input.getKey(Key.A) && !Input.getKey(Key.S) && !Input.getKey(Key.D)) {
            animator.play("idling");
            this.soundEmitter.pause();
        }

        if (Input.getKeyOnce(Key.U)) {
            GameObject parent = gameObject.scene.getGameObjectDeep("test2");
            if (parent != null)
                Instantiate(new GameObject("testChild", new Component[] {
                    new SpriteRenderer(AssetManager.get("test", SpriteAsset.class)) {{
                        setSortingLayer("Default");
                    }},
                    new Component(){
                       @Override
                       public void update() {
                           System.out.println(gameObject.isDestroyed);
                       }
                    }
                }) {{
                    transform = new Transform(new Vector2(2, 2), new Vector2(.5f, .5f));
                }}, parent);
        }

        if (Input.getKeyOnce(Key.K)) {
            ctx().setScene(ctx().getCurrentScene().index == 0 ? 1 : 0);
//            ctx().setScene(1);
        }
    }

    @Override
    public boolean checkOverlap(Vector2 mousePos) {
        Vector2 worldPos = this.gameObject.scene.getCamera().screenToWorldPosition(mousePos);
        boolean overlap = rectTransform().checkOverlap(worldPos, Pivot.CENTER);

//        System.out.println(worldPos);
//        if (overlap)
//            Debug.log("overlap");

        return overlap;
    }

    @Override
    public void setHovering(boolean hovering) {

    }

    @Override
    public boolean isHovering() {
        return false;
    }

//    @Override
//    public void mouseHover(MouseEvent event) {
//
//    }
//
//    @Override
//    public void mouseHoverEnter(MouseEvent event) {
//
//    }
//
//    @Override
//    public void mouseHoverExit(MouseEvent event) {
//
//    }

    @Override
    public void mouseClick(MouseEvent event) {

    }

    @Override
    public void mouseScroll(MouseEvent event) {

    }

    @Override
    public void mouseDown(MouseEvent event) {

    }

    @Override
    public void mouseUp(MouseEvent event) {

    }

    private boolean dragging = false;

    @Override
    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    @Override
    public boolean isDragging() {
        return dragging;
    }

    @Override
    public void mouseDrag(MouseEvent event) {
        Debug.log("dragging");
    }

    @Override
    public void mouseDragStart(MouseEvent event) {
        Debug.log("started dragging");
    }

    @Override
    public void mouseDragStop(MouseEvent event) {
        Debug.log("stopped dragging");
    }
}
