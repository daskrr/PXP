package net.daskrr.cmgt.pxp.core.component;

import net.daskrr.cmgt.pxp.data.assets.SoundAsset;
import processing.core.PApplet;

/**
 * The SoundEmitter Component makes using sound easier and stored into a component<br/>
 * The SoundEmitter allows spatial audio as well as global audio*<br/>
 * <i>* spatial audio only works if the sound source is mono (on a single channel)</i>
 */
public class SoundEmitter extends Component
{
    /**
     * The sound asset
     */
    private SoundAsset sound = null;
    /**
     * The volume of the component (mixes with the volume of the asset)
     */
    private float volume = 1f;

    /**
     * Whether to loop the audio when the end of the track is reached
     */
    private boolean loop = false;
    /**
     * Whether the audio should start playing immediately as this component is started
     */
    private boolean autoPlay = false;

    /**
     * Whether the sound is spatial (affected by the relative position of this game object in regard to the camera position)<br/>
     * <b>!NOTE: The sound must be mono (on one channel) in order to use left-right panning.</b> Distance volume fade will still work.
     */
    public boolean isSpatial = false;
    /**
     * The inner radius for spatial audio (determines how much distance in units is required to reach full left or full right)
     */
    public float innerRadius = 5f;

    /**
     * The outer radius for spatial audio (determines the slow fading of the sound until the threshold is met when the sound stops)<br/>
     * <i>Does not include the innerRadius (is calculated from transform position outwards)</i>
     */
    public float outerRadius = 10f;

    /**
     * Creates an empty sound emitter component
     */
    public SoundEmitter() { }
    /**
     * Creates a sound emitter component given a SoundAsset
     * @param sound the sound asset
     * @see SoundAsset
     */
    public SoundEmitter(SoundAsset sound) {
        this.sound = sound;
    }

    @Override
    public void start() {
        this.play();
    }

    @Override
    public void update() {
        if (!isSpatial) return;

        // calculate the distance between the camera center and this game object on the X axis
        float distX = transform().position.x - gameObject.scene.getCamera().transform().position.x;
        float distInner = Math.min(Math.max(distX, -innerRadius), innerRadius);
        float pan = PApplet.map(distInner, -innerRadius,innerRadius, -1f,1f);
        sound.getSound().pan(pan);

        // calculate when the sound needs to fade and eventually reach 0f
        // calculate the distance between the camera center and this game object
        float dist = transform().position.distance(gameObject.scene.getCamera().transform().position);
        float distOuter = Math.min(Math.max(dist, -outerRadius), outerRadius);
        float fadeVolume = PApplet.map(distOuter, -outerRadius,outerRadius, 1f,0f);
        sound.setVolume(this.volume * fadeVolume);
    }

    /**
     * Plays the sound taking loop into consideration
     */
    public void play() {
        if (loop)
            this.sound.getSound().loop();
        else
            this.sound.play();
    }
    /**
     * Pauses the sound
     */
    public void pause() {
        this.sound.pause();
    }
    /**
     * Stops the sound
     */
    public void stop() {
        this.sound.stop();
    }

    /**
     * Retrieves the sound asset
     * @return the SoundAsset object
     */
    public SoundAsset getSound() {
        return this.sound;
    }
    /**
     * Sets the sound asset (or changes it) applying the settings of this component
     * @param sound the SoundAsset
     */
    public void setSound(SoundAsset sound) {
        this.sound = sound;
        this.sound.setVolume(volume);

        if (autoPlay && loop)
            this.sound.getSound().loop();
        else if (autoPlay)
            this.sound.play();
    }

    /**
     * Sets the volume of the component (mixes with the volume of the asset)
     * @param volume the volume to mix (0f - 1f)
     */
    public void setVolume(float volume) {
        this.volume = volume;
        this.sound.setVolume(volume);
    }
    /**
     * Retrieves the volume of the component
     * @return the volume (0f - 1f)
     */
    public float getVolume() {
        return this.volume;
    }
    /**
     * Set whether to loop the audio when the track finishes
     * @param loop loop on or off
     */
    public void setLoop(boolean loop) {
        this.loop = loop;
    }
    /**
     * Retrieves whether the audio is going to loop
     * @return loop on or off
     */
    public boolean getLoop() {
        return this.loop;
    }
    /**
     * Sets whether to autoplay (when the component is started)
     * @param autoPlay autoplay on or off
     */
    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
    }

    /**
     * Retrieves whether the audio is set to autoplay
     * @return autoplay on or off
     */
    public boolean getAutoPlay() {
        return this.autoPlay;
    }
}
