package pxp.engine.core;

import pxp.engine.data.GameSettings;

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

    protected final GameSettings settings;
    protected final Scene[] scenes;

    /**
     * Creates & starts the game. The constructor must only be used once in the <i>static main()</i> method of the application.
     */
    public Game() {
        settings = this.startup();
        scenes = this.buildScenes();

        // create GameProcess
        this.process = new GameProcess(this);
        this.process.run();
    }

    /**
     * The method where the GameSettings need to be customized and the AssetManager must have its assets defined & loads them<br/>
     * <i>Processing context doesn't exist at this point. Use setup() for tinkering with processing</i>
     * @return The settings of the game
     * @see Game#setup()
     */
    public abstract GameSettings startup();


    /**
     * Method callback for when processing runs setup(). This executes <b>before</b> the GameProcess does anything!<br/>
     * Access the PApplet/GameProcess through Game#process
     * @see Game#process
     * @see processing.core.PApplet
     * @see GameProcess
     */
    public void setup() { }

    /**
     * The method where the scenes of the game are put in order with game objects and their components.<br/>
     * <i>Note: if OOP is better for you or the scenes are too large, Scene can be extended for each scene of the game,
     * populated with GameObjects and their components and given a separate file</i>
     * @return An array of all the scenes of the game, in order
     */
    public abstract Scene[] buildScenes();
}
