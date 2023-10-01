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
    public Transform transform = new Transform();
    /**
     * Reference to the Scene this GameObject is part of
     */
    public Scene scene;

    /**
     * [Internal] The renderer of the GameObject
     */
    public Renderer renderer;

    /**
     * Creates a GameObject, given a unique name
     * @param name the unique name of the object
     */
    public GameObject(String name) {
        this(name, new Component[0]);
    }

    /**
     * Creates a GameObject, given a unique name and its components
     * @param name the unique name of the object
     * @param components the components of the object
     */
    public GameObject(String name, Component[] components) {
        this.name = name;
        for (Component c : components) {
            this.components.add(c);

            c.gameObject = this;
            this.findRenderer(c);

            c.awake();
        }
    }

    /**
     * Called when the objects are loaded, starts the components
     */
    protected void load() {
        // call start methods
        components.forEach(Component::start);
    }

    /**
     * Game step event, runs every frame
     */
    protected void draw() {
        // bind transform
        transform.bind();
        // call update methods
        components.forEach(Component::update);

        transform.unbind();
    }

    /**
     * Dynamically adds a component to this game object<br/>
     * <i>Note: This component will not persist past the termination of the game process and will be awakened immediately</i>
     * @param component the component to be added
     */
    public void addComponent(Component component) {
        this.components.add(component);
        component.gameObject = this;
        this.findRenderer(component);

        component.awake();
        component.start();
    }

    /**
     * Looks for a component of a given type
     * @param type the type of the component to get
     * @return the first component of the specified type or null
     */
    public <T extends Component> T getComponentOfType(Class<T> type) {
        for (Component c : components)
            if (c.getClass().equals(type))
                return (T) c;

        return null;
    }
    /**
     * Looks for components of a given type
     * @param type the type of the component to get
     * @return the components of the specified type or an empty Array
     */
    public <T extends Component> T[] getComponentsOfType(Class<T> type) {
        List<T> components = new ArrayList<>();
        for (Component c : this.components)
            if (c.getClass().equals(type))
                components.add((T) c);

        return components.toArray((T[]) Array.newInstance(type, components.size()));
    }

    /**
     * Removes and destroys a component
     * @param component the component to remove
     */
    public void removeComponent(Component component) {
        components.remove(component);
        component.destroy();
    }
    /**
     * Removes and destroys the first occurrence of a component of the specified type
     * @param type the component type to remove
     */
    public <T extends Component> void removeComponent(Class<T> type) {
        // prevent ConcurrentModificationException
        new ArrayList<>(components).forEach(c -> {
            if (c.getClass().equals(type))
                components.remove(c);
        });
    }

    /**
     * Checks if the component is a Renderer
     * @param c the component to check
     * @see Renderer
     */
    private void findRenderer(Component c) {
        if (c instanceof Renderer)
            this.renderer = (Renderer) c;
    }

    /**
     * Destroys this game object, its components and removes it from the scene<br/>
     * <b>This cannot be reversed!</b>
     */
    public void destroy() {
        components.forEach(Component::destroy);

        scene.removeGameObject(this);
    }
}
