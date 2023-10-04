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
     * Looks for a component of a given type
     * @param type the type of the component to get
     * @return the first component of the specified type or null
     */
    public final <T extends Component> T getComponentOfType(Class<T> type) {
        return gameObject.getComponentOfType(type);
    }

    /**
     * Looks for components of a given type
     * @param type the type of the component to get
     * @return the components of the specified type or an empty Array
     */
    public final <T extends Component> T[] getComponentsOfType(Class<T> type) {
        return gameObject.getComponentsOfType(type);
    }

    /**
     * Instantiates a GameObject into the current Scene
     * @param gameObject the GameObject to be placed in the Scene
     */
    public final void Instantiate(GameObject gameObject) {
        ctx().getCurrentScene().addGameObject(gameObject);
    }

    /**
     * Instantiates a GameObject into the current Scene, as a child of this component's game object
     * @param gameObject the GameObject to be placed in the Scene
     */
    public final void InstantiateChild(GameObject gameObject) {
        ctx().getCurrentScene().addGameObject(gameObject, this.gameObject);
    }

    /**
     * Instantiates a GameObject into the current Scene, as a child of the specified game object
     * @param gameObject the GameObject to be placed in the Scene
     * @param parent the parent to father the child
     */
    public final void Instantiate(GameObject gameObject, GameObject parent) {
        ctx().getCurrentScene().addGameObject(gameObject, parent);
    }
    /**
     * Instantiates a GameObject into the current Scene, as a child of the specified game object
     * @param gameObject the GameObject to be placed in the Scene
     * @param parentName the parent's name under which the newly instantiated game object will exist
     */
    public final void Instantiate(GameObject gameObject, String parentName) {
        ctx().getCurrentScene().addGameObject(gameObject, parentName);
    }
}
