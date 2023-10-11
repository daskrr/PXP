package pxp.engine.core.component;

import pxp.annotations.RequiresRectTransform;
import pxp.engine.core.RectTransform;
import pxp.engine.data.Vector2;
import pxp.engine.data.Pivot;
import pxp.engine.data.assets.SpriteAsset;
import pxp.engine.data.assets.AssetManager;

/**
 * The SpriteRenderer Component is crucial to displaying images/textures on the screen.
 */
@RequiresRectTransform
public class SpriteRenderer extends PivotedRenderer
{
    /**
     * The sprite to be rendered
     */
    public SpriteAsset sprite;

    /**
     * Whether the sprite is flipped on the horizontal axis
     */
    public boolean flipX = false;
    /**
     * Whether the sprite is flipped on the vertical axis
     */
    public boolean flipY = false;

    /**
     * Creates a blank SpriteRenderer component
     */
    public SpriteRenderer() { }

    /**
     * Creates a SpriteRenderer component, given a sprite
     * @param sprite the SpriteAsset to render (use AssetManager)
     * @see AssetManager
     */
    public SpriteRenderer(SpriteAsset sprite) {
        this(sprite, Pivot.CENTER);
    }

    /**
     * Creates a SpriteRenderer component, given a sprite
     * @param sprite the SpriteAsset to render (use AssetManager)
     * @param pivot the pivot point of the sprite
     * @see AssetManager
     */
    public SpriteRenderer(SpriteAsset sprite, Pivot pivot) {
        this.sprite = sprite;
        this.pivot = pivot;
    }

    @Override
    public void start() {
        // force rect transform
        if (transform() instanceof RectTransform) return;

        gameObject.transform = transform().toRectTransform();

        if (sprite == null) return;
        // apply size to the rect transform
        Vector2 size = sprite.size.clone();

        size.x = size.x / (float) sprite.getPixelsPerUnit();
        size.y = size.y / (float) sprite.getPixelsPerUnit();

        rectTransform().size = size;
    }

    @Override
    public void render() {
        super.render();

        if (sprite == null) return;

        Vector2 size = sprite.size.clone();

        size.x = size.x / (float) sprite.getPixelsPerUnit();
        size.y = size.y / (float) sprite.getPixelsPerUnit();

        // set rect transform size in case sprite changed
        rectTransform().size = size;

        Vector2 pivot = this.pivot.calc.apply(size);

        // push translation for pivot
        ctx().translate(pivot.x, pivot.y);

        if (sprite.isSubSprite) {
            int[] uvs = new int[] {
                //                  u1                                   v1
                           (int) sprite.pos.x,                  (int) sprite.pos.y,         // corner 1
                (int) (sprite.pos.x + sprite.size.x), (int) (sprite.pos.y + sprite.size.y)  // corner 2
            };

            if (flipX) {
                uvs[0] = (int) (sprite.pos.x + sprite.size.x);
                uvs[2] = (int) sprite.pos.x;
            }
            if (flipY) {
                uvs[1] = (int) (sprite.pos.y + sprite.size.y);
                uvs[3] = (int) sprite.pos.y;
            }

            ctx().image(
                sprite.getPImage(),
                0, 0, size.x, size.y, // position and size to draw
                uvs[0], uvs[1], // corner 1 (u1v1)
                uvs[2], uvs[3]  // corner 2 (u2v2)
            );
        }
        else {
            int[] uvs = new int[] {
                //       u1                    v1
                          0,                   0,           // corner 1
                (int) sprite.size.x, (int) sprite.size.y    // corner 2
            };

            if (flipX) {
                uvs[0] = (int) sprite.size.x;
                uvs[2] = 0;
            }
            if (flipY) {
                uvs[1] = (int) sprite.size.y;
                uvs[3] = 0;
            }

            ctx().image(sprite.getPImage(), 0, 0, size.x, size.y, uvs[0], uvs[1], uvs[2], uvs[3]);
        }
    }
}
