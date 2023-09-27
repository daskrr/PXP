package net.daskrr.cmgt.pxp.core;

import net.daskrr.cmgt.pxp.data.GameSettings;

/**
 * The base for the game, this class needs to be extended in order to create the game
 */
public abstract class Game
{
    /**
     * The GameProcess, object that handles everything and extends PApplet
     * @see processing.core.PApplet
     */
    public final GameProcess process;

    /**
     * Creates & starts the game. The constructor must only be used once in the <i>static main()</i> method of the application.
     */
    public Game() {
        GameSettings settings = this.setup();
        Scene[] scenes = this.buildScenes();

        // create GameProcess
        this.process = new GameProcess(settings, scenes);
        this.process.run();
    }

    /**
     * The method where the GameSettings need to be customized and the AssetManager must have its assets defined & loads them
     * @return The settings of the game
     */
    public abstract GameSettings setup();

    /**
     * The method where the scenes of the game are put in order with game objects and their components.<br/>
     * <i>Note: if OOP is better for you or the scenes are too large, Scene can be extended for each scene of the game,
     * populated with GameObjects and their components and given a separate file</i>
     * @return An array of all the scenes of the game, in order
     */
    public abstract Scene[] buildScenes();
}
