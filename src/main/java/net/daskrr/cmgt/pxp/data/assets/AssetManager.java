package net.daskrr.cmgt.pxp.data.assets;

import java.util.HashMap;
import java.util.Map;

/**
 * The AssetManager manages assets like images (sprites, sprite sheets) or sounds<br/>
 * The creation of assets must occur at setup in the extended Game class. Once assets are created they cannot be unloaded
 * from memory and the creation of new assets is impossible
 */
public class AssetManager
{
    /**
     * The instance of the AssetManager (acts only as a singleton)
     */
    private static AssetManager instance = null;

    /**
     * Gets the AssetManager singleton instance or creates one if it doesn't exist
     * @return the AssetManager instance
     */
    protected static AssetManager getInstance() {
        if (instance == null)
            instance = new AssetManager();

        return instance;
    }

    /**
     * The map of assets by their unique names
     */
    private final Map<String, Asset> assets = new HashMap<String, Asset>();

    /**
     * Whether the assets were loaded into memory
     */
    private boolean loaded = false;

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
        getInstance().assets.put(name, new SpriteAsset(path) {{ totalRows = rows; totalColumns = columns; isSpriteSheet = true; }});
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
    public static void createSpriteSheet(String name, String path, int pixelsPerUnit, int rows, int columns) {
        getInstance().assets.put(name, new SpriteAsset(path, pixelsPerUnit) {{ totalRows = rows; totalColumns = columns; isSpriteSheet = true; }});
    }

    public static void createSound(String name, String path) {} // TODO

    /**
     * Retrieves an asset
     * @param name the unique name of the asset
     * @param type the type of the asset (for convenience)
     * @return the asset or null
     */
    public static <T extends Asset> T get(String name, Class<T> type) {
        Asset asset = getInstance().assets.get(name);
        if (asset == null) return null;
        return (T) asset;
    }

    /**
     * Retrieves a sprite from a sprite sheet given the index of the sprite in the sheet
     * @param name the unique name of the sprite sheet asset (SpriteAsset)
     * @param index the index of the sprite in the sheet (going rows -> columns)
     * @return a sub sprite SpriteAsset
     */
    public static SpriteAsset getSpriteFromSheet(String name, int index) {
        SpriteAsset asset = (SpriteAsset) getInstance().assets.get(name);
        if (asset == null) return null;

        return asset.getSubSprite(index);
    }

    /**
     * [Internal] Loads the assets into memory (can only happen once)
     */
    public static void load() {
        if (AssetManager.getInstance().loaded) return;

        AssetManager.getInstance().loaded = true;
        for (Asset asset : getInstance().assets.values())
            asset.load();
    }
}
