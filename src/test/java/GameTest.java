import net.daskrr.cmgt.pxp.core.*;
import net.daskrr.cmgt.pxp.core.component.Component;
import net.daskrr.cmgt.pxp.core.component.SpriteRenderer;
import net.daskrr.cmgt.pxp.data.Color;
import net.daskrr.cmgt.pxp.data.GameSettings;
import net.daskrr.cmgt.pxp.data.Vector3;
import net.daskrr.cmgt.pxp.data.assets.AssetManager;
import net.daskrr.cmgt.pxp.data.assets.ImageAsset;

import java.util.ArrayList;

public class GameTest extends Game
{
    @Override
    public GameSettings setup() {
        AssetManager.createImage("test", "test_img.png");

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

        // TODO to watch out, game objects & their components need to be cloned when used, since when destroying a
        // scene/object & its components they maintain state
        // ACTUALLY, better idea just use what I used for UGXP, structures with data for GameObjects and lambdas for the components

        return new Scene[] {
            new Scene(new GameObject[] {
                new GameObject("test", new Component[] {
                    new SpriteRenderer(AssetManager.get("test", ImageAsset.class)) {{
                        sortingLayer = "Default";
                        orderInLayer = 1;
                    }}
                }) {{
                    transform = new Transform(new Vector2(50,50));
                }},
                new GameObject("test2", new Component[] {
                    new SpriteRenderer(AssetManager.get("test", ImageAsset.class)) {{
                        sortingLayer = "Default";
                        orderInLayer = 0;
                    }}
                }) {{
                    transform = new Transform(new Vector2(100,50), new Vector3(0,0,50), new Vector2(3,3));
                }}
            })
        };
    }

    public static void main(String[] args) {
        new GameTest();
    }
}
