package pxp.engine.core.component;

import pxp.engine.core.GameObject;
import pxp.engine.core.GameProcess;
import pxp.engine.core.Transform;
import pxp.engine.core.RectTransform;
import processing.core.PConstants;
import pxp.engine.data.collision.Collision;

/**
 * Base for the components
 */
public class Component implements PConstants
{
    /**
     * The parent GameObject of this component<br/><br/>
     * <i>Changing this will break functionality!</i>
     */
    public GameObject gameObject;

    /**
     * Whether this component was started<br/>
     * Changing this will break functionality!
     */
    public boolean started = false;

    /**
     * Whether to draw gizmos for this component
     */
    public boolean drawGizmos = false;


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

    /**
     * Called after render, only in SceneView (with the Editor) or in the game itself if enabled
     */
    public void gizmosDraw() { }

    /**
     * Called when a collision first happens
     * @param collision the collision
     */
    public void collisionEnter(Collision collision) { }
    /**
     * Called every frame when the object is colliding with another one
     * @param collision the collision
     */
    public void collisionStay(Collision collision) { }
    /**
     * Called after the last frame of collision
     * @param collision the collision
     */
    public void collisionExit(Collision collision) { }

    /**
     * Called when a trigger collision first happens
     * @param collider the other collider
     */
    public void triggerEnter(Collider collider) { }
    /**
     * Called every frame when the object is trigger colliding with another one
     * @param collider the other collider
     */
    public void triggerStay(Collider collider) { }
    /**
     * Called after the last frame of the trigger collision
     * @param collider the other collider
     */
    public void triggerExit(Collider collider) { }


    // short method names for ease of use

    /**
     * Gets the context ({@link GameProcess} [{@link processing.core.PApplet}]) of the game for easy usage of the Processing methods<br/>
     * <br/><b>THIS SHOULD NOT BE USED TOWARDS THE PURPOSE OF RENDERING!</b><br/>
     * <i>Shorthand for: {@link Component#context()}</i>
     */
    public final GameProcess ctx() {
        return context();
    }
    /**
     * Gets the context ({@link GameProcess} [{@link processing.core.PApplet}]) of the game for easy usage of the Processing methods<br/>
     * <br/><b>THIS SHOULD NOT BE USED TOWARDS THE PURPOSE OF RENDERING!</b>
     */
    public final GameProcess context() {
        if (gameObject != null && gameObject.scene != null)
            return gameObject.scene.context;

        // fallback
        return GameProcess.getInstance();
    }

    // Deprecated & removed as ctx() is now interchangeable (due to step method synchronization)
    /*
    *   /**
    *    * Gets the render context (GameProcess [PApplet]) of the game for usage only withing the rendering methods
    *     /
    *   public final GameProcess renderContext() {
    *       if (gameObject.scene.handlingProcess == null)
    *          return ctx();

    *       return gameObject.scene.handlingProcess;
    *   }
    *   /**
    *    * Gets the render context (GameProcess [PApplet]) of the game for usage only withing the rendering methods<br/>
    *    * <i>Shorthand for: {@link Component#renderContext()}</i>
    *     /
    *   public final GameProcess rctx() {
    *       return renderContext();
    *   }
    */

    /**
     * Shorthand method for <i>gameObject.transform</i>
     * @return the Transform of the parent GameObject
     */
    public final Transform transform() {
        return gameObject.transform;
    }

    /**
     * Shorthand method for <i>gameObject.transform</i>, with the transform cast to {@link RectTransform}.<br/>
     * <i>If the transform is not a RectTransform, this might throw a cast exception!</i>
     * @return the RectTransform of the parent GameObject
     */
    public final RectTransform rectTransform() {
        return (RectTransform) gameObject.transform;
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
    public final void instantiate(GameObject gameObject) {
        ctx().getCurrentScene().addGameObject(gameObject);
    }

    /**
     * Instantiates a GameObject into the current Scene, as a child of this component's game object
     * @param gameObject the GameObject to be placed in the Scene
     */
    public final void instantiateChild(GameObject gameObject) {
        ctx().getCurrentScene().addGameObject(gameObject, this.gameObject);
    }

    /**
     * Instantiates a GameObject into the current Scene, as a child of the specified game object
     * @param gameObject the GameObject to be placed in the Scene
     * @param parent the parent to father the child
     */
    public final void instantiate(GameObject gameObject, GameObject parent) {
        ctx().getCurrentScene().addGameObject(gameObject, parent);
    }
    /**
     * Instantiates a GameObject into the current Scene, as a child of the specified game object
     * @param gameObject the GameObject to be placed in the Scene
     * @param parentName the parent's name under which the newly instantiated game object will exist
     */
    public final void instantiate(GameObject gameObject, String parentName) {
        ctx().getCurrentScene().addGameObject(gameObject, parentName);
    }
}
