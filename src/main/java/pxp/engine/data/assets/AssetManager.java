package pxp.engine.data.assets;

import pxp.engine.core.GameProcess;
import processing.core.PApplet;

import java.util.HashMap;
import java.util.Map;

/**
 * The AssetManager manages assets like images (sprites, sprite sheets) or sounds<br/>
 * The creation of assets must occur at setup in the extended Game class. Once assets are created they cannot be unloaded
 * from memory and the creation of new assets is impossible
 */
public class AssetManager
{
    private static final String DEFAULT_FONT = "_defaultFont";

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

    private AssetManager() {
        this._createFont(DEFAULT_FONT, new FontAsset("Arial", "Arial Bold", "Arial Italic", "Arial Bold Italic"));
    }

    /**
     * The map of assets by their unique names
     */
    private Map<String, Asset> assets = new HashMap<String, Asset>();

    /**
     * Whether the assets were loaded into memory
     */
    private boolean loaded = false;

    /**
     * Creates a sprite asset which will be loaded once when the game starts and kept in memory until the process is terminated
     * @param name the unique name of the asset
     * @param path the path to the asset (in the {@literal <resources>}/data folder)
     */
    public static void createSprite(String name, String path) {
        getInstance()._createSprite(name, path);
    }

    /**
     * Creates a sprite asset which will be loaded once when the game starts and kept in memory until the process is terminated
     * @param name the unique name of the asset
     * @param path the path to the asset (in the {@literal <resources>}/data folder)
     * @param pixelsPerUnit the pixels to game units ratio (i.e.: 16 ppi is 16x16 image pixels from 0,0 to 1,1)
     */
    public static void createSprite(String name, String path, int pixelsPerUnit) {
        getInstance()._createSprite(name, path, pixelsPerUnit);
    }

    /**
     * Creates a sprite asset for every sprite in the sheet which will be loaded once when the game starts and kept in
     * memory until the process is terminated. The sprites can be accessed using the provided name + "_index" where index
     * represents the position in a uni-dimensional array of sprites (created from left to right from all rows).
     * The main image can be accessed using the name without a suffix.
     * @param name the unique name of the asset
     * @param path the path to the asset (in the {@literal <resources>}/data folder)
     * @param rows the number of rows (horizontal)
     * @param columns the number of columns (vertical)
     */
    public static void createSpriteSheet(String name, String path, int rows, int columns) {
        getInstance()._createSpriteSheet(name, path, rows, columns);
    }
    /**
     * Creates a sprite asset for every sprite in the sheet which will be loaded once when the game starts and kept in
     * memory until the process is terminated. The sprites can be accessed using the provided name + "_index" where index
     * represents the position in a uni-dimensional array of sprites (created from left to right from all rows).
     * The main image can be accessed using the name without a suffix.
     * @param name the unique name of the asset
     * @param path the path to the asset (in the {@literal <resources>}/data folder)
     * @param rows the number of rows (horizontal)
     * @param columns the number of columns (vertical)
     * @param pixelsPerUnit the pixels to game units ratio (i.e.: 16 ppi is 16x16 image pixels from 0,0 to 1,1)
     */
    public static void createSpriteSheet(String name, String path, int pixelsPerUnit, int rows, int columns) {
        getInstance()._createSpriteSheet(name, path, pixelsPerUnit, rows, columns);
    }

    /**
     * Creates a sound asset
     * @param name the unique name of the asset
     * @param path the path to the asset (in the {@literal <resources>}/data folder)
     */
    public static void createSound(String name, String path) {
        getInstance()._createSound(name, path);
    }

    /**
     * Creates a sound asset given a volume
     * @param name the unique name of the asset
     * @param path the path to the asset (in the {@literal <resources>}/data folder)
     * @param volume the volume of the sound asset
     */
    public static void createSound(String name, String path, float volume) {
        getInstance()._createSound(name, path, volume);
    }

    /**
     * Creates a video asset which will be loaded once when the game starts and kept in memory until the process is terminated
     * @param name the unique name of the asset
     * @param path the path to the asset (in the {@literal <resources>}/data folder)
     * @deprecated VIDEO PLAYBACK DOESN'T WORK (check GameProcess' constructor for more information)
     */
    @Deprecated
    public static void createVideo(String name, String path) {
        getInstance()._createVideo(name, path);
    }

    /**
     * Creates a video asset which will be loaded once when the game starts and kept in memory until the process is terminated
     * @param name the unique name of the asset
     * @param path the path to the asset (in the {@literal <resources>}/data folder)
     * @param pixelsPerUnit the pixels to game units ratio (i.e.: 16 ppi is 16x16 image pixels from 0,0 to 1,1)
     * @deprecated VIDEO PLAYBACK DOESN'T WORK (check GameProcess' constructor for more information)
     */
    @Deprecated
    public static void createVideo(String name, String path, int pixelsPerUnit) {
        getInstance()._createVideo(name, path);
    }

    /**
     * Creates a custom asset (provided the asset implementation)
     * @param name the unique name of the asset
     * @param asset the font asset (needs to be created separately
     * @see FontAsset
     */
    public static void createFont(String name, FontAsset asset) {
        getInstance()._createFont(name, asset);
    }

    /**
     * Creates a custom asset (provided the asset implementation)
     * @param name the unique name of the asset
     * @param asset the asset instance
     * @see Asset
     */
    public static <T extends Asset> void createCustom(String name, T asset) {
        getInstance()._createCustom(name, asset);
    }

    /**
     * Retrieves an asset
     * @param name the unique name of the asset
     * @param type the type of the asset (for convenience)
     * @return the asset or null
     */
    public static <T extends Asset> T get(String name, Class<T> type) {
        return getInstance()._get(name, type);
    }

    /**
     * Retrieves a sprite from a sprite sheet given the index of the sprite in the sheet
     * @param name the unique name of the sprite sheet asset (SpriteAsset)
     * @param index the index of the sprite in the sheet (going rows -> columns)
     * @return a sub sprite SpriteAsset
     */
    public static SpriteAsset getSpriteFromSheet(String name, int index) {
        return getInstance()._getSpriteFromSheet(name, index);
    }

    /**
     * [Internal] Loads the assets into memory (can only happen once per asset manager)
     */
    public static void load() {
        AssetManager.getInstance().load(GameProcess.getInstance());
    }
    /**
     * [Internal] Loads the assets into memory given a specific processing PApplet (can only happen once per asset manager)
     */
    public void load(PApplet processing) {
        if (this.loaded) return;

        this.loaded = true;
        for (Asset asset : this.assets.values())
            asset.load(processing);
    }


    // =========== INSTANCE VARIANTS ===========

    /**
     * Creates a sprite asset which will be loaded once when the game starts and kept in memory until the process is terminated
     * @param name the unique name of the asset
     * @param path the path to the asset (in the {@literal <resources>}/data folder)
     */
    public void _createSprite(String name, String path) {
        checkName(name);
        assets.put(name, new SpriteAsset(path));
    }

    /**
     * Creates a sprite asset which will be loaded once when the game starts and kept in memory until the process is terminated
     * @param name the unique name of the asset
     * @param path the path to the asset (in the {@literal <resources>}/data folder)
     * @param pixelsPerUnit the pixels to game units ratio (i.e.: 16 ppi is 16x16 image pixels from 0,0 to 1,1)
     */
    public void _createSprite(String name, String path, int pixelsPerUnit) {
        checkName(name);
        assets.put(name, new SpriteAsset(path, pixelsPerUnit));
    }

    /**
     * Creates a sprite asset for every sprite in the sheet which will be loaded once when the game starts and kept in
     * memory until the process is terminated. The sprites can be accessed using the provided name + "_index" where index
     * represents the position in a uni-dimensional array of sprites (created from left to right from all rows).
     * The main image can be accessed using the name without a suffix.
     * @param name the unique name of the asset
     * @param path the path to the asset (in the {@literal <resources>}/data folder)
     * @param rows the number of rows (horizontal)
     * @param columns the number of columns (vertical)
     */
    public void _createSpriteSheet(String name, String path, int rows, int columns) {
        checkName(name);
        assets.put(name, new SpriteAsset(path) {{ totalRows = rows; totalColumns = columns; isSpriteSheet = true; }});
    }
    /**
     * Creates a sprite asset for every sprite in the sheet which will be loaded once when the game starts and kept in
     * memory until the process is terminated. The sprites can be accessed using the provided name + "_index" where index
     * represents the position in a uni-dimensional array of sprites (created from left to right from all rows).
     * The main image can be accessed using the name without a suffix.
     * @param name the unique name of the asset
     * @param path the path to the asset (in the {@literal <resources>}/data folder)
     * @param rows the number of rows (horizontal)
     * @param columns the number of columns (vertical)
     * @param pixelsPerUnit the pixels to game units ratio (i.e.: 16 ppi is 16x16 image pixels from 0,0 to 1,1)
     */
    public void _createSpriteSheet(String name, String path, int pixelsPerUnit, int rows, int columns) {
        checkName(name);
        assets.put(name, new SpriteAsset(path, pixelsPerUnit) {{ totalRows = rows; totalColumns = columns; isSpriteSheet = true; }});
    }

    /**
     * Creates a sound asset
     * @param name the unique name of the asset
     * @param path the path to the asset (in the {@literal <resources>}/data folder)
     */
    public void _createSound(String name, String path) {
        checkName(name);
        assets.put(name, new SoundAsset(path, 1f));
    }

    /**
     * Creates a sound asset given a volume
     * @param name the unique name of the asset
     * @param path the path to the asset (in the {@literal <resources>}/data folder)
     * @param volume the volume of the sound asset
     */
    public void _createSound(String name, String path, float volume) {
        checkName(name);
        assets.put(name, new SoundAsset(path, volume));
    }

    /**
     * Creates a video asset which will be loaded once when the game starts and kept in memory until the process is terminated
     * @param name the unique name of the asset
     * @param path the path to the asset (in the {@literal <resources>}/data folder)
     * @deprecated VIDEO PLAYBACK DOESN'T WORK (check GameProcess' constructor for more information)
     */
    @Deprecated
    public void _createVideo(String name, String path) {
        checkName(name);
        assets.put(name, new VideoAsset(path));
    }

    /**
     * Creates a video asset which will be loaded once when the game starts and kept in memory until the process is terminated
     * @param name the unique name of the asset
     * @param path the path to the asset (in the {@literal <resources>}/data folder)
     * @param pixelsPerUnit the pixels to game units ratio (i.e.: 16 ppi is 16x16 image pixels from 0,0 to 1,1)
     * @deprecated VIDEO PLAYBACK DOESN'T WORK (check GameProcess' constructor for more information)
     */
    @Deprecated
    public void _createVideo(String name, String path, int pixelsPerUnit) {
        checkName(name);
        assets.put(name, new VideoAsset(path, pixelsPerUnit));
    }

    /**
     * Creates a custom asset (provided the asset implementation)
     * @param name the unique name of the asset
     * @param asset the font asset (needs to be created separately
     * @see FontAsset
     */
    public void _createFont(String name, FontAsset asset) {
        checkName(name);
        assets.put(name, asset);
    }

    /**
     * Creates a custom asset (provided the asset implementation)
     * @param name the unique name of the asset
     * @param asset the asset instance
     * @see Asset
     */
    public <T extends Asset> void _createCustom(String name, T asset) {
        checkName(name);
        assets.put(name, asset);
    }

    /**
     * Checks the name of an asset against the stored assets to check for it to be unique
     * @param name the name to check
     */
    private void checkName(String name) {
        if (assets.containsKey(name))
            throw new RuntimeException("The name ("+ name +") is already assigned to another asset! The asset was not loaded!");
    }

    /**
     * Retrieves an asset
     * @param name the unique name of the asset
     * @param type the type of the asset (for convenience)
     * @return the asset or null
     */
    public <T extends Asset> T _get(String name, Class<T> type) {
        Asset asset = assets.get(name);
        if (asset == null) return null;
        return (T) asset.clone(); // clone it, we don't want to reference to the stored asset
    }

    /**
     * Retrieves a sprite from a sprite sheet given the index of the sprite in the sheet
     * @param name the unique name of the sprite sheet asset (SpriteAsset)
     * @param index the index of the sprite in the sheet (going rows -> columns)
     * @return a sub sprite SpriteAsset
     */
    public SpriteAsset _getSpriteFromSheet(String name, int index) {
        SpriteAsset asset = (SpriteAsset) assets.get(name);
        if (asset == null) return null;

        return asset.getSubSprite(index);
    }

    // defaults

    /**
     * Retrieves the default font (Arial)
     * @return the Arial Font
     */
    public FontAsset _getDefaultFont() {
        return this._get(DEFAULT_FONT, FontAsset.class);
    }

    /**
     * Retrieves the default font (Arial)
     * @return the Arial Font
     */
    public static FontAsset getDefaultFont() {
        return getInstance()._getDefaultFont();
    }

    // =========== SECONDARY INSTANCES ===========

    /**
     * Creates a secondary instance of AssetManager, which is not saved as a singleton
     * @return the temporary instance of AssetManager
     */
    public static AssetManager createSecondary() {
        return new AssetManager();
    }
    /**
     * Creates a secondary instance of AssetManager, which is not saved as a singleton and clones the current instance's assets
     * This method should be used after an AssetManager is loaded with assets, and before it is loaded
     * @return the temporary instance of AssetManager with the cloned assets
     */
    public static AssetManager createSecondaryClone() {
        AssetManager manager = new AssetManager();
        // if the instance is null, just make a new asset manager
        if (getInstance() == null) return manager;

        // clone all assets
        getInstance().assets.forEach((name, asset) -> {
            manager.assets.put(name, asset.clone());
        });

        return manager;
    }
}
