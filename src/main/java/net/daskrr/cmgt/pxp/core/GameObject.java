package net.daskrr.cmgt.pxp.core;

import net.daskrr.cmgt.pxp.core.component.Component;
import net.daskrr.cmgt.pxp.core.component.Renderer;

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
    private boolean isDestroyed = false;

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
            this.findRenderer(c, false);

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
        this.components.forEach(Component::start);
        this.children.forEach(go -> {
            go.load();
            go.scene = this.scene; // and set scene context
            scene.registerSortingLayer(go);
        });
    }

    /**
     * Game step event, runs every frame
     */
    protected void draw() {
        if (!isLoaded) return;
        if (this.isDestroyed) return;
        if (!this.isActive) return;

        // bind transform
        this.transform.bind();
        // call update methods
        this.components.forEach(Component::update);

        // call draw methods for children
        this.children.forEach(GameObject::draw);

        this.transform.unbind();
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
            this.findRenderer(component, true);

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
                    c.destroy();
                }
            }
        ));
    }

    /**
     * Checks if the component is a Renderer
     * @param c the component to check
     * @see Renderer
     */
    private void findRenderer(Component c, boolean register) {
        if (c instanceof Renderer) {
            this.renderer = (Renderer) c;

            if (register)
                scene.registerSortingLayer(this);
        }
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
}
