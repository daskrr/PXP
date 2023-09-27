package net.daskrr.cmgt.pxp.data.assets;

/**
 * The base for Assets
 */
public abstract class Asset
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
    protected abstract void load();
}
