package net.daskrr.cmgt.pxp.core.component;

import net.daskrr.cmgt.pxp.core.GameObject;
import net.daskrr.cmgt.pxp.core.GameProcess;
import processing.core.PConstants;

public class Component implements PConstants
{
    public GameObject gameObject;

    public Component() {  }

    /**
     * Called when the component is added to the GameObject
     */
    public void awake() { }
    /**
     * Called when the scene is loaded or right after awake if component is added dynamically
     */
    public void start() { }

    /**
     * Called every frame
     */
    public void update() { }

    /**
     * Called when the game object is destroyed or when the component is removed dynamically
     */
    public void destroy() { }

    // short method name for ease of use

    /**
     * Gets the context (GameProcess [PApplet]) of the game for easy usage of the Processing methods
     */
    public final GameProcess ctx() {
        return GameProcess.getInstance();
    }

    public final void Instantiate(GameObject gameObject) {
        ctx().getCurrentScene().addGameObject(gameObject);
    }
}
