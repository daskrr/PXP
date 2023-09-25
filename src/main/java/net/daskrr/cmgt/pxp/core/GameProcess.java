package net.daskrr.cmgt.pxp.core;

import net.daskrr.cmgt.pxp.data.GameSettings;
import net.daskrr.cmgt.pxp.data.assets.AssetManager;
import processing.core.PApplet;
import processing.opengl.PGraphicsOpenGL;
import processing.opengl.Texture;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameProcess extends PApplet
{
    private static GameProcess instance;
    public static GameProcess getInstance() {
        return instance;
    }

    protected GameSettings settings;
    private Scene[] scenes;
    private int currentScene = 0;

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
    }

    @Override
    public void draw() {
        Time.newFrame();
        // update method
        for (GameObject go : getCurrentScene().objects)
            go.draw();

        render();
    }

    private void render() {
        // refresh background
        background(this.settings.background.getHex());

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
