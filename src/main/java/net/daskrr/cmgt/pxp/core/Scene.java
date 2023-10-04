package net.daskrr.cmgt.pxp.core;

import net.daskrr.cmgt.pxp.core.component.Camera;
import net.daskrr.cmgt.pxp.data.GameObjectSupplier;

import java.lang.reflect.Array;
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
    public ArrayList<GameObject>[] objectsByLayer = null;

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
//        objectsByLayer = new ArrayList<GameObject> [GameProcess.getInstance().settings.sortingLayers.size()];
        // giant fucking no-no right here \/
        objectsByLayer = (ArrayList<GameObject>[]) Array.newInstance(ArrayList.class, GameProcess.getInstance().settings.sortingLayers.size());

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
    protected void registerSortingLayer(GameObject gameObject) {
        if (gameObject.renderer == null)
            return;

        int sortingLayerId = GameProcess.getInstance().settings.sortingLayers.indexOf(gameObject.renderer.sortingLayer);

        if (objectsByLayer[sortingLayerId] == null)
            objectsByLayer[sortingLayerId] = new ArrayList<>() {{ add(gameObject); }};
        else
            objectsByLayer[sortingLayerId].add(gameObject);

        // sort layer
        objectsByLayer[sortingLayerId].sort(Comparator.comparingInt(o -> o.renderer.orderInLayer));
    }

    /**
     * Gets the first Camera GameObject in the scene and establishes it as the main camera to be used for this scene
     * @return the camera game object
     * @throws RuntimeException if there isn't a Camera GameObject present in the scene
     */
    public Camera getCamera() {
        if (mainCam != null)
            return mainCam;

        for (GameObject object : objects) {
            mainCam = object.getComponentOfType(Camera.class);
            if (mainCam != null)
                return mainCam;
        }

        throw new RuntimeException("The scene doesn't contain a camera!");
    }

    /**
     * Dynamically adds a GameObject to the scene<br/>
     * Tip: Use Component#Instantiate()
     * @param gameObject the game object to add
     * @see net.daskrr.cmgt.pxp.core.component.Component#Instantiate(GameObject)
     */
    public void addGameObject(GameObject gameObject) {
        gameObject.scene = this;

        GameProcess.nextFrame(() -> {
            objects.add(gameObject);
            registerSortingLayer(gameObject);

            gameObject.load();
        });
    }

    /**
     * Dynamically adds a GameObject to the scene, under a specified parent<br/>
     * Tip: Use Component#Instantiate()
     * @param gameObject the game object to add
     * @param parent the parent under which the newly instantiated game object will exit
     * @see net.daskrr.cmgt.pxp.core.component.Component#Instantiate(GameObject)
     */
    public void addGameObject(GameObject gameObject, GameObject parent) {
        gameObject.scene = this;

        GameProcess.nextFrame(() -> {
            parent.addGameObject(gameObject);
            registerSortingLayer(gameObject);

            gameObject.parent = parent;
            gameObject.load();
        });
    }
    /**
     * Dynamically adds a GameObject to the scene, under a specified parent (by name, searched using Scene#getGameObjectDeep())<br/>
     * Tip: Use Component#Instantiate()
     * @param gameObject the game object to add
     * @param parentName the parent's name under which the newly instantiated game object will exist
     * @throws RuntimeException if the parent could not be found
     * @see net.daskrr.cmgt.pxp.core.component.Component#Instantiate(GameObject)
     * @see Scene#getGameObjectDeep(String)
     */
    public void addGameObject(GameObject gameObject, String parentName) {
        gameObject.scene = this;

        GameObject parentGo = getGameObjectDeep(parentName);
        if (parentGo == null)
            throw new RuntimeException("The parent could not be found in any of the parents after a deep search.");

        GameProcess.nextFrame(() -> {
            parentGo.addGameObject(gameObject);
            registerSortingLayer(gameObject);

            gameObject.parent = parentGo;
            gameObject.load();
        });
    }

    /**
     * Gets a GameObject from the scene level (existing or added dynamically)
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
     * Gets a GameObject from the scene or from any parent game object (existing or added dynamically)
     * @param name the unique name of the GameObject
     * @return the game object with the specified name or null
     */
    public GameObject getGameObjectDeep(String name) {
        for (GameObject go : objects) {
            if (go.name.equals(name))
                return go;
            GameObject child = go.getGameObjectDeep(name);
            if (child != null)
                return child;
        }

        return null;
    }

    /**
     * Removes a game object from the scene<br/>
     * <i>Note: the GameObject is not destroyed, use its destroy() method if that is what is needed.</i>
     * @param gameObject the GameObject to remove
     * @see GameObject#destroy()
     */
    // this is not next frame due to usage issues, if this method is used, it needs to be used inside GameProcess#nextFrame()
    protected void removeGameObject(GameObject gameObject) {
        for (List<GameObject> layer : objectsByLayer)
            if (layer != null)
                layer.remove(gameObject);

        objects.remove(gameObject);
    }

    /**
     * Destroys this scene, along with its game objects and their components
     */
    public void destroy() {
        GameProcess.nextFrame(() -> {
            for (GameObject go : objects)
                go.destroy();

            objects.clear();
            objectsByLayer = null;
        });
    }
}
