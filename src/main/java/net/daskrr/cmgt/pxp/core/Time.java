package net.daskrr.cmgt.pxp.core;

public class Time
{
    /**
     * Last Frame Time in seconds
     */
    protected static float lastTime = 0f;
    /**
     * Delta Time in seconds (time passed between the previous frame and the current)
     */
    public static float deltaTime = 0f;

    /**
     * Calculates delta time for new frame
     */
    protected static void newFrame() {
        // calculate delta time
        deltaTime = GameProcess.getInstance().millis() / 1000f - lastTime;
        lastTime = GameProcess.getInstance().millis() / 1000f;
    }
}
