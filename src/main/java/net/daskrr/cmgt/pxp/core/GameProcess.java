package net.daskrr.cmgt.pxp.core;

import net.daskrr.cmgt.pxp.core.component.Camera;
import net.daskrr.cmgt.pxp.data.GameSettings;
import net.daskrr.cmgt.pxp.data.Input;
import net.daskrr.cmgt.pxp.data.Key;
import net.daskrr.cmgt.pxp.data.MouseButton;
import net.daskrr.cmgt.pxp.data.assets.AssetManager;
import processing.core.PApplet;
import processing.opengl.PGraphicsOpenGL;

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
     * The size of the window
     */
    public Vector2 windowSize = new Vector2(1, 1);

    /**
     * Creates the GameProcess (this happens automatically, do not re-instantiate!)
     * @param game the Game object from which this was created (contains game settings & scenes)
     */
    public GameProcess(Game game) {
        // Against my best efforts, I was unable to implement videos. GStreamer doesn't want to cooperate
        // The code remains as @Deprecated if anyone feels like taking a shot at it. Just uncomment the following line of code:
        // fixGStreamer();
        // The error that occurs is rather strange: "No such gstreamer factory: playbin". Processing's movie library uses
        // PlayBin to play the video. playbin is nowhere to be found in the natives. I've looked through the wrappers and I
        // could not figure it out. Good luck!

        instance = this;

        this.game = game;
        this.settings = game.settings;
        this.scenes = game.scenes;

        // check sorting layers and make sure there is a default
        if (!settings.sortingLayers.contains("Default")) {
            if (settings.sortingLayers.size() == 0)
                settings.sortingLayers.add("Default");
            else
                settings.sortingLayers.add(0, "Default");
        }

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

        // load first scene
        getCurrentScene().load();

        Time.lastTime = millis() / 1000f;
        Time.deltaTime = 0f;

        this.finishSetup = true;
    }

    /**
     * Step method, called every frame, checks window size, calls draw for all GameObjects of the current scene and calls render
     */
    @Override
    public void draw() {
        this.windowSize = new Vector2(width, height);

        if (!finishSetup) return;

        Time.newFrame();
        // update method
        for (GameObject go : getCurrentScene().objects)
            go.draw();

        render();
        Input.reset();
    }

    /**
     * The main render method, re-paints the background, sets up the camera view and renders GameObjects based on sorting layers
     */
    private void render() {
        // refresh background
        if (this.settings.backgroundImage != null)
            background(this.settings.backgroundImage.getPImage());
        else
            background(this.settings.background.getHex());

        try {
            Camera cam = getCurrentScene().getCamera();
            cam.applyCamera();
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(0);
        }

        for (List<GameObject> layer : getCurrentScene().objectsByLayer)
            if (layer != null)
                for (GameObject go : layer)
                    if (go.renderer != null && go.isActive) {
                        go.transform.bind();
                        go.renderer.render();
                        go.transform.unbind();
                    }
    }

    /**
     * Starts the game through Processing
     */
    protected void run() {
        String[] args = new String[] { this.getClass().getName() };
        runSketch(args, this);
    }

    @Override
    public void mouseClicked() {
        MouseButton button = MouseButton.fromCode(mouseButton);
        if (button == null)
            return;

        Input.mouseButtonsClicked[button.ordinal()] = button;
    }
    @Override
    public void mousePressed() {
        MouseButton button = MouseButton.fromCode(mouseButton);
        if (button == null)
            return;

        Input.mouseButtons[button.ordinal()] = button;
    }
    @Override
    public void mouseReleased() {
        MouseButton button = MouseButton.fromCode(mouseButton);
        if (button == null)
            return;

        Input.mouseButtons[button.ordinal()] = null;
    }

    @Override
    public void keyPressed() {
        Key key = Key.fromCode(keyCode);
        if (key == null)
            return;

        Input.keys[key.ordinal()] = key;
    }
    @Override
    public void keyReleased() {
        Key key = Key.fromCode(keyCode);
        if (key == null)
            return;

        Input.keys[key.ordinal()] = null;
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
        currentScene = index;
        getCurrentScene().load();
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
