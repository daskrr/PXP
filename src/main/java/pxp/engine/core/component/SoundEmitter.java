package pxp.engine.core.component;

import pxp.annotations.LinkFieldInInspector;
import pxp.annotations.LinkState;
import pxp.annotations.LinkType;
import pxp.engine.data.assets.SoundAsset;
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
    public boolean autoPlay = false;

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
     * <i>Does not include the innerRadius (is calculated from when exiting the innerRadius outwards)</i>
     */
    public float outerRadius = 10f;

    /**
     * Creates a blank sound emitter component
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
        if (this.autoPlay)
            this.play();
    }

    @Override
    public void update() {
        if (!isSpatial) return;

        // calculate the distance between the camera center and this game object on the X axis
        float distX = transform().position.x - gameObject.scene.getCamera().transform().position.x;
        float distInnerX = Math.min(Math.max(distX, -innerRadius), innerRadius);
        // pan the sound to left or right
        float panX = PApplet.map(distInnerX, -innerRadius,innerRadius, -1f,1f);
        sound.getSound().pan(panX);

        // calculate the distance between the camera center and this game object on the Y axis
        float distY = Math.abs(transform().position.y - gameObject.scene.getCamera().transform().position.y);
        float distInnerY = Math.min(Math.max(distY, 0), innerRadius);
        float panY = PApplet.map(distInnerY, 0,innerRadius, 0,.5f);
        float volumePanY = 1f - panY;
        sound.setVolume(this.volume * volumePanY);

        // check if the distance is smaller than innerRadius, if so volume fading shouldn't happen
        if (transform().position.distance(gameObject.scene.getCamera().transform().position) <= innerRadius) return;

        // calculate when the sound needs to fade and eventually reach 0f
        // calculate the distance between the camera center and this game object,
        // subtracting the inner radius so volume fading starts here
        float dist = transform().position.distance(gameObject.scene.getCamera().transform().position) - innerRadius;
        float distOuter = Math.min(Math.max(dist, 0), outerRadius);
        float fadeVolume = PApplet.map(distOuter, 0,outerRadius, 1f,0f);
        sound.setVolume(this.volume * volumePanY * fadeVolume);
    }

    @Override
    public void destroy() {
        this.stop();
    }

    /**
     * Plays the sound taking loop into consideration<br/>
     * This will not resume the sound if loop is enabled!<br/>
     * <i>Note: If the sound is already playing, it will not be restarted!</i>
     */
    public void play() {
        if (this.sound.isPlaying())
            return;

        if (loop)
            this.sound.getSound().loop();
        else
            this.sound.play();
    }
    /**
     * Pauses the sound
     */
    public void pause() {
        if (!this.sound.isPlaying()) return;

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
    @LinkFieldInInspector(name = "sound", type = LinkType.GETTER, state = LinkState.BOTH)
    public SoundAsset getSound() {
        return this.sound;
    }
    /**
     * Sets the sound asset (or changes it) applying the settings of this component
     * @param sound the SoundAsset
     */
    @LinkFieldInInspector(name = "sound", type = LinkType.SETTER, state = LinkState.BOTH)
    public void setSound(SoundAsset sound) {
        this.sound = sound;

        // in case we are in the inspector, this should not set volume or play as it is not needed
        if (!started) return;

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
    @LinkFieldInInspector(name = "volume", type = LinkType.SETTER, state = LinkState.BOTH)
    public void setVolume(float volume) {
        this.volume = volume;
        if (started)
            this.sound.setVolume(volume);
    }
    /**
     * Retrieves the volume of the component
     * @return the volume (0f - 1f)
     */
    @LinkFieldInInspector(name = "volume", type = LinkType.GETTER, state = LinkState.BOTH)
    public float getVolume() {
        return this.volume;
    }
    /**
     * Set whether to loop the audio. Setting this after the track started will start it again!
     * @param loop loop on or off
     */
    @LinkFieldInInspector(name = "loop", type = LinkType.SETTER, state = LinkState.BOTH)
    public void setLoop(boolean loop) {
        this.loop = loop;

        if (!started) return;

        if (loop)
            this.sound.getSound().loop();
        else
            this.sound.play();

    }
    /**
     * Retrieves whether the audio is going to loop
     * @return loop on or off
     */
    @LinkFieldInInspector(name = "loop", type = LinkType.GETTER, state = LinkState.BOTH)
    public boolean getLoop() {
        return this.loop;
    }
}
