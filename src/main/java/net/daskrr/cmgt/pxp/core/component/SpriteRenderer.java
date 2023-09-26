package net.daskrr.cmgt.pxp.core.component;

import net.daskrr.cmgt.pxp.core.Vector2;
import net.daskrr.cmgt.pxp.data.Pivot;
import net.daskrr.cmgt.pxp.data.assets.SpriteAsset;

public class SpriteRenderer extends Renderer
{
    public SpriteAsset sprite;
    public Pivot pivot = Pivot.CENTER;

    public SpriteRenderer() { }

    public SpriteRenderer(SpriteAsset sprite) {
        this(sprite, Pivot.CENTER);
    }

    public SpriteRenderer(SpriteAsset sprite, Pivot pivot) {
        this.sprite = sprite;
        this.pivot = pivot;
    }

    @Override
    public void render() {
        if (sprite == null) return;

        Vector2 size = new Vector2(sprite.size);

//        size.x = size.x / (float) sprite.getPixelsPerUnit();
//        size.y = size.y / (float) sprite.getPixelsPerUnit();

        Vector2 pivot = this.pivot.calculatePivot.apply(size);

        // push translation for pivot
        ctx().translate(pivot.x, pivot.y);

//        System.out.println(gameObject.name +"] size: " + size);
//        System.out.println(gameObject.name +"] sprite.size: " + sprite.size);

        if (sprite.isSubSprite)
            ctx().image(
                sprite.getPImage(),
                0, 0, size.x, size.y, // position and size to draw
                (int) sprite.pos.x, (int) sprite.pos.y, // corner 1 (uv1)
                (int) sprite.pos.x + (int) sprite.size.x, (int) sprite.pos.y + (int) sprite.size.y // corner2 (uv2)
            );
        else
            ctx().image(sprite.getPImage(), 0, 0, size.x, size.y);
    }
}
