package net.daskrr.cmgt.pxp.data.assets;

import net.daskrr.cmgt.pxp.core.GameProcess;
import processing.core.PApplet;
import processing.core.PImage;

public class ImageAsset extends Asset
{
    private PImage image;

    public ImageAsset(String path) {
        super(path);
    }

    @Override
    protected void load() {
        image = GameProcess.getInstance().loadImage(this.path);
    }

    public PImage getPImage() {
        return this.image;
    }
}
