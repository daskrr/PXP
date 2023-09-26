package net.daskrr.cmgt.pxp.data.assets;

import net.daskrr.cmgt.pxp.core.GameProcess;
import net.daskrr.cmgt.pxp.core.Vector2;
import processing.core.PImage;

public class SpriteAsset extends Asset
{
    // sprite sheet
    public boolean isSpriteSheet = false;
    public int totalRows = -1;
    public int totalColumns = -1;

    public boolean isSubSprite = false;
    public int index = -1;
    public Vector2 pos;
    public Vector2 size;

    private PImage image;
    @Deprecated
    private int pixelsPerUnit;

    public SpriteAsset(String path) {
        super(path);
    }

    @Deprecated
    public SpriteAsset(String path, int pixelsPerUnit) {
        super(path);
        this.pixelsPerUnit = pixelsPerUnit;
    }

    @Override
    protected void load() {
        image = GameProcess.getInstance().loadImage(this.path);
        size = new Vector2(image.width, image.height);
    }

    public PImage getPImage() {
        return this.image;
    }
    @Deprecated
    public int getPixelsPerUnit() {
        return this.pixelsPerUnit;
    }

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

    public SpriteAsset getSubSprite(int index) {
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
