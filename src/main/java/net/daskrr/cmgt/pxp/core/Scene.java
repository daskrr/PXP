package net.daskrr.cmgt.pxp.core;

import java.util.*;

public class Scene
{
    public final List<GameObject> objects;

    public final List<List<GameObject>> objectsByLayer = new ArrayList<>();

    public Scene(GameObject[] gameObjects) {
        this.objects = new ArrayList<>(List.of(gameObjects));
    }

    public void load() {
        for (GameObject go : objects) {
            // arrange game objects into sorting layers
            registerSortingLayer(go);

            // give scene context to go
            go.scene = this;

            go.load();
        }
    }

    private void registerSortingLayer(GameObject gameObject) {
        if (gameObject.renderer == null)
            return;

        int sortingLayerId = GameProcess.getInstance().settings.sortingLayers.indexOf(gameObject.renderer.sortingLayer);

        if (objectsByLayer.size() <= sortingLayerId)
            objectsByLayer.add(sortingLayerId, new ArrayList<>() {{ add(gameObject); }});
        else if (objectsByLayer.get(sortingLayerId) == null)
            objectsByLayer.set(sortingLayerId, new ArrayList<>() {{ add(gameObject); }});
        else
            objectsByLayer.get(sortingLayerId).add(gameObject);

        // sort layer
        objectsByLayer.get(sortingLayerId).sort(new Comparator<GameObject>() {
            @Override
            public int compare(GameObject o1, GameObject o2) {
                return o1.renderer.orderInLayer - o2.renderer.orderInLayer;
            }
        });
    }

    public void addGameObject(GameObject gameObject) {
        objects.add(gameObject);

        registerSortingLayer(gameObject);

        gameObject.load();
    }

    public void removeGameObject(GameObject gameObject) {
        for (List<GameObject> layer : objectsByLayer)
            layer.remove(gameObject);

        objects.remove(gameObject);
    }

    public void destroy() {
        for (GameObject go : objects)
            go.destroy();
    }
}
