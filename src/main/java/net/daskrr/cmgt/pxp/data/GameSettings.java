package net.daskrr.cmgt.pxp.data;

import net.daskrr.cmgt.pxp.core.Vector2;

import java.util.ArrayList;
import java.util.List;

public class GameSettings
{
    public Vector2 size = new Vector2(400, 400);
    public int targetFPS = 60;
    public boolean fullscreen = false;
    public Color background = new Color();
    public TextureFilter textureFilter = TextureFilter.POINT;
    public List<String> sortingLayers = new ArrayList<>() {{ add("Default"); }};

    public GameSettings() { }
}
