import net.daskrr.cmgt.pxp.core.*;
import net.daskrr.cmgt.pxp.core.component.Animation;
import net.daskrr.cmgt.pxp.core.component.Camera;
import net.daskrr.cmgt.pxp.core.component.Canvas;
import net.daskrr.cmgt.pxp.core.component.SpriteRenderer;
import net.daskrr.cmgt.pxp.data.*;
import net.daskrr.cmgt.pxp.data.assets.AssetManager;
import net.daskrr.cmgt.pxp.data.assets.SpriteAsset;

import java.util.ArrayList;

public class GameTest extends Game
{
    @Override
    public GameSettings setup() {
        AssetManager.createSprite("test", "test_img.png");
        AssetManager.createSpriteSheet("testSheet", "player.png", 6, 15);

        return new GameSettings() {{
            size = new Vector2(600,600);
//            fullscreen = true;
            background = new Color(0,0,0,255);
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
                () -> new GameObject("test", new ComponentSupplier[] {
                    () -> new Canvas(new DrawEnvironment() {
                        @Override
                        public void accept(GameProcess p) {
                            p.rectMode(CORNERS);
                            p.rect(0,0,100,100);
                        }
                    }) {{
                        sortingLayer = "Default";
                        orderInLayer = 1;
                    }}
                }) {{
                    transform = new Transform(new Vector2(50,50), new Vector3(0,0,0), new Vector2(4,5));
                }},
                () -> new GameObject("test2", new ComponentSupplier[] {
                    () -> new SpriteRenderer(AssetManager.getSpriteFromSheet("testSheet", 65)) {{
                        sortingLayer = "Default";
                        orderInLayer = 0;
                    }}
                }) {{
                    transform = new Transform(new Vector2(0,0), new Vector3(0,0,0), new Vector2(1,1));
                }},
                () -> new GameObject("testAnim", new ComponentSupplier[] {
                    Camera::new
                }) {{
                    transform = new Transform(new Vector2(0,0));
                }},
                () -> new GameObject("testAnim", new ComponentSupplier[] {
                    () -> new SpriteRenderer() {{
                        sortingLayer = "Default2";
                        orderInLayer = 0;
                    }},
                    () -> new Animation("full", AssetManager.get("testSheet", SpriteAsset.class), 0, 76, 9f)
                }) {{
                    transform = new Transform(new Vector2(300,300), new Vector3(0,0,0), new Vector2(1,1));
                }}
            })
        };
    }

    public static void main(String[] args) {
        new GameTest();
    }
}
