package net.daskrr.cmgt.pxp.core;

import net.daskrr.cmgt.pxp.core.component.Component;
import net.daskrr.cmgt.pxp.core.component.Renderer;
import net.daskrr.cmgt.pxp.data.ComponentSupplier;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class GameObject
{
    public final String name;
    public final List<Component> components = new ArrayList<>();

    public Transform transform = new Transform();
    public Scene scene;

    public Renderer renderer;

    public GameObject(String name) {
        this(name, new ComponentSupplier[0]);
    }
    public GameObject(String name, ComponentSupplier[] componentSuppliers) {
        this.name = name;
        for (ComponentSupplier supplier : componentSuppliers) {
            Component c = supplier.get();
            components.add(c);

            c.gameObject = this;
            this.findRenderer(c);

            c.awake();
        }
    }

    protected void load() {
        // call start methods
        for (Component c : components)
            c.start();
    }

    protected void draw() {
        // bind transform
        transform.bind();
        // call start methods
        for (Component c : components)
            c.update();

        transform.unbind();
    }

    public void addComponent(Component c) {
        this.components.add(c);
        c.gameObject = this;
        this.findRenderer(c);

        c.awake();
        c.start();
    }
    public <T extends Component> T getComponentOfType(Class<T> type) {
        for (Component c : components)
            if (c.getClass().equals(type))
                return (T) c;

        return null;
    }
    public <T extends Component> T[] getComponentsOfType(Class<T> type) {
        List<T> components = new ArrayList<>();
        for (Component c : this.components)
            if (c.getClass().equals(type))
                components.add((T) c);

        return components.toArray((T[]) Array.newInstance(type, components.size()));
    }
    public void removeComponent(Component c) {
        components.remove(c);
        c.destroy();
    }
    public <T extends Component> void removeComponent(Class<T> type) {
        // prevent ConcurrentModificationException
        new ArrayList<>(components).forEach(c -> {
            if (c.getClass().equals(type))
                components.remove(c);
        });
    }

    private void findRenderer(Component c) {
        if (c instanceof Renderer)
            this.renderer = (Renderer) c;
    }

    public void destroy() {
        for (Component c : components)
            c.destroy();

        scene.removeGameObject(this);
    }
}
