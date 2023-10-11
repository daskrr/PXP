package pxp.engine.data.assets;

import pxp.engine.data.Vector2;
import processing.core.PApplet;
import processing.video.Movie;

/**
 * The video asset stores video files which are created through the AssetManager<br/>
 * <b>VIDEO PLAYBACK <i>DOES NOT</i> WORK!</b> If you wish to try and fix it, first look at GameProcess' constructor for further instructions<br/>
 * <i>Given the aforementioned issue, the entirety of whether a video plays (correctly) is untested!</i>
 * @see AssetManager
 */
@Deprecated
public class VideoAsset extends Asset
{
    /**
     * Videos don't load properly while in development, not sure if they load properly for a built jar, but set this to
     * null if you are not in development and pray.<br/>
     * <i>Note: this needs to be pre and suffixed by the system separator</i><br/>
     * <i>The default value is: \src\main\resources\data\ (with windows "\" separator)</i>
     */
    public static String developmentPath = "\\src\\main\\resources\\data\\";

    /**
     * The Movie instance, the movie already loaded into memory
     */
    public Movie movie;

    /**
     * The size of the video
     */
    public Vector2 size;

    /**
     * The pixels per unit (the amount of pixels that would fit in 1 vertical or horizontal unit in the xOy plane of the 2D Game)<br/>
     * <i>Default: 100 pixels per unit</i>
     */
    private int pixelsPerUnit = 100;

    /**
     * Creates a video asset given a path
     *
     * @param path the path to the asset (asset must be in the resources directory)
     */
    public VideoAsset(String path) {
        super(path);
    }
    /**
     * Creates a video asset given a path and the pixels per unit
     *
     * @param path the path to the asset (asset must be in the resources directory)
     * @param pixelsPerUnit the amount of pixels that would fit in 1 vertical or horizontal unit in the xOy plane of the 2D Game
     */
    public VideoAsset(String path, int pixelsPerUnit) {
        super(path);
        this.pixelsPerUnit = pixelsPerUnit;
    }

    @Override
    protected void load(PApplet processing) {
        String path;
        if (developmentPath != null)
            path = processing.sketchPath() + developmentPath + this.path;
        else
            path = this.path;

        // separate thread because P3D would time out
        new Thread(() -> {
            System.out.println("Loading video: " + path);

            movie = new Movie(processing, path);
            size = new Vector2(movie.width, movie.height);

            System.out.println("Loaded video successfully: " + path);
        }).start();
    }

    /**
     * Retrieves the Movie (movie already loaded into memory?)
     * @return the Movie
     */
    public Movie getMovie() {
        return this.movie;
    }

    /**
     * Gets the pixels per unit
     * @return the pixels per unit for this movie
     */
    public int getPixelsPerUnit() {
        return this.pixelsPerUnit;
    }
}
