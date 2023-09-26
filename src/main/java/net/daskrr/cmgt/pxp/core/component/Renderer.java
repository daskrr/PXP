package net.daskrr.cmgt.pxp.core.component;

public abstract class Renderer extends Component
{
    public String sortingLayer = "Default";
    public int orderInLayer = 0;

    public abstract void render();
}
