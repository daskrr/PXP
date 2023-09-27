package net.daskrr.cmgt.pxp.core;

import net.daskrr.cmgt.pxp.core.component.Camera;
import net.daskrr.cmgt.pxp.data.GameObjectSupplier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The Scene contains GameObjects and helper methods, only one scene can exist at once.
 */
public class Scene
{
    /**
     * The GameObjects' suppliers (for re-usability)
     */
    private final GameObjectSupplier[] gameObjectSuppliers;

    /**
     * The instantiated GameObjects of the scene (are only used when the scene is loaded & running)
     */
    public final List<GameObject> objects = new ArrayList<>();

    /**
     * The instantiated GameObjects sorted by sortingLayer
     */
    public final List<List<GameObject>> objectsByLayer = new ArrayList<>();

    /**
     * The main camera of the scene
     */
    private Camera mainCam;

    /**
     * Creates a new scene to be used in the game
     * @param gameObjectSuppliers The GameObject (preferably lambda) suppliers
     */
    public Scene(GameObjectSupplier[] gameObjectSuppliers) {
        this.gameObjectSuppliers = gameObjectSuppliers;
    }

    /**
     * Loads the scene into the game (along with its gameObjects and Components)
     */
    protected void load() {
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

    /**
     * Adds a GameObjects to the objectsByLayer map and sorts the map (in case a new layer was added)
     * @param gameObject the game object to register
     */
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

    /**
     * Gets the first Camera GameObject in the scene and establishes it as the main camera to be used for this scene
     * @return the camera game object
     * @throws Exception if there isn't a Camera GameObject present in the scene
     */
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

    /**
     * Dynamically adds a GameObject to the scene
     * @param gameObject the game object to add
     */
    public void addGameObject(GameObject gameObject) {
        objects.add(gameObject);

        registerSortingLayer(gameObject);

        gameObject.load();
    }

    /**
     * Gets a GameObject from the scene (existing or added dynamically)
     * @param name the unique name of the GameObject
     * @return the game object with the specified name or null
     */
    public GameObject getGameObject(String name) {
        for (GameObject go : objects)
            if (go.name.equals(name))
                return go;

        return null;
    }

    /**
     * Removes a game object from the scene<br/>
     * <i>Note: the GameObject is not destroyed, use its destroy() method if that is required!</i>
     * @param gameObject the GameObject to remove
     * @see GameObject#destroy()
     */
    public void removeGameObject(GameObject gameObject) {
        for (List<GameObject> layer : objectsByLayer)
            layer.remove(gameObject);

        objects.remove(gameObject);
    }

    /**
     * Destroys this scene, along with its game objects and their components
     */
    public void destroy() {
        for (GameObject go : objects)
            go.destroy();

        objects.clear();
        objectsByLayer.clear();
    }
}
