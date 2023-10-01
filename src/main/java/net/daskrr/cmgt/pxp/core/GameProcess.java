package net.daskrr.cmgt.pxp.core;

import net.daskrr.cmgt.pxp.core.component.Camera;
import net.daskrr.cmgt.pxp.data.GameSettings;
import net.daskrr.cmgt.pxp.data.Input;
import net.daskrr.cmgt.pxp.data.Key;
import net.daskrr.cmgt.pxp.data.MouseButton;
import net.daskrr.cmgt.pxp.data.assets.AssetManager;
import processing.core.PApplet;
import processing.opengl.PGraphicsOpenGL;

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
     * @param settings the settings of the game (such as window size)
     * @param scenes all the scenes this game comprises
     */
    public GameProcess(GameSettings settings, Scene[] scenes) {
        instance = this;

        this.settings = settings;
        this.scenes = scenes;

        // check sorting layers and make sure there is a default
        if (!settings.sortingLayers.contains("Default")) {
            settings.sortingLayers.add(0, "Default");
            this.settings.sortingLayers = settings.sortingLayers;
        }

        this.windowSize = new Vector2(settings.size);
    }

    @Override
    public void settings() {
        size((int) settings.size.x, (int) settings.size.y, P3D);
        if (settings.fullscreen)
            fullScreen();
    }

    @Override
    public void setup() {
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
                    if (go.renderer != null) {
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
}
