package net.daskrr.cmgt.pxp.core.component;

import net.daskrr.cmgt.pxp.core.GameProcess;
import net.daskrr.cmgt.pxp.core.Vector2;
import net.daskrr.cmgt.pxp.data.Pivot;
import net.daskrr.cmgt.pxp.data.assets.ImageAsset;

public class SpriteRenderer extends Component
{
    public ImageAsset sprite;
    public Pivot pivot = Pivot.CENTER;
    public String sortingLayer = "Default";
    public int orderInLayer = 0;

    public SpriteRenderer() { }

    public SpriteRenderer(ImageAsset sprite) {
        this(sprite, Pivot.CENTER);
    }

    public SpriteRenderer(ImageAsset sprite, Pivot pivot) {
        this.sprite = sprite;
        this.pivot = pivot;
    }

    public void render() {
        Vector2 size = new Vector2(
            sprite.getPImage().width,
            sprite.getPImage().height
        );
        Vector2 pivot = this.pivot.calculatePivot.apply(size);

        // push translation for pivot
        GameProcess.getInstance().translate(pivot.x, pivot.y);

        GameProcess.getInstance().image(sprite.getPImage(), 0, 0);
    }
}
