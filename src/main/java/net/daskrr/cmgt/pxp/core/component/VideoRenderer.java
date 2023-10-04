package net.daskrr.cmgt.pxp.core.component;

import net.daskrr.cmgt.pxp.data.Vector2;
import net.daskrr.cmgt.pxp.data.Pivot;
import net.daskrr.cmgt.pxp.data.assets.AssetManager;
import net.daskrr.cmgt.pxp.data.assets.VideoAsset;

/**
 * The VideoRenderer Component is crucial to displaying videos on the screen.
 * <b>VIDEO PLAYBACK <i>DOES NOT</i> WORK!</b> If you wish to try and fix it, first look at GameProcess' constructor for further instructions<br/>
 * <i>Given the aforementioned issue, the entirety of whether a video plays (correctly) is untested!</i>
 * @see AssetManager
 */
@Deprecated
public class VideoRenderer extends Renderer
{
    /**
     * The video to be rendered
     */
    public VideoAsset video;
    /**
     * The pivot of the video
     */
    public Pivot pivot = Pivot.CENTER;

    /**
     * Creates a VideoRenderer component
     */
    public VideoRenderer() { }

    /**
     * Creates a VideoRenderer component, given a video asset
     * @param video the VideoAsset to render (use AssetManager)
     * @see net.daskrr.cmgt.pxp.data.assets.AssetManager
     */
    public VideoRenderer(VideoAsset video) {
        this(video, Pivot.CENTER);
    }

    /**
     * Creates a VideoRenderer component, given a video
     * @param video the VideoAsset to render (use AssetManager)
     * @param pivot the pivot point of the sprite
     * @see net.daskrr.cmgt.pxp.data.assets.AssetManager
     */
    public VideoRenderer(VideoAsset video, Pivot pivot) {
        this.video = video;
        this.pivot = pivot;
    }

    @Override
    public void render() {
        if (video == null) return;
        if (video.getMovie() == null) return;

        video.movie.read();

        Vector2 size = new Vector2(video.size);

        size.x = size.x / (float) video.getPixelsPerUnit();
        size.y = size.y / (float) video.getPixelsPerUnit();

        Vector2 pivot = this.pivot.calculatePivot.apply(size);

        // push translation for pivot
        ctx().translate(pivot.x, pivot.y);

        ctx().image(video.getMovie(), 0, 0, size.x, size.y);
    }
}
