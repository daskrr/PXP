package pxp.engine.core.component.ui;

import pxp.annotations.RequiresRectTransform;
import pxp.engine.core.RectTransform;
import pxp.engine.core.component.SpriteRenderer;
import pxp.engine.data.Pivot;
import pxp.engine.data.Vector2;
import pxp.engine.data.assets.SpriteAsset;
import pxp.engine.data.assets.AssetManager;

/**
 * The Image Component is used to render sprites in a UI context (Canvas) and is better suited for rendering UI images
 * than the {@link SpriteRenderer}
 */
@RequiresRectTransform
public class Image extends UIRenderer
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
     * Whether to preserve the original aspect ratio of the image<br/>
     * <i>Cancels out the one of the {@link RectTransform}'s x or y components (depending on which measurement of the
     * image (width or height) is greater)</i><br/>
     * <i>Defaulted to false</i>
     */
    public boolean preserveAspect = false;

    /**
     * Whether to use the pixels per unit the image has<br/>
     * <i>This can lead to undesired results as typically the size of the image is determined by the {@link RectTransform}</i><br/>
     * <i>This is defaulted to false</i>
     */
    public boolean usePixelsPerUnit = false;

    /**
     * Creates a blank Image component
     */
    public Image() { }

    /**
     * Creates a Image component, given a sprite
     * @param sprite the SpriteAsset to render (use AssetManager)
     * @see AssetManager
     */
    public Image(SpriteAsset sprite) {
        this(sprite, Pivot.CENTER);
    }

    /**
     * Creates a Image component, given a sprite
     * @param sprite the SpriteAsset to render (use AssetManager)
     * @param pivot the pivot point of the sprite
     * @see AssetManager
     */
    public Image(SpriteAsset sprite, Pivot pivot) {
        this.sprite = sprite;
        this.pivot = pivot;
    }

    @Override
    public void render() {
        super.render();

        if (sprite == null) return;

        Vector2 size = new Vector2(sprite.size);

        // ppu or rect size
        if (usePixelsPerUnit) {
            size.x = size.x / (float) sprite.getPixelsPerUnit();
            size.y = size.y / (float) sprite.getPixelsPerUnit();
        }
        else {
            if (preserveAspect) {
                // depending on the image's dominant size, we use either x or y of the rect size to determine the size
                // of the image calculated with the aspect ratio
                if (size.x > size.y) {
                    float aspectRatio = size.y / size.x;
                    size = new Vector2(rectTransform().size.x, rectTransform().size.x * aspectRatio);
                }
                else {
                    float aspectRatio = size.x / size.y;
                    size = new Vector2(rectTransform().size.y * aspectRatio, rectTransform().size.y);
                }
            }
            else
                size = rectTransform().size;
        }

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
