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

public class GameProcess extends PApplet
{
    private static GameProcess instance;
    public static GameProcess getInstance() {
        return instance;
    }

    public GameSettings settings;
    private final Scene[] scenes;
    private int currentScene = 0;

    private boolean finishSetup = false;

    /**
     * GameProcess & main PApplet -> is the entry point of the application and handles everything
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
    }

    @Override
    public void settings() {
        size((int) settings.size.x, (int) settings.size.y, P3D);
        if (settings.fullscreen)
            fullScreen();
    }

    @Override
    public void setup() {
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
        if (!finishSetup) return;

        Time.newFrame();
        // update method
        for (GameObject go : getCurrentScene().objects)
            go.draw();

        render();
        Input.reset();
    }

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
            for (GameObject go : layer)
                if (go.renderer != null) {
                    go.transform.bind();
                    go.renderer.render();
                    go.transform.unbind();
                }
    }

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

    public Scene getCurrentScene() {
        return scenes[currentScene];
    }
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
