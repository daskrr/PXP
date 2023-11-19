package pxp.engine.core;

import pxp.engine.core.component.Collider;
import pxp.engine.core.component.Component;
import pxp.engine.core.component.pointer.*;
import pxp.engine.core.component.Renderer;
import pxp.engine.core.manager.CollisionManager;
import pxp.engine.data.CollisionEvent;
import pxp.engine.data.TriggerCollisionEvent;
import pxp.engine.data.Vector2;
import processing.event.MouseEvent;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * The building block of PXP and the game, the GameObject holds Components that give it functionality and data about its
 * position, rotation and scale in the game
 */
public class GameObject
{
    /**
     * The unique (scene-wide) name of the GameObject
     */
    public final String name;
    /**
     * The components of the GameObject
     */
    private final List<Component> components = new ArrayList<>();

    /**
     * The transform of this GameObject, containing position, rotation and scale
     */
    public Transform transform;
    /**
     * Reference to the Scene this GameObject is part of
     */
    public Scene scene;

    /**
     * [Internal] The renderer of the GameObject
     */
    public Renderer renderer;

    /**
     * [Internal] The colliders of the GameObject
     */
    public List<Collider> colliders = new ArrayList<>();

    /**
     * The parent of this game object, null if this is at scene level<br/>
     * <b>DO NOT REASSIGN THIS MANUALLY!</b>
     */
    public GameObject parent = null;

    /**
     * The children of this game object<br/>
     * <b>Do not modify directly!</b>
     */
    public List<GameObject> children = new ArrayList<>();

    /**
     * Whether this game object was loaded
     */
    private boolean isLoaded = false;

    /**
     * Whether the game object is active (runs the update loop for components & renders)<br/>
     * <i>Set this to false if you don't want to destroy an object, but wish to make it dormant</i>
     */
    public boolean isActive = true;

    /**
     * Whether this game object is destroyed
     */
    public boolean isDestroyed = false;

    /**
     * Whether this object will persist after its scene is destroyed
     */
    protected boolean persistent = false;

    /**
     * Creates a GameObject, given a unique name
     * @param name the unique name of the object
     */
    public GameObject(String name) {
        this(name, new Component[0]);
    }

    /**
     * Creates a GameObject, given a unique name and a set of children
     * @param name the unique name of the object
     */
    public GameObject(String name, GameObject[] children) {
        this(name, new Component[0], children);
    }

    /**
     * Creates a GameObject, given a unique name and its components
     * @param name the unique name of the object
     * @param components the components of the object
     */
    public GameObject(String name, Component[] components) {
        this(name, components, new GameObject[0]);
    }

    /**
     * Creates a GameObject, given a unique name, its components and a set of children
     * @param name the unique name of the object
     * @param components the components of the object
     */
    public GameObject(String name, Component[] components, GameObject[] children) {
        this.transform = new Transform() {{ gameObject = GameObject.this; }};

        this.name = name;
        for (Component c : components) {
            this.components.add(c);

            c.gameObject = this;
            this.registerRenderer(c, false);

            c.awake();
        }

        this.children = new ArrayList<>(List.of(children));
        this.children.forEach(go -> go.parent = this);
    }

    /**
     * Called when the objects are loaded, starts the components
     */
    protected void load() {
        if (this.isDestroyed) return;

        // set is loaded now to allow usage of methods by the components
        // isLoaded protects mainly against incorrect usage of block initializers with methods that can't yet run
        this.isLoaded = true;

        // set the transform's gameObject field
        this.transform.gameObject = this;

        // call start methods
        this.components.forEach(c -> {
            c.start();
            c.started = true;
        });
        this.registerCoreComponents();
        this.children.forEach(go -> {
            go.scene = this.scene; // and set scene context
            go.load();
            scene.registerSortingLayer(go);
        });
    }

    /**
     * Game step event, runs every frame
     */
    protected void update() {
        if (!isLoaded) return;
        if (this.isDestroyed) return;
        if (!this.isActive) return;

        // set last position
        this.transform.lastPosition = this.transform.position.clone();

        // bind transform
        this.transform.bind(scene.context);
        // call update methods
        this.components.forEach(Component::update);

        // call draw methods for children
        this.children.forEach(GameObject::update);

        this.transform.unbind();
    }

    /**
     * Invokes the gizmosDraw of all components and children
     */
    protected void gizmosDraw() {
        this.components.forEach((c) -> {
            if (c.drawGizmos || this.scene.context.forceDrawGizmos) {
                c.gizmosDraw();
            }
        });
        this.children.forEach(GameObject::gizmosDraw);
    }

    /**
     * Adds a game object as a child of this game object
     * @param object the object to add
     */
    protected void addGameObject(GameObject object) {
        GameProcess.nextFrame(() -> {
            this.children.add(object);
        });
    }
    /**
     * Retrieves a game object by its name at this game object's level
     * @param name the unique name of the object
     * @return the game object or null
     */
    public GameObject getGameObject(String name) {
        for (GameObject child : this.children)
            if (child.name.equals(name))
                return child;

        return null;
    }
    /**
     * Retrieves a game object by its name looking through all the children and their children and so on...
     * @param name the unique name of the object
     * @return the game object or null
     */
    public GameObject getGameObjectDeep(String name) {
        for (GameObject child : this.children) {
            if (child.name.equals(name))
                return child;
            GameObject search = child.getGameObjectDeep(name);
            if (search != null)
                return search;
        }

        return null;
    }

    /**
     * Retrieves all children of all children ... from this game object
     * @return the children
     */
    public List<GameObject> getChildrenDeep() {
        List<GameObject> children = new ArrayList<>();
        for (GameObject child : this.children) {
            children.add(child);
            children.addAll(child.getChildrenDeep());
        }

        return children;
    }
    /**
     * Removes and destroys a game object child of this game object's by its name<br/>
     * <i>If the game object doesn't exist, it will not be destroyed!</i>
     * @param name the unique name of the object to remove & destroy
     */
    protected void removeGameObject(String name) {
        GameObject go = this.getGameObject(name);
        if (go == null) return;

        this.removeGameObject(go);
    }
    /**
     * Removes and destroys a game object child of this game object's<br/>
     * <i>If the game object doesn't exist, it will not be destroyed!</i>
     * @param object the object to remove & destroy
     */
    public void removeGameObject(GameObject object) {
        if (!this.isLoaded) throw new RuntimeException("This GameObject was not yet loaded!");

        GameProcess.nextFrame(() -> {
            if (this.children.remove(object)) {
                this.scene.removeGameObject(object);
                object.destroy();
            }
        });
    }

    /**
     * Dynamically adds a component to this game object<br/>
     * <i>Note: This component will not persist past the termination of the game process and will be awakened immediately</i>
     * @param component the component to be added
     */
    public void addComponent(Component component) {
        if (this.isDestroyed) throw new RuntimeException("This GameObject is destroyed!");
        if (!this.isLoaded) throw new RuntimeException("This GameObject was not yet loaded!");

        GameProcess.nextFrame(() -> {
            this.components.add(component);
            component.gameObject = this;
            this.findCoreComponents(component, true);

            component.awake();
            component.start();
        });
    }

    /**
     * Looks for a component of a given type
     * @param type the type of the component to get
     * @return the first component of the specified type or null
     */
    public <T extends Component> T getComponentOfType(Class<T> type) {
        if (this.isDestroyed) throw new RuntimeException("This GameObject is destroyed!");
        if (!this.isLoaded) throw new RuntimeException("This GameObject was not yet loaded!");

        for (Component c : components)
            if (type.isInstance(c))
                return (T) c;

        return null;
    }
    /**
     * Looks for components of a given type
     * @param type the type of the component to get
     * @return the components of the specified type or an empty Array
     */
    public <T extends Component> T[] getComponentsOfType(Class<T> type) {
        if (this.isDestroyed) throw new RuntimeException("This GameObject is destroyed!");
        if (!this.isLoaded) throw new RuntimeException("This GameObject was not yet loaded!");

        List<T> components = new ArrayList<>();
        for (Component c : this.components)
            if (type.isInstance(c))
                components.add((T) c);

        return components.toArray((T[]) Array.newInstance(type, components.size()));
    }

    /**
     * Removes and destroys a component
     * @param component the component to remove
     */
    public void removeComponent(Component component) {
        if (this.isDestroyed) throw new RuntimeException("This GameObject is destroyed!");
        if (!this.isLoaded) throw new RuntimeException("This GameObject was not yet loaded!");

        GameProcess.nextFrame(() -> {
            components.remove(component);
            unregisterCoreComponents(component);
            component.destroy();
        });
    }
    /**
     * Removes and destroys the first occurrence of a component of the specified type
     * @param type the component type to remove
     */
    public <T extends Component> void removeComponent(Class<T> type) {
        if (this.isDestroyed) throw new RuntimeException("This GameObject is destroyed!");
        if (!this.isLoaded) throw new RuntimeException("This GameObject was not yet loaded!");

        // prevent ConcurrentModificationException
        GameProcess.nextFrame(() ->
            new ArrayList<>(components).forEach(c -> {
                if (type.isInstance(c)) {
                    components.remove(c);
                    unregisterCoreComponents(c);
                    c.destroy();
                }
            }
        ));
    }

    /**
     * Checks if the component is a Renderer and registers the sorting layer of the Renderer
     * @param c the component to check
     * @param registerSortingLayer whether to register the sorting layer
     */
    private void registerRenderer(Component c, boolean registerSortingLayer) {
        if (c instanceof Renderer r) {
            this.renderer = r;
            if (registerSortingLayer)
                this.scene.registerSortingLayer(this);
        }
    }

    /**
     * Loops through all components and registers colliders, updating the {@link CollisionManager}
     */
    private void registerCoreComponents() {
        this.components.forEach((c) -> {
            if (c instanceof Collider col) {
                this.colliders.add(col);
                this.scene.context.collisionManager.register(this);
            }
        });
    }

    /**
     * Checks if the component is a Renderer or a Collider and removes them, as well as updating the CollisionManager
     * @param c the component to check
     */
    private void unregisterCoreComponents(Component c) {
        if (c instanceof Renderer && this.renderer.equals(c))
            this.renderer = null;
        else if (c instanceof Collider col) {
            this.colliders.remove(col);

            if (this.colliders.size() == 0)
                this.scene.context.collisionManager.unregister(this);
        }

    }

    // ========================== EVENTS ==========================

    /**
     * [Internal] Propagates a collision event throughout components
     * @param collisionEvent the collision to propagate
     */
    public void propagateCollisionEvent(CollisionEvent collisionEvent) {
        if (collisionEvent instanceof TriggerCollisionEvent) {
            this.components.forEach((c) -> {
                switch (collisionEvent.eventTime) {
                    case ENTER -> c.triggerEnter(collisionEvent.collision.otherCollider);
                    case STAY -> c.triggerStay(collisionEvent.collision.otherCollider);
                    case EXIT -> c.triggerExit(collisionEvent.collision.otherCollider);
                }

            });
        } else {
            this.components.forEach((c) -> {
                switch (collisionEvent.eventTime) {
                    case ENTER -> c.collisionEnter(collisionEvent.collision);
                    case STAY -> c.collisionStay(collisionEvent.collision);
                    case EXIT -> c.collisionExit(collisionEvent.collision);
                }

            });
        }

    }

    /**
     * Propagates a mouse event throughout components and children
     * @param event the event to propagate
     */
    protected void propagateMouseEvent(MouseEvent event) {
        for (Component c : this.components) {
            if (!(c instanceof PointerHandlerBase ph)) continue;

            Vector2 mousePos = new Vector2(event.getX(), event.getY());
            if (ph.checkOverlap(mousePos.clone())) {
                switch (event.getAction()) {
                    case MouseEvent.MOVE -> {
                        if (ph instanceof PointerHandlerHover phh)
                            // continuous hover event
                            phh.mouseHover(event);

                        if (ph instanceof PointerHandlerMouse phm)
                            // one time enter event
                            if (!phm.isHovering()) {
                                if (ph instanceof PointerHandlerHover phh)
                                    phh.mouseHoverEnter(event);

                                phm.setHovering(true);
                            }
                    }
                    case MouseEvent.CLICK -> {
                        if (ph instanceof PointerHandlerMouse phm)
                            phm.mouseClick(event);
                    }
                    case MouseEvent.WHEEL -> {
                        if (ph instanceof PointerHandlerMouse phm)
                            phm.mouseScroll(event);
                    }
                    case MouseEvent.PRESS -> {
                        if (ph instanceof PointerHandlerMouse phm)
                            phm.mouseDown(event);
                    }
                    case MouseEvent.RELEASE -> {
                        if (ph instanceof PointerHandlerMouse phm)
                            phm.mouseUp(event);
                    }
                }
            }
            else if (event.getAction() == MouseEvent.MOVE && ph.isHovering()) {
                // hover exit
                if (ph instanceof PointerHandlerHover phh)
                    phh.mouseHoverExit(event);

                ph.setHovering(false);
            }

            // dragging (separated for readability & some functionality outside of overlap)
            // drag start & continuous
            if (event.getAction() == MouseEvent.DRAG && ph instanceof PointerHandlerDrag phd) {
                // check if overlap & start dragging
                if (phd.checkOverlap(mousePos.clone()))
                    // only if we are not already dragging, we set the state to true and fire the start drag
                    if (!phd.isDragging()) {
                        phd.setDragging(true);
                        phd.mouseDragStart(event);
                    }

                // call mouse drag event if dragging
                if (phd.isDragging())
                    phd.mouseDrag(event);
            }
            // handle drag stop
            if (event.getAction() == MouseEvent.RELEASE && ph instanceof PointerHandlerDrag phd) {
                // release the drag if we were dragging
                if (phd.isDragging()) {
                    phd.setDragging(false);
                    phd.mouseDragStop(event);
                }
            }
        }

        this.children.forEach(child -> {
            if (!child.isDestroyed && child.isActive)
                child.propagateMouseEvent(event);
        });
    }

    /**
     * Checks if the component is a Renderer or a Collider and registers the sorting layer of the Renderer
     * @param c the component to check
     * @see Renderer
     * @see Collider
     */
    private void findCoreComponents(Component c, boolean register) {
        if (c instanceof Renderer) {
            this.renderer = (Renderer) c;

            if (register)
                scene.registerSortingLayer(this);
        }
        else if (c instanceof Collider col)
            this.colliders.add(col);
    }

    /**
     * Creates a routine that runs every x seconds
     * @param time the time interval between executions (in seconds)
     * @param runnable the action to perform
     * @return the routine created
     */
    public Routine runInterval(float time, Runnable runnable) {
        return scene.context.runInterval(this, time, runnable);
    }

    /**
     * Creates a routine that runs after a specified amount of time
     * @param time the amount of time after which this routine should be executed (in seconds)
     * @param runnable the action to perform
     * @return the routine created
     */
    public Routine runLater(float time, Runnable runnable) {
        return scene.context.runLater(this, time, runnable);
    }

    /**
     * Sets this game object as persistent. This object will not be destroyed when a scene change happens and will be added
     * to the scene that is loaded next<br/>
     * <i>This can only be used in methods executing after or during start()</i>
     * @param persistent whether the object is persistent
     */
    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
        if (persistent)
            scene.context.persistent.add(this);
        else
            scene.context.persistent.remove(this);
    }

    /**
     * Sets the scene of this game object as well as all its children and their children ...
     * @param scene the new scene
     */
    protected void setScene(Scene scene) {
        this.scene = scene;
        for (GameObject child : this.children)
            child.setScene(scene);
    }

    /**
     * Destroys this game object, its components and removes it from the scene<br/>
     * <b>This cannot be reversed!</b>
     */
    public void destroy() {
        if (this.isDestroyed) throw new RuntimeException("This GameObject is destroyed already!");

        GameProcess.nextFrame(() -> {
            this.components.forEach(Component::destroy);
            this.children.forEach(GameObject::destroy);

            this.scene.removeGameObject(this);

            this.isDestroyed = true;
        });
    }

    @Override
    public String toString() {
        return "GameObject [" + this.name + "]";
    }

    public String toStringDebug() {
        return "GameObject [" + this.name + "] @ " + hashCode();
    }
}
