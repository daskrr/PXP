import net.daskrr.cmgt.pxp.core.GameObject;
import net.daskrr.cmgt.pxp.core.Transform;
import net.daskrr.cmgt.pxp.data.Vector2;
import net.daskrr.cmgt.pxp.core.component.Animator;
import net.daskrr.cmgt.pxp.core.component.Component;
import net.daskrr.cmgt.pxp.core.component.SoundEmitter;
import net.daskrr.cmgt.pxp.core.component.SpriteRenderer;
import net.daskrr.cmgt.pxp.data.Input;
import net.daskrr.cmgt.pxp.data.Key;
import net.daskrr.cmgt.pxp.data.assets.AssetManager;
import net.daskrr.cmgt.pxp.data.assets.SpriteAsset;

public class ComponentTest extends Component
{
    private Animator animator;
    private SoundEmitter soundEmitter;

    @Override
    public void start() {
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

        if (Input.getKey(Key.U)) {
            Instantiate(new GameObject("testChild", new Component[] {
                new SpriteRenderer(AssetManager.get("test", SpriteAsset.class)) {{
                    sortingLayer = "Default";
                    orderInLayer = 0;
                }}
            }) {{
                transform = new Transform(new Vector2(2, 2), new Vector2(.5f, .5f));
            }}, "test2");
        }
    }
}
