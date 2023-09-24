package net.daskrr.cmgt.pxp.core;

import net.daskrr.cmgt.pxp.core.GameProcess;
import net.daskrr.cmgt.pxp.data.GameSettings;

public abstract class Game
{
    public final GameProcess process;

    public Game() {
        GameSettings settings = this.setup();
        Scene[] scenes = this.buildScenes();

        // create GameProcess
        this.process = new GameProcess(settings, scenes);
        this.process.run();
    }

    public abstract GameSettings setup();

    public abstract Scene[] buildScenes();
}
