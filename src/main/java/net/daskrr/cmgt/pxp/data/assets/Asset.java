package net.daskrr.cmgt.pxp.data.assets;

import processing.core.PApplet;

public abstract class Asset
{
    public String path;

    public Asset(String path) {
        this.path = path;
    }

    protected abstract void load();
}
