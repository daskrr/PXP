package net.daskrr.cmgt.pxp.core;

import net.daskrr.cmgt.pxp.core.component.Component;
import net.daskrr.cmgt.pxp.core.component.SpriteRenderer;

import java.util.ArrayList;
import java.util.List;

public class GameObject
{
    public final String name;
    public final List<Component> components;

    public Transform transform = new Transform();
    public Scene scene;

    protected SpriteRenderer renderer;

    public GameObject(String name) {
        this(name, new Component[0]);
    }
    public GameObject(String name, Component[] components) {
        this.name = name;
        this.components = new ArrayList<>(List.of(components));
        initComponents();
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

    private void initComponents() {
        for (Component c : components) {
            c.gameObject = this;
            findRenderer(c);

            c.awake();
        }
    }
    private void findRenderer(Component c) {
        if (c instanceof SpriteRenderer)
            this.renderer = (SpriteRenderer) c;
    }

    public void destroy() {
        for (Component c : components)
            c.destroy();

        scene.removeGameObject(this);
    }
}
