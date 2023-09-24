package net.daskrr.cmgt.pxp.core;

import net.daskrr.cmgt.pxp.data.GameSettings;
import processing.core.PApplet;

public class GameProcess extends PApplet
{
    private GameSettings settings;
    private Scene[] scenes;

    /**
     * GameProcess empty constructor for Processing to use
     */
    public GameProcess() {}

    /**
     * GameProcess & main PApplet is the entry point of the application and handles everything
     * @param settings the settings of the game (such as window size)
     * @param scenes all the scenes this game comprises
     */
    public GameProcess (GameSettings settings, Scene[] scenes) {
        this.settings = settings;
        this.scenes = scenes;
    }

    @Override
    public void settings() {
        size(800,800);
    }

    @Override
    public void setup() {

    }

    @Override
    public void draw() {
        background(0x000000);
    }

    protected void run() {
        String[] args = new String[] { this.getClass().getName() };
        runSketch(args, this);
    }
}
