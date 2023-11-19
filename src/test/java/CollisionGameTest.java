import java.util.ArrayList;

import pxp.engine.core.Game;
import pxp.engine.core.GameObject;
import pxp.engine.core.Scene;
import pxp.engine.core.Transform;
import pxp.engine.core.component.Animation;
import pxp.engine.core.component.Animator;
import pxp.engine.core.component.BoxCollider;
import pxp.engine.core.component.Camera;
import pxp.engine.core.component.CircleCollider;
import pxp.engine.core.component.Component;
import pxp.engine.core.component.SoundEmitter;
import pxp.engine.core.component.SpriteRenderer;
import pxp.engine.data.Color;
import pxp.engine.data.GameObjectSupplier;
import pxp.engine.data.GameSettings;
import pxp.engine.data.LayerMask;
import pxp.engine.data.Vector2;
import pxp.engine.data.Vector3;
import pxp.engine.data.assets.AssetManager;
import pxp.engine.data.assets.SoundAsset;
import pxp.engine.data.assets.SpriteAsset;
import pxp.util.Pair;

// RECOVERED FILE, don't mind the messy coding "style" or you'll hurt the decompiler's feelings.

public class CollisionGameTest extends Game {
    public GameSettings startup() {
        AssetManager.createSprite("test", "test_img.png", 16);
        AssetManager.createSpriteSheet("testSheet", "player.png", 16, 6, 15);
        AssetManager.createSound("testSoundMono", "test_mono.wav");
        return new GameSettings() {{
            this.size = new Vector2(1280.0F, 720.0F);
            this.targetFPS = 200;
            this.background = new Color(0, 0, 0);
            this.forceDrawGizmos = true;
            this.sortingLayers = new String[]{"Default", "Default2"};
            this.layers = new String[]{"Default", "layer1", "layer2"};
            this.ignoreCollisionLayers = new ArrayList<>() {{
                this.add(new Pair<>("layer1", "layer2"));
            }};
        }};
    }

    public Scene[] buildScenes() {
        return new Scene[]{new Scene(new GameObjectSupplier[]{() -> new GameObject("cam", new Component[]{new Camera(8.0F) {
            {
                this.setFollowing("player");
            }
        }}) {
            {
                this.transform = new Transform(new Vector2(0.0F, 0.0F));
            }
        }, () -> new GameObject("static", new Component[]{new SpriteRenderer((SpriteAsset) AssetManager.get("test", SpriteAsset.class)) {
            {
                this.setSortingLayer("Default");
                this.flipX = true;
                this.flipY = true;
            }
        }}) {
            {
                this.transform = new Transform(new Vector2(0.0F, 0.0F), new Vector3(0.0F, 0.0F, 0.0F), new Vector2(1.0F, 1.0F));
            }
        }, () -> new GameObject("player", new Component[]{new SpriteRenderer() {
            {
                this.color = new Color(255, 255, 255);
                this.setSortingLayer("Default2");
            }
        }, new Animation("idling", (SpriteAsset) AssetManager.get("testSheet", SpriteAsset.class), 0, 5, 2.0F), new Animation("walking", (SpriteAsset) AssetManager.get("testSheet", SpriteAsset.class), 6, 13, 1.0F), new Animation("climbing", (SpriteAsset) AssetManager.get("testSheet", SpriteAsset.class), 58, 62, 0.7F), new Animation("full", (SpriteAsset) AssetManager.get("testSheet", SpriteAsset.class), 5.0F), new Animator("idling"), new ComponentTest(), new SoundEmitter((SoundAsset) AssetManager.get("testSoundMono", SoundAsset.class)) {
            {
                this.isSpatial = true;
                this.innerRadius = 10.0F;
                this.setLoop(true);
            }
        }, new CircleCollider(new Vector2(0.0F, 0.0F), 2f) {
            {
                this.layer = LayerMask.nameToId("layer1");
            }
        }}) {
            {
                this.transform = new Transform(new Vector2(3.0F, 3.0F), new Vector3(0.0F, 0.0F, 0.0F), new Vector2(1.0F, 1.0F));
            }
        }, () -> new GameObject("test2", new Component[]{new SpriteRenderer((SpriteAsset) AssetManager.get("test", SpriteAsset.class)) {
            {
                this.setSortingLayer("Default");
                this.flipX = true;
                this.flipY = true;
            }
        }, new CircleCollider(new Vector2(0.0F, 0.0F), 2.0F) {
            {
                this.layer = LayerMask.nameToId("layer1");
            }
        }}) {
            {
                this.transform = new Transform(new Vector2(15.5F, 5.0F), new Vector3(0.0F, 0.0F, 0.0F), new Vector2(1.0F, 1.0F));
            }
        }, () -> new GameObject("test3", new Component[]{new SpriteRenderer((SpriteAsset) AssetManager.get("test", SpriteAsset.class)) {
            {
                this.setSortingLayer("Default");
                this.flipX = true;
                this.flipY = true;
            }
        }, new BoxCollider(new Vector2(0.0F, 0.0F), new Vector2(2.0F, 4.0F)) {
            {
//                this.trigger = true;
            }
        }}) {
            {
                this.transform = new Transform(new Vector2(10.5F, 4.0F), new Vector3(0.0F, 0.0F, 0.0F), new Vector2(1.0F, 1.0F));
            }
        }}), new Scene(new GameObjectSupplier[]{() -> new GameObject("cam", new Component[]{new Camera(8.0F) {
            {
                this.setFollowing("player");
            }
        }}) {
            {
                this.transform = new Transform(new Vector2(0.0F, 0.0F));
            }
        }})};
    }

    public static void main(String[] args) {
        new CollisionGameTest();
    }
}
