package pxp.engine.data.assets;

import processing.core.PApplet;

/**
 * The base for Assets
 */
public abstract class Asset implements Cloneable
{
    /**
     * The path to the asset (asset must be in the resources directory)
     */
    public String path;

    /**
     * Creates an asset given a path
     * @param path the path to the asset (asset must be in the resources directory)
     */
    public Asset(String path) {
        this.path = path;
    }

    /**
     * Callback method for when the asset is loaded from disk into memory
     */
    protected abstract void load(PApplet processing);

    @Override
    public Asset clone() {
        try {
            return (Asset) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return null;
    }

//    @Override
//    public String toString() {
//        return "Asset [" + this.path + "]";
//    }
}
