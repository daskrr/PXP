import net.daskrr.cmgt.pxp.core.Game;
import net.daskrr.cmgt.pxp.core.Scene;
import net.daskrr.cmgt.pxp.core.Vector2;
import net.daskrr.cmgt.pxp.data.GameSettings;

public class GameTest extends Game
{
    @Override
    public GameSettings setup() {
        return new GameSettings() {{
            size = new Vector2(0,0);
        }};
    }

    @Override
    public Scene[] buildScenes() {
        return new Scene[] {

        };
    }

    public static void main(String[] args) {
        new GameTest();
    }
}
