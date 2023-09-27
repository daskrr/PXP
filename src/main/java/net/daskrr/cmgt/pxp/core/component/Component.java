package net.daskrr.cmgt.pxp.core.component;

import net.daskrr.cmgt.pxp.core.GameObject;
import net.daskrr.cmgt.pxp.core.GameProcess;
import net.daskrr.cmgt.pxp.core.Transform;
import processing.core.PConstants;

/**
 * Base for the components
 */
public class Component implements PConstants
{
    /**
     * The parent GameObject of this component
     */
    public GameObject gameObject;

    public Component() { }

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

    /**
     * Shorthand method for <i>gameObject.transform</i>
     * @return the Transform of the parent GameObject
     */
    public final Transform transform() {
        return gameObject.transform;
    }

    /**
     * Instantiates a GameObject into the current Scene
     * @param gameObject the GameObject to be placed in the Scene
     */
    public final void Instantiate(GameObject gameObject) {
        ctx().getCurrentScene().addGameObject(gameObject);
    }
}
