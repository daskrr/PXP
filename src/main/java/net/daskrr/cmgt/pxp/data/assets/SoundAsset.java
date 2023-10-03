package net.daskrr.cmgt.pxp.data.assets;

import net.daskrr.cmgt.pxp.core.GameProcess;
import processing.sound.SoundFile;

/**
 * The SoundAsset contains the path to a sound and after loading, the SoundFile itself
 */
public class SoundAsset extends Asset
{
    /**
     * The sound file
     */
    private SoundFile sound = null;
    /**
     * The volume of the sound itself
     */
    private float volume = 1f;

    /**
     * Creates a sound asset given a path
     *
     * @param path the path to the asset (asset must be in the resources directory)
     */
    public SoundAsset(String path, float volume) {
        super(path);
        this.volume = volume;
    }

    @Override
    protected void load() {
        this.sound = new SoundFile(GameProcess.getInstance(), this.path);
        this.sound.amp(volume);
    }

    /**
     * Retrieves the sound file
     * @return the SoundFile
     * @see SoundFile
     */
    public SoundFile getSound() {
        return this.sound;
    }

    /**
     * Plays the audio
     */
    public void play() {
        sound.play();
    }
    /**
     * Pauses the audio
     */
    public void pause() {
        sound.pause();
    }
    /**
     * Stops the audio
     */
    public void stop() {
        sound.stop();
    }
    /**
     * Retrieves whether the audio is playing
     * @return audio paused or not
     */
    public boolean isPlaying() {
        return this.sound.isPlaying();
    }

    /**
     * Sets the base volume of the audio
     * @param volume the volume (0f - 1f)
     */
    public void setBaseVolume(float volume) {
        this.volume = volume;
    }
    /**
     * Retrieves the base volume
     * @return the base volume
     */
    public float getBaseVolume() {
        return this.volume;
    }

    /**
     * Sets the volume on top of the base volume (mixes the volumes)
     * @param volume the volume to mix (0f - 1f)
     */
    public void setVolume(float volume) {
        this.sound.amp(this.volume * volume);
    }
}
