package pxp.engine.core;

/**
 * A routine is an action that will be performed later or at an interval
 */
public class Routine {
    protected int id;

    public float initialTime = 0f;
    public float time = 0f;
    public Runnable run;
    protected boolean interval = false;

    private Runnable remove;

    protected Routine(int gameObjectId, float time, Runnable run) {
        this.id = gameObjectId;
        this.time = time;
        this.initialTime = time;
        this.run = run;
    }

    /**
     * Sets the removal callback for this routine
     */
    protected void setRemove(Runnable callback) {
        this.remove = callback;
    }

    protected void step() {
        time -= Time.deltaTime;

        // no more time left to wait, execute the routine
        if (time <= 0f) {
            this.run.run();

            // if its interval we repeat
            if (interval)
                time = initialTime;
            else
                // else we remove the routine
                this.remove.run();
        }
    }

    /**
     * Stops the routine (without the possibility to restart it!)
     */
    public void stop() {
        this.remove.run();
    }
}
