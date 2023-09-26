package net.daskrr.cmgt.pxp.core;

import net.daskrr.cmgt.pxp.core.component.Camera;
import net.daskrr.cmgt.pxp.data.GameObjectSupplier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Scene
{
    private final GameObjectSupplier[] gameObjectSuppliers;

    public final List<GameObject> objects = new ArrayList<>();

    public final List<List<GameObject>> objectsByLayer = new ArrayList<>();

    private Camera mainCam;

    /**
     * Creates a new scene to be used in the game
     * @param gameObjectSuppliers The GameObject (preferably lambda) suppliers
     */
    public Scene(GameObjectSupplier[] gameObjectSuppliers) {
        this.gameObjectSuppliers = gameObjectSuppliers;
    }

    public void load() {
        for (GameObjectSupplier supplier : gameObjectSuppliers) {
            GameObject go = supplier.get();
            objects.add(go);

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

    public Camera getCamera() throws Exception {
        if (mainCam != null)
            return mainCam;

        for (GameObject object : objects) {
            mainCam = object.getComponentOfType(Camera.class);
            if (mainCam != null)
                return mainCam;
        }

        throw new Exception("The scene doesn't contain a camera!");
    }

    public void addGameObject(GameObject gameObject) {
        objects.add(gameObject);

        registerSortingLayer(gameObject);

        gameObject.load();
    }

    public GameObject getGameObject(String name) {
        for (GameObject go : objects)
            if (go.name.equals(name))
                return go;

        return null;
    }

    public void removeGameObject(GameObject gameObject) {
        for (List<GameObject> layer : objectsByLayer)
            layer.remove(gameObject);

        objects.remove(gameObject);
    }

    public void destroy() {
        for (GameObject go : objects)
            go.destroy();

        objects.clear();
        objectsByLayer.clear();
    }
}
