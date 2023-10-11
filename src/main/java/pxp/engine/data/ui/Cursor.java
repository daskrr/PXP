package pxp.engine.data.ui;

import pxp.engine.core.GameProcess;
import pxp.engine.data.assets.SpriteAsset;
import processing.core.PConstants;

public class Cursor
{
    public static final Cursor NONE = new Cursor(-1);
    public static final Cursor ARROW = new Cursor(PConstants.ARROW);
    public static final Cursor CROSS = new Cursor(PConstants.CROSS);
    public static final Cursor HAND = new Cursor(PConstants.HAND);
    public static final Cursor MOVE = new Cursor(PConstants.MOVE);
    public static final Cursor TEXT = new Cursor(PConstants.TEXT);
    public static final Cursor WAIT = new Cursor(PConstants.WAIT);

    public static Cursor createCustom(SpriteAsset sprite) {
        return new Cursor(sprite);
    }

    public final int id;
    public final SpriteAsset sprite;

    private Cursor(int id) {
        this.id = id;
        this.sprite = null;
    }
    private Cursor(SpriteAsset sprite) {
        this.id = -1;
        this.sprite = sprite;
    }

    public void use(GameProcess context) {
        if (this.id == -1 && this.sprite == null) {
            context.noCursor();
            return;
        }

        if (this.id == -1) {
            context.cursor(sprite.getPImage());
            return;
        }

        context.cursor(this.id);
    }
}
