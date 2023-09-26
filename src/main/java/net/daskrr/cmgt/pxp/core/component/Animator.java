package net.daskrr.cmgt.pxp.core.component;

import java.util.HashMap;
import java.util.Map;

public class Animator extends Component
{
    private final Map<String, Animation> animations = new HashMap<>();
    private String defaultAnimation = "";

    public Animator(String defaultAnimation) {
        this.defaultAnimation = defaultAnimation;
    }

    @Override
    public void start() {
        Animation[] animations = gameObject.getComponentsOfType(Animation.class);
        for (Animation animation : animations) {
            this.animations.put(animation.name, animation);

            if (!animation.name.equals(defaultAnimation))
                animation.stop();
        }
    }

    public void play() {
        this.animations.get(defaultAnimation).play();
    }
    public void play(String animation) {
        this.animations.get(animation).play();
    }

    public void pause() {
        this.animations.forEach((name, anim) -> anim.pause());
    }
    public void pause(String animation) {
        this.animations.get(animation).pause();
    }

    public void stop() {
        this.animations.forEach((name, anim) -> anim.stop());
    }

    public void stop(String animation) {
        this.animations.get(animation).stop();
    }
}
