package net.daskrr.cmgt.pxp.data.assets;

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

    private final Map<String, Asset> assets = new HashMap<String, Asset>();

    /**
     * Creates a sprite asset which will be loaded once when the game starts and kept in memory until the process is terminated
     * @param name the unique name of the asset
     * @param path the path to the asset (in the resources folder)
     */
    public static void createSprite(String name, String path) {
        getInstance().assets.put(name, new SpriteAsset(path));
    }

    /**
     * Creates a sprite asset which will be loaded once when the game starts and kept in memory until the process is terminated
     * @param name the unique name of the asset
     * @param path the path to the asset (in the resources folder)
     * @param pixelsPerUnit the pixels to game units ratio (i.e.: 16 ppi is 16x16 image pixels from 0,0 to 1,1)
     */
    @Deprecated
    public static void createSprite(String name, String path, int pixelsPerUnit) {
        getInstance().assets.put(name, new SpriteAsset(path, pixelsPerUnit));
    }

    /**
     * Creates a sprite asset for every sprite in the sheet which will be loaded once when the game starts and kept in
     * memory until the process is terminated. The sprites can be accessed using the provided name + "_index" where index
     * represents the position in a uni-dimensional array of sprites (created from left to right from all rows).
     * The main image can be accessed using the name without a suffix.
     * @param name the unique name of the asset
     * @param path the path to the asset (in the resources folder)
     * @param rows the number of rows (horizontal)
     * @param columns the number of columns (vertical)
     */
    public static void createSpriteSheet(String name, String path, int rows, int columns) {
        getInstance().assets.put(name, new SpriteAsset(path) {{ totalRows = rows; totalColumns = columns; }});
    }
    /**
     * Creates a sprite asset for every sprite in the sheet which will be loaded once when the game starts and kept in
     * memory until the process is terminated. The sprites can be accessed using the provided name + "_index" where index
     * represents the position in a uni-dimensional array of sprites (created from left to right from all rows).
     * The main image can be accessed using the name without a suffix.
     * @param name the unique name of the asset
     * @param path the path to the asset (in the resources folder)
     * @param rows the number of rows (horizontal)
     * @param columns the number of columns (vertical)
     * @param pixelsPerUnit the pixels to game units ratio (i.e.: 16 ppi is 16x16 image pixels from 0,0 to 1,1)
     */
    @Deprecated
    public static void createSpriteSheet(String name, String path, int pixelsPerUnit, int rows, int columns) {
        getInstance().assets.put(name, new SpriteAsset(path, pixelsPerUnit) {{ totalRows = rows; totalColumns = columns; }});
    }

    public static void createSound(String name, String path) {} // TODO

    public static <T extends Asset> T get(String name, Class<T> type) {
        Asset asset = getInstance().assets.get(name);
        if (asset == null) return null;
        return (T) asset;
    }

    public static SpriteAsset getSpriteFromSheet(String name, int index) {
        SpriteAsset asset = (SpriteAsset) getInstance().assets.get(name);
        if (asset == null) return null;

        return asset.getSubSprite(index);
    }

    public static void load() {
        for (Asset asset : getInstance().assets.values())
            asset.load();
    }
}
