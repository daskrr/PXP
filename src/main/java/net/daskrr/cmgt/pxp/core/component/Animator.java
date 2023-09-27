package net.daskrr.cmgt.pxp.core.component;

import java.util.HashMap;
import java.util.Map;

/**
 * The Animator Component makes the usage of Animation components easier and more organized.<br/>
 * The Animator automatically maps the Animation components of a GameObject and through its methods any animation can be
 * easily played, paused or stopped.<br/>
 * <i>Note: The Animation components must already exist in the GameObject. No dynamically added components will be mapped!</i>
 * @see Animation
 * @see net.daskrr.cmgt.pxp.core.GameObject
 */
public class Animator extends Component
{
    /**
     * The mapped Animation components
     */
    private final Map<String, Animation> animations = new HashMap<>();
    /**
     * The default animation to play
     */
    private String defaultAnimation = "";

    /**
     * Whether to immediately start playing an Animation when the game starts.
     */
    private boolean autoPlay = true;

    /**
     * Creates an animator component, given a default animation
     * @param defaultAnimation The animation that plays at the start of the game
     */
    public Animator(String defaultAnimation) {
        this.defaultAnimation = defaultAnimation;
    }

    /**
     * Creates an animator component, given a default animation and whether to automatically play said Animation
     * @param defaultAnimation The animation that plays at the start of the game
     * @param autoPlay Whether the default animation start playing immediately
     */
    public Animator(String defaultAnimation, boolean autoPlay) {
        this.defaultAnimation = defaultAnimation;
        this.autoPlay = autoPlay;
    }

    @Override
    public void start() {
        Animation[] animations = gameObject.getComponentsOfType(Animation.class);
        for (Animation animation : animations) {
            this.animations.put(animation.name, animation);

            if (!animation.name.equals(defaultAnimation))
                animation.stop();
            // if autoplay is disabled, stop even the default animation
            else if (!autoPlay)
                animation.stop();
        }
    }

    /**
     * Plays the default animation<br/>
     * Stops all other animations
     */
    public void play() {
        this.stop();
        if (!this.animations.containsKey(defaultAnimation))
            return;
        this.animations.get(defaultAnimation).play();
    }

    /**
     * Plays the specified animation<br/>
     * Stops all other animations
     * @param animation the animation to play
     */
    public void play(String animation) {
        this.stop();
        if (!this.animations.containsKey(animation))
            return;
        this.animations.get(animation).play();
    }

    /**
     * Pauses all animations
     */
    public void pause() {
        this.animations.forEach((name, anim) -> anim.pause());
    }

    /**
     * Pauses the specified animation
     * @param animation the animation to pause
     */
    public void pause(String animation) {
        if (!this.animations.containsKey(animation))
            return;
        this.animations.get(animation).pause();
    }

    /**
     * Stops all animations
     */
    public void stop() {
        this.animations.forEach((name, anim) -> anim.stop());
    }

    /**
     * Stops the specified animation
     * @param animation the animation to stop
     */
    public void stop(String animation) {
        if (!this.animations.containsKey(animation))
            return;
        this.animations.get(animation).stop();
    }
}
