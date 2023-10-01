package net.daskrr.cmgt.pxp.data;

import net.daskrr.cmgt.pxp.core.Vector2;

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
     * The background color
     */
    public Color background = new Color();
    /**
     * The texture filter used when scaling images
     */
    public TextureFilter textureFilter = TextureFilter.POINT;
    /**
     * The sorting layers, in the desired order of rendering
     */
    public List<String> sortingLayers = new ArrayList<>() {{ add("Default"); }};

    /**
     * Creates a new GameSettings structure, it is preferred to use an initializer, i.e: <br/>
     * <code>new GameSettings() {{ size = ...; targetFPS = ...; }}</code>
     */
    public GameSettings() { }
}
