package pxp.engine.core;

import java.util.HashMap;
import java.util.Map;

public class Time
{
    /**
     * All the time instances
     */
    private static final Map<Integer, Time> timeInstances = new HashMap<>();

    /**
     * Get the main time instance
     */
    public static Time getDefault() {
        return timeInstances.get(GameProcess.getInstance().hashCode());
    }

    /**
     * Gets the instance of time assigned to the specified game process
     * @param process the game process
     * @return the Time instance or null
     */
    public static Time get(GameProcess process) {
        return timeInstances.get(process.hashCode());
    }

    /**
     * Creates the default instance
     */
    protected static void createDefault() {
        new Time(GameProcess.getInstance());
    }

    /**
     * The context for this time instance
     */
    private final GameProcess ctx;

    /**
     * The amount of frames that were generated from the beginning of the game
     */
    public int _frameCount = 0;
    /**
     * Last Frame Time in seconds
     */
    protected float _lastTime = 0f;
    /**
     * Delta Time in seconds (time passed between the previous frame and the current)
     */
    public float _deltaTime = 0f;

    /**
     * [Internal] The number to multiply deltaTime with.<br/>
     * This can slow down or speed up everything that uses delta time.
     */
    public float _timeScale = 1.0F;

    private Time(GameProcess ctx) {
        this.ctx = ctx;
        timeInstances.put(ctx.hashCode(), this);
    }

    /**
     * Retrieves the time in milliseconds from Processing since the application has started
     * @return the time in milliseconds
     */
    public float _getMillis() {
        return ctx.millis();
    }
    /**
     * Retrieves and calculates the time in seconds from Processing since the application has started
     * @return the time in seconds
     */
    public float _get() {
        return ctx.millis() / 1000f;
    }

    /**
     * [Internal] Sets the timeScale of this Time instance.
     * @param scale the scale (0f-1f slows down, 1f-inf speeds up)
     */
    public void _setTimeScale(float scale) {
        this._timeScale = scale;
    }

    /**
     * Calculates delta time for new frame
     */
    protected void _newFrame() {
        _frameCount++;

        // calculate delta time
        _deltaTime = ctx.millis() / 1000f - _lastTime;
        _lastTime = ctx.millis() / 1000f;

        // check if this instance is the default instance
        if (timeInstances.containsKey(ctx.hashCode())) {
            frameCount = this._frameCount;
            lastTime = this._lastTime;
            deltaTime = this._deltaTime;
        }
    }


    /**
     * The amount of frames that were generated from the beginning of the game
     */
    public static int frameCount = 0;
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
     * The number to multiply deltaTime with.<br/>
     * This can slow down or speed up everything that uses delta time.
     */
    public static float getTimeScale() {
        return getDefault()._timeScale;
    }
    /**
     * Sets the timeScale of this Time instance.
     * @param scale the scale (0-1 slows down, 1-inf speeds up)
     */
    public static void setTimeScale(float scale) {
        getDefault()._setTimeScale(scale);
    }

    /**
     * Calculates delta time for new frame
     */
    protected static void newFrame() {
        getDefault()._newFrame();
    }
}
