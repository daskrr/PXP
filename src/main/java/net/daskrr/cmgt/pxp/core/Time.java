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
     * Retrieves the time in milliseconds from Processing since the application has started
     * @return the time in milliseconds
     */
    public static float getMillis() {
        return GameProcess.getInstance().millis();
    }

    /**
     * Retrieves and calculates the time in seconds from Processing since the application has started
     * @return the time in seconds
     */
    public static float get() {
        return GameProcess.getInstance().millis() / 1000f;
    }

    /**
     * Calculates delta time for new frame
     */
    protected static void newFrame() {
        // calculate delta time
        deltaTime = GameProcess.getInstance().millis() / 1000f - lastTime;
        lastTime = GameProcess.getInstance().millis() / 1000f;
    }
}
