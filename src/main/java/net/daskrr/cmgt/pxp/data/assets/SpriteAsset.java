package net.daskrr.cmgt.pxp.data.assets;

import net.daskrr.cmgt.pxp.core.GameProcess;
import net.daskrr.cmgt.pxp.data.Vector2;
import processing.core.PImage;

/**
 * The SpriteAsset contains the path to an image and after loading, the PImage itself<br/>
 * The SpriteAsset can either be a regular image, a sprite sheet or a sub sprite of a sprite sheet
 */
public class SpriteAsset extends Asset
{
    // sprite sheet
    /**
     * Whether this is a sprite sheet
     */
    public boolean isSpriteSheet = false;
    /**
     * The number of rows in the sprite sheet
     */
    public int totalRows = -1;
    /**
     * the number of columns in the sprite sheet
     */
    public int totalColumns = -1;

    /**
     * Whether this is a sub sprite of a sprite sheet
     */
    public boolean isSubSprite = false;
    /**
     * The index of the sub sprite
     */
    public int index = -1;
    /**
     * the position in the sprite sheet of the sub sprite
     */
    public Vector2 pos;

    /**
     * The size of the image
     */
    public Vector2 size;

    /**
     * The PImage instance, the image loaded into memory
     */
    private PImage image;
    /**
     * The pixels per unit (the amount of pixels that would fit in 1 vertical or horizontal unit in the xOy plane of the 2D Game)<br/>
     * <i>Default: 100 pixels per unit</i>
     */
    private int pixelsPerUnit = 100;

    /**
     * Creates a new SpriteAsset given a path
     * @param path the path to the image in the resources directory
     */
    public SpriteAsset(String path) {
        super(path);
    }

    /**
     * Creates a new SpriteAsset given a path and the pixels per unit
     * @param path the path to the image in the resources directory
     * @param pixelsPerUnit the amount of pixels that would fit in 1 vertical or horizontal unit in the xOy plane of the 2D Game
     */
    public SpriteAsset(String path, int pixelsPerUnit) {
        super(path);
        this.pixelsPerUnit = pixelsPerUnit;
    }

    @Override
    protected void load() {
        image = GameProcess.getInstance().loadImage(this.path);
        size = new Vector2(image.width, image.height);
    }

    /**
     * Retrieves the PImage (image already loaded into memory)
     * @return the PImage
     */
    public PImage getPImage() {
        return this.image;
    }

    /**
     * Gets the pixels per unit
     * @return the pixels per unit for this sprite
     */
    public int getPixelsPerUnit() {
        return this.pixelsPerUnit;
    }

    /**
     * Calculates and mathematically splits the sprite into a grid using
     * @see SpriteAsset#totalRows
     * @see SpriteAsset#totalColumns
     */
    private void calculateSprite() {
        int rowSize = (int) (size == null ? image.height : size.x) / totalRows;
        int colSize = (int) (size == null ? image.width : size.y) / totalColumns;

        int index = 0;
        for (int row = 0; row < totalRows; row++)
            for (int col = 0; col < totalColumns; col++) {
                if (index == this.index) {
                    this.pos = new Vector2(col * colSize, row * rowSize);
                    this.size = new Vector2(colSize, rowSize);

                    return;
                }

                index++;
            }

        throw new IndexOutOfBoundsException("The image index provided doesn't exist");
    }

    /**
     * Creates a sub sprite from an index, given this SpriteAsset is a sprite sheet
     * @param index the index of the sub sprite
     * @return the sub SpriteAsset or null if this isn't a sprite sheet
     */
    public SpriteAsset getSubSprite(int index) {
        if (!this.isSpriteSheet)
            return null;

        SpriteAsset asset = new SpriteAsset(path, pixelsPerUnit);
        asset.image = this.image;
        asset.totalRows = this.totalRows;
        asset.totalColumns = this.totalColumns;
        asset.index = index;
        asset.isSubSprite = true;
        asset.calculateSprite();

        return asset;
    }
}
