import net.daskrr.cmgt.pxp.core.*;
import net.daskrr.cmgt.pxp.core.component.*;
import net.daskrr.cmgt.pxp.data.*;
import net.daskrr.cmgt.pxp.data.assets.AssetManager;
import net.daskrr.cmgt.pxp.data.assets.SoundAsset;
import net.daskrr.cmgt.pxp.data.assets.SpriteAsset;

import java.util.ArrayList;

public class GameTest extends Game
{
    @Override
    public GameSettings startup() {
        AssetManager.createSprite("test", "test_img.png", 16);
        AssetManager.createSpriteSheet("testSheet", "player.png", 16, 6, 15);
        AssetManager.createSound("testSound", "test_sound.wav");
        AssetManager.createSound("testSoundMono", "test_mono.wav");
//        AssetManager.createVideo("testVideo", "test_video.mov");

        return new GameSettings() {{
            size = new Vector2(800,800);
//            fullscreen = true;
//            resizable = true;
            background = new Color(0,0,0,255);
            backgroundImage = AssetManager.get("test", SpriteAsset.class);
            sortingLayers = new ArrayList<>() {{
                add("Default");
                add("Default2");
            }};
        }};
    }

    @Override
    public Scene[] buildScenes() {
        return new Scene[] {
            new Scene(new GameObjectSupplier[] {
                // camera
                () -> new GameObject("cam", new Component[] {
                        new Camera()
                }) {{
                    transform = new Transform(new Vector2(0,0));
                }},

                // testing
                () -> new GameObject("test", new Component[] {
                    new Canvas(new DrawEnvironment() {
                        @Override
                        public void accept(GameProcess p) {
                            p.rectMode(CORNERS);
                            p.rect(0,0,100,100);
                        }
                    }) {{
                        sortingLayer = "Default";
                        orderInLayer = 1;
                    }},
                    new SoundEmitter(AssetManager.get("testSoundMono", SoundAsset.class)) {{
                        isSpatial = true;
                        setAutoPlay(true);
                        setLoop(true);
                    }}
                }) {{
                    transform = new Transform(new Vector2(0,0), new Vector3(0,0,0), new Vector2(4,5));
                }},
                () -> new GameObject("test2", new Component[] {
                    new SpriteRenderer(AssetManager.getSpriteFromSheet("testSheet", 65)) {{
                        sortingLayer = "Default";
                        orderInLayer = 0;
                    }}
                }) {{
                    transform = new Transform(new Vector2(0,0), new Vector3(0,0,0), new Vector2(1,1));
                }},
                () -> new GameObject("testAnim", new Component[] {
                    new SpriteRenderer() {{
                        sortingLayer = "Default2";
                        orderInLayer = 0;
                    }},
                    new Animation("full", AssetManager.get("testSheet", SpriteAsset.class), 0, 76, 9f)
                }) {{
                    transform = new Transform(new Vector2(3,3), new Vector3(0,0,0), new Vector2(1,1));
                }}
            })
        };
    }

    public static void main(String[] args) {
        new GameTest();
    }
}
