package pxp.engine.core;

import pxp.engine.core.manager.CollisionManager;
import pxp.engine.core.manager.LayerManager;
import pxp.engine.data.assets.AssetManager;
import pxp.engine.data.ui.Cursor;
import processing.core.PApplet;
import processing.event.MouseEvent;
import processing.opengl.PGraphicsOpenGL;
import pxp.engine.data.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The Game Process is the "trunk" of the game application, from where systems are started, assets imported, scenes loaded...<br/>
 * The Game Process extends PApplet (the processing application "trunk")
 * @see PApplet
 */
public class GameProcess extends PApplet
{
    /**
     * The instance of the GameProcess (works as singleton, too)
     */
    private static GameProcess instance;
    /**
     * Gets the instance of the GameProcess, can be used almost anywhere
     * @return the instance of the game process
     */
    public static GameProcess getInstance() {
        return instance;
    }

    public final Game game;
    /**
     * The settings of the game<br/>
     * <i>(modifying them while the game runs can break the application)</i>
     */
    public final GameSettings settings;
    /**
     * The scenes of the game
     */
    private final Scene[] scenes;
    /**
     * The currently active scene
     */
    private int currentScene = 0;

    /**
     * Whether the setup() function has finished. Used to determine whether the process can start drawing
     */
    private boolean finishSetup = false;

    /**
     * [Internal] Callback for when the setup has finished
     */
    public Runnable finishSetupCallback = () -> {};

    /**
     * The size of the window
     */
    public Vector2 windowSize = new Vector2(1, 1);

    /**
     * [Internal] Determines whether this GameProcess uses Processing's draw method to step or, it is controlled externally<br/>
     * <i>This is only used by the Editor and should not be set to false (as the game will not step)</i>
     */
    public boolean autoDraw = true;

    private final List<Runnable> nextFrameQueue = new ArrayList<>();

    private final List<Routine> routines = new ArrayList<>();

    /**
     * The Collision Manager
     */
    public CollisionManager collisionManager;

    /**
     * Whether to forcibly draw all gizmos (may cause lag, as every component's gizmosDraw will be invoked every frame)
     */
    public boolean forceDrawGizmos;

    /**
     * Holds the scene change persistent game objects
     */
    protected final List<GameObject> persistent = new ArrayList<>();

    public GameProcess(Game game) {
        this(game, false);
    }

    /**
     * Creates the GameProcess (this happens automatically, do not re-instantiate!)
     * @param game the Game object from which this was created (contains game settings & scenes)
     */
    public GameProcess(Game game, boolean secondary) {
        // Against my best efforts, I was unable to implement videos. GStreamer doesn't want to cooperate
        // The code remains as @Deprecated if anyone feels like taking a shot at it. Just uncomment the following line of code:
        // fixGStreamer();
        // The error that occurs is rather strange: "No such gstreamer factory: playbin". Processing's movie library uses
        // PlayBin to play the video. playbin is nowhere to be found in the natives. I've looked through the wrappers, and I
        // could not figure it out. Good luck!

        // this is used in case multiple instances are required, so we don't override the existing singleton
        if (!secondary)
            instance = this;

        Time.createDefault();
        Input.createDefault();

        this.game = game;
        this.settings = game.settings;
        this.forceDrawGizmos = game.settings.forceDrawGizmos;
        this.scenes = game.scenes;

        for (int i = 0; i < this.scenes.length; i++)
            this.scenes[i].index = i;

        LayerManager.initialize(this.settings.layers, this.settings.sortingLayers);

        this.collisionManager = new CollisionManager(this.settings.ignoreCollisionLayers);

        this.windowSize = new Vector2(settings.size);
    }

    /**
     * Sets up the size of the game and fullscreen
     */
    @Override
    public void settings() {
        size((int) settings.size.x, (int) settings.size.y, P3D);
        if (settings.fullscreen)
            fullScreen();
    }

    /**
     * Calls Game#setup, sets resizable, target frame rate, texture filtering, loads assets with AssetManager, the first scene, sets up time
     * @see AssetManager
     * @see Time
     */
    @Override
    public void setup() {
        game.setup();

        if (!settings.fullscreen && settings.resizable)
            surface.setResizable(true);

        frameRate(settings.targetFPS);
        ((PGraphicsOpenGL) g).textureSampling(settings.textureFilter.ordinal());

        // load assets
        AssetManager.load();

        // game settings after asset manager
        resetCursor();

        // load first scene
        getCurrentScene().context = this;
        getCurrentScene().load();

        this.finishSetup = true;
        this.finishSetupCallback.run();
    }

    @Override
    public void draw() {
        if (autoDraw)
            this.step();
    }

    /**
     * Step method, called every frame, checks window size,
     * calls update for all GameObjects of the current scene and calls render
     */
    public void step() {
        this.windowSize = new Vector2(width, height);

        if (!finishSetup) return;

        Time.newFrame();

        // execute next frame queue
        // PREVENT CONCURRENT ON THE FUCKING QUEUE (╯°□°)╯︵ ┻━┻
        List<Runnable> queue = new ArrayList<>(this.nextFrameQueue);
        queue.forEach(Runnable::run);
        this.nextFrameQueue.removeAll(queue);

        // update method
        for (GameObject go : getCurrentScene().objects)
            go.update();

        this.collisionManager.update();

        // routines
        this.stepRoutines();

        getCurrentScene().render();
        Input.reset();
    }

    /**
     * [Internal] Starts the game through Processing
     */
    public void run() {
        String[] args = new String[] { this.getClass().getName() };
        runSketch(args, this);
    }

    // ==================== INPUT ====================

    @Override
    public void mouseClicked(MouseEvent event) {
        MouseButton button = MouseButton.fromCode(mouseButton);
        if (button == null)
            return;

        Input.getDefault().mouseButtonsClicked[button.ordinal()] = button;

        getCurrentScene().propagateMouseEvent(event);
    }
    @Override
    public void mouseWheel(MouseEvent event) {
        Input.getDefault().isScrolling = true;
        Input.getDefault().scrollAmount = event.getCount();

        getCurrentScene().propagateMouseEvent(event);
    }
    @Override
    public void mousePressed(MouseEvent event) {
        MouseButton button = MouseButton.fromCode(mouseButton);
        if (button == null)
            return;

        Input.getDefault().mouseButtons[button.ordinal()] = button;

        getCurrentScene().propagateMouseEvent(event);
    }
    @Override
    public void mouseReleased(MouseEvent event) {
        MouseButton button = MouseButton.fromCode(mouseButton);
        if (button == null)
            return;

        Input.getDefault().mouseButtons[button.ordinal()] = null;

        getCurrentScene().propagateMouseEvent(event);
    }
    @Override
    public void mouseMoved(MouseEvent event) {
        Input.getDefault().mousePos = new Vector2(event.getX(), event.getY());

        getCurrentScene().propagateMouseEvent(event);
    }
    @Override
    public void mouseDragged(MouseEvent event) {
        getCurrentScene().propagateMouseEvent(event);
    }

    @Override
    public void keyPressed() {
        Key key = Key.fromCode(keyCode);
        if (key == null)
            return;

        Input.getDefault().keys[key.ordinal()] = key;
    }
    @Override
    public void keyReleased() {
        Key key = Key.fromCode(keyCode);
        if (key == null)
            return;

        Input.getDefault().keys[key.ordinal()] = null;
    }


    // ==================== ASYNCHRONICITY ====================

    /**
     * Executes the runnable at the beginning of the next frame
     * @param run what to execute
     */
    public static void nextFrame(Runnable run) {
        getInstance()._nextFrame(run);
    }

    /**
     * Executes the runnable at the beginning of the next frame (in instance)
     * @param run what to execute
     */
    public void _nextFrame(Runnable run) {
        this.nextFrameQueue.add(run);
    }

    /**
     * Creates a routine that runs after a specified amount of time
     * @param gameObject the game object that owns this routine
     * @param time the amount of time after which this routine should be executed (in seconds)
     * @param run the action to perform
     * @return the routine created
     */
    public Routine runLater(GameObject gameObject, float time, Runnable run) {
        Routine routine = new Routine(gameObject.hashCode(), time, run);
        routine.setRemove(() -> this.routines.remove(routine));

        this.routines.add(routine);
        return routine;
    }

    /**
     * Creates a routine that runs every x seconds
     * @param gameObject the game object that owns this routine
     * @param time the time interval between executions (in seconds)
     * @param run the action to perform
     * @return the routine created
     */
    public Routine runInterval(GameObject gameObject, float time, Runnable run) {
        Routine routine = new Routine(gameObject.hashCode(), time, run);
        routine.interval = true;
        routine.setRemove(() -> this.routines.remove(routine));

        this.routines.add(routine);
        return routine;
    }

    /**
     * Removes a routine using the routine object
     * @param routine the routine to remove
     */
    public void removeRoutine(Routine routine) {
        this.routines.remove(routine);
    }

    /**
     * Removes all routines of a game object
     * @param gameObject the game object that owns the routines
     */
    public void removeAllRoutines(GameObject gameObject) {
        this.routines.removeIf(r -> r.id == gameObject.hashCode());
    }

    /**
     * Called every frame to advance routines
     */
    private void stepRoutines() {
        new ArrayList<>(routines).forEach(Routine::step);
    }



    // ==================== UTIL ====================

    /**
     * Sets the cursor
     * @param cursor the cursor to use
     */
    public void setCursor(Cursor cursor) {
        if (cursor == null) return;

        cursor.use(this);
    }

    /**
     * Resets the cursor to the default cursor (from settings)
     */
    public void resetCursor() {
        settings.cursor.use(this);
    }

    /**
     * Gets the current scene
     * @return the currently active scene
     */
    public Scene getCurrentScene() {
        return scenes[currentScene];
    }
    /**
     * Sets the current scene, destroying the previous
     * @param index the index (position in the scenes array) of the scene to load
     */
    public void setScene(int index) {
        // check if index is valid
        if (index >= scenes.length)
            throw new IndexOutOfBoundsException("The provided scene index is invalid.");

        // destroy current scene
        getCurrentScene().destroy();
        // prevent concurrent & fuckery
        nextFrame(() -> {
            // set the current scene
            currentScene = index;

            getCurrentScene().context = this;

            // add the persistent objects to the scene
            for (GameObject persistent : this.persistent) {
                persistent.setScene(getCurrentScene());
                getCurrentScene().addGameObjectBeforeLoad(persistent);
            }

            // finally, load the scene
            getCurrentScene().load();
        });
    }



    /**
     * Sets GStreamer library path
     */
    @Deprecated
    private void fixGStreamer() {
        if (PApplet.platform == WINDOWS)
            System.setProperty("gstreamer.library.path", sketchPath() + "\\natives\\windows-amd64");
        else if (PApplet.platform == MACOS)
            System.setProperty("gstreamer.library.path", sketchPath() + "/natives/macos-x86_64");
        else if (PApplet.platform == LINUX)
            System.setProperty("gstreamer.library.path", sketchPath() + "/natives/linux-amd64");
//        System.setProperty("gstreamer.plugin.path", System.getProperty("gstreamer.library.path") + "\\gstreamer-1.0");
    }
}
