package net.daskrr.cmgt.pxp.core.component;

import net.daskrr.cmgt.pxp.core.Time;
import net.daskrr.cmgt.pxp.data.assets.SpriteAsset;

import java.util.ArrayList;
import java.util.List;

/**
 * The Animation Component uses a given set of sprite frames distributed over a set duration to change the sprite of the SpriteRenderer.<br/>
 * <b>This component requires a SpriteRenderer</b><br/>
 * This component can be used in conjunction with Animator
 * @see SpriteRenderer
 * @see Animator
 */
public class Animation extends Component
{
    /**
     * The name of the animation, must be unique and can be used by the Animator
     * @see Animator
     */
    public String name;

    /**
     * The set of sprite frames that the animation comprises
     */
    public SpriteAsset[] frames;
    /**
     * The duration of the animation
     */
    public float duration;
    /**
     * Whether the animation should start from the start after it finishes
     */
    public boolean repeat = true;

    /**
     * The current frame of animation that is rendered
     */
    public int currentFrame = 0;
    /**
     * The current progress of the animation in seconds
     */
    private float currentTime;

    /**
     * Whether this animation is playing
     */
    public boolean playing = true;

    /**
     * Creates an animation component using all sprites from a sprite sheet
     * @param name The unique (per GameObject) name of the animation
     * @param spriteSheet The SpriteAsset sprite sheet (use AssetManager)
     * @param duration The duration of the animation
     * @see net.daskrr.cmgt.pxp.data.assets.AssetManager
     * @see net.daskrr.cmgt.pxp.core.GameObject
     */
    public Animation(String name, SpriteAsset spriteSheet, float duration) {
        this(name, spriteSheet, 0, spriteSheet.totalRows * spriteSheet.totalColumns - 1, duration);
    }

    /**
     * Creates an animation component using a specified range of sprites from a sprite sheet
     * @param name The unique (per GameObject) name of the animation
     * @param spriteSheet The SpriteAsset sprite sheet (use AssetManager)
     * @param start The index from the sprite sheet of where to start the animation (first sprite has an index of 0)
     * @param end The index from the sprite sheet of where to end the animation (set to -1 to use last index)
     * @param duration The duration of the animation
     * @see net.daskrr.cmgt.pxp.data.assets.AssetManager
     * @see net.daskrr.cmgt.pxp.core.GameObject
     */
    public Animation(String name, SpriteAsset spriteSheet, int start, int end, float duration) {
        this.name = name;
        int maxIndex = spriteSheet.totalRows * spriteSheet.totalColumns;
        if (end == -1)
            end = maxIndex - 1;

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

    /**
     * Creates an animation component using an array of sprites
     * @param name The unique (per GameObject) name of the animation
     * @param frames An array containing the frames (SpriteAssets) of the animation
     * @param duration The duration of the animation
     * @see net.daskrr.cmgt.pxp.data.assets.AssetManager
     * @see net.daskrr.cmgt.pxp.core.GameObject
     */
    public Animation(String name, SpriteAsset[] frames, float duration) {
        this.name = name;
        this.frames = frames;
        this.duration = duration;
    }

    /**
     * Starts playing the animation or resumes it
     */
    public void play() {
        this.playing = true;
    }

    /**
     * Pauses the animation
     */
    public void pause() {
        this.playing = false;
    }

    /**
     * Stops the animation, resetting its progress
     */
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
