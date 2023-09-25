package net.daskrr.cmgt.pxp.core;

public class Time
{
    protected static int lastTime = 0;
    public static int deltaTime = 0;

    protected static void newFrame() {
        // calculate delta time
        deltaTime = GameProcess.getInstance().millis() - lastTime;
        lastTime = GameProcess.getInstance().millis();
    }
}
