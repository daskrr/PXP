package net.daskrr.cmgt.pxp.core.component;

import net.daskrr.cmgt.pxp.core.Time;
import net.daskrr.cmgt.pxp.data.assets.SpriteAsset;

import java.util.ArrayList;
import java.util.List;

public class Animation extends Component
{
    public String name;

    public SpriteAsset[] frames;
    public float duration;
    public boolean repeat = true;

    public int currentFrame = 0;
    private float currentTime;

    public boolean playing = true;

    public Animation(String name, SpriteAsset spriteSheet, float duration) {
        this(name, spriteSheet, 0, spriteSheet.totalRows * spriteSheet.totalColumns - 1, duration);
    }

    public Animation(String name, SpriteAsset spriteSheet, int start, int end, float duration) {
        this.name = name;
        int maxIndex = spriteSheet.totalRows * spriteSheet.totalColumns;
        if (start < 0 || start > maxIndex || end < 0 || end < start) {
            start = 0;
            end = maxIndex - 1;
        }

        List<SpriteAsset> sprites = new ArrayList<>();
        for (int i = start; i < end; i++)
            sprites.add(spriteSheet.getSubSprite(i));

        this.frames = sprites.toArray(new SpriteAsset[0]);
        this.duration = duration;
        this.currentTime = 0;
    }

    public Animation(String name, SpriteAsset[] frames, float duration) {
        this.name = name;
        this.frames = frames;
        this.duration = duration;
    }

    public void play() {
        this.playing = true;
    }

    public void pause() {
        this.playing = false;
    }
    public void stop() {
        this.playing = false;
        currentTime = 0;
        currentFrame = 0;
    }

    @Override
    public void update() {
        if (gameObject.renderer == null || !(gameObject.renderer instanceof SpriteRenderer)) return;
        if (!this.playing) return;

        currentTime += Time.deltaTime;
        if (currentFrame + 1 >= frames.length) {
            if (!repeat) {
                this.stop();
                return;
            }

            currentTime = 0;
            currentFrame = 0;
        }

        if (currentTime > (duration / frames.length) * currentFrame) {
            currentFrame++;
            ((SpriteRenderer) gameObject.renderer).sprite = frames[currentFrame];
        }
    }
}
