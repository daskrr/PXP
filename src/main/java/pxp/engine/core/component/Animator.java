package pxp.engine.core.component;

import pxp.annotations.RequiresComponent;
import pxp.engine.core.GameObject;

import java.util.HashMap;
import java.util.Map;

/**
 * The Animator Component makes the usage of Animation components easier and more organized.<br/>
 * The Animator automatically maps the Animation components of a GameObject and through its methods any animation can be
 * easily played, paused or stopped.<br/>
 * <i>Note: The Animation components must already exist in the GameObject.&nbsp;No dynamically added components will be mapped!</i>
 * @see Animation
 * @see GameObject
 */
@RequiresComponent(Animation.class)
public class Animator extends Component
{
    public static final String DEFAULT_ID = "__default";

    /**
     * The mapped Animation components
     */
    private final Map<String, Animation> animations = new HashMap<>();
    /**
     * The default animation to play
     */
    public String defaultAnimation = "";

    /**
     * Whether to immediately start playing an Animation when the game starts.<br/>
     * <i>This is defaulted to true</i>
     */
    public boolean autoPlay = true;

    /**
     * Creates a blank Animator component
     */
    public Animator() { }

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
     * Retrieves the default animation
     * @return the default Animation component
     */
    public Animation getAnimation() {
        return this.animations.get(defaultAnimation);
    }

    /**
     * Retrieves an animation
     * @param name the name of the animation
     * @return the animation component assigned to that name or null
     */
    public Animation getAnimation(String name) {
        if (name.equals(DEFAULT_ID))
            name = this.defaultAnimation;

        if (!this.animations.containsKey(name))
            return null;

        return this.animations.get(name);
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
     * Stops all other animations<br/>
     * <i>Note: If the specified animation is already playing, it will not be reset!</i>
     * @param animation the animation to play
     */
    public void play(String animation) {
        if (animation.equals(DEFAULT_ID))
            animation = this.defaultAnimation;

        if (!this.animations.containsKey(animation))
            return;

        // check if the animation is already playing
        if (this.animations.get(animation).playing)
            return;

        this.stop();
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
        if (animation.equals(DEFAULT_ID))
            animation = this.defaultAnimation;

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
        if (animation.equals(DEFAULT_ID))
            animation = this.defaultAnimation;

        if (!this.animations.containsKey(animation))
            return;

        this.animations.get(animation).stop();
    }
}
