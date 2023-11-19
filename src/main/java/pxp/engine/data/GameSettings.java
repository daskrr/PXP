package pxp.engine.data;

import pxp.engine.data.assets.AssetManager;
import pxp.engine.data.assets.SpriteAsset;
import pxp.engine.data.ui.Cursor;
import pxp.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Structure that holds the settings of the game
 */
public class GameSettings
{
    /**
     * The size of the window
     */
    public Vector2 size = new Vector2(400, 400);
    /**
     * Whether the window is resizable (has no effect in fullscreen)<br/>
     * <b>STRONGLY ADVISE AGAINST USING THIS. IT WORKS IN MYSTERIOUS WAYS...</b>
     */
    public boolean resizable = false;
    /**
     * The desired FPS (not guaranteed, depends on hardware)
     */
    public int targetFPS = 60;
    /**
     * Whether the game should play in fullscreen
     */
    public boolean fullscreen = false;

    /**
     * The default cursor to use<br/>
     * <i>Create your own using {@link Cursor#createCustom(SpriteAsset)} (with the {@link AssetManager})
     */
    public Cursor cursor = Cursor.ARROW;

    /**
     * The background color
     */
    public Color background = new Color();
    /**
     * The background image (done the "Processing way"). Use the AssetManager to assign this.<br/>
     * <i>The image needs to match the size of the window!</i><br/>
     * <i>If this field is assigned a value other than null, it takes priority over background color.</i>
     */
    public SpriteAsset backgroundImage = null;
    /**
     * The texture filter used when scaling images
     */
    public TextureFilter textureFilter = TextureFilter.POINT;

    /**
     * The sorting layers, in the desired order of rendering<br/>
     * Should contain a "Default" layer (placed anywhere). If one is not present, one will be inserted at the 0th index.
     */
    public String[] sortingLayers = new String[] { "Default" };

    /**
     * The layers of the game, assignable to game objects<br/>
     * Used for collisions and physics<br/>
     * Should contain a "Default" layer (placed anywhere). If one is not present, one will be inserted at the 0th index.<br/>
     * <b>IMPORTANT</b> The amount of layers cannot exceed 32! (due to usage of bitmasks [{@link LayerMask}])<br/>
     * There should not be duplicate layers, however if they are wrongfully created, the first is picked.
     */
    public String[] layers = new String[] { "Default" };

    /**
     * A miniature collision matrix<br/>
     * The specified {@link GameSettings#layers}' names have their collisions disabled
     */
    public List<Pair<String, String>> ignoreCollisionLayers = new ArrayList<>();

    /**
     * Forces the game to draw Gizmos, no matter component preference
     */
    public boolean forceDrawGizmos = false;

    /**
     * Creates a new GameSettings structure, it is preferred to use an initializer, i.e: <br/>
     * <code>new GameSettings() {{ size = ...; targetFPS = ...; }}</code>
     */
    public GameSettings() { }
}
