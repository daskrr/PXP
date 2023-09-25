package net.daskrr.cmgt.pxp.data.assets;

import net.daskrr.cmgt.pxp.core.GameProcess;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages assets like images (sprites) or sounds
 */
public class AssetManager
{
    private static AssetManager instance = null;
    protected static AssetManager getInstance() {
        if (instance == null)
            instance = new AssetManager();

        return instance;
    }

    private Map<String, Asset> assets = new HashMap<String, Asset>();

    public static void createImage(String name, String path) {
        getInstance().assets.put(name, new ImageAsset(path));
    }

    public static void createSound(String name, String path) {} // TODO

    public static <T extends Asset> T get(String name, Class<T> type) {
        Asset asset = getInstance().assets.get(name);
        if (asset == null) return null;
        return (T) asset;
    }

    public static void load() {
        for (Asset asset : getInstance().assets.values())
            asset.load();
    }
}
