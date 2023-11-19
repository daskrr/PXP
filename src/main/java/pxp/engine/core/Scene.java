package pxp.engine.core;

import pxp.engine.core.component.Camera;
import pxp.engine.core.manager.LayerManager;
import pxp.engine.data.GameObjectSupplier;
import pxp.engine.data.Rect;
import processing.event.MouseEvent;
import pxp.engine.core.component.Component;
import pxp.logging.Debug;

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
     * The index of this scene (is automatically determined from the list of scenes based on position in array)
     */
    public int index = -1;

    /**
     * The GameObjects' suppliers (for re-usability)
     */
    private GameObjectSupplier[] gameObjectSuppliers;

    /**
     * The instantiated GameObjects of the scene (are only used when the scene is loaded & running)
     */
    public final List<GameObject> objects = new ArrayList<>();

    /**
     * The instantiated GameObjects sorted by sortingLayer
     */
    public ArrayList<GameObject>[] objectsByLayer = null;

    /**
     * Whether the scene has loaded
     */
    private boolean loaded = false;

    /**
     * Whether to draw a grid on top of the screen after rendering (in the background - yeah it doesn't make sense, and it's not my fault)
     */
    private boolean grid = false;

    /**
     * Whether to debug all sorting layers with all objects in the console at load
     */
    private boolean debugSortingLayers = false;

    /**
     * The main camera of the scene
     */
    private Camera mainCam;

    /**
     * [Internal Usage] The current game process<br/>
     * (it is only changed by the Editor, should otherwise be {@link GameProcess#getInstance()})
     */
    public GameProcess context = null;

    /**
     * Creates an empty scene
     */
    public Scene() {
        this.gameObjectSuppliers = new GameObjectSupplier[0];
    }

    /**
     * Creates a new scene to be used in the game
     * @param gameObjectSuppliers The GameObject (preferably lambda) suppliers
     */
    public Scene(GameObjectSupplier[] gameObjectSuppliers) {
        this.gameObjectSuppliers = gameObjectSuppliers;
    }

    /**
     * Sets the Game Object Suppliers for this scene.
     * @param gameObjectSuppliers the supplier array containing all game objects of this scene
     */
    public void setGameObjects(GameObjectSupplier[] gameObjectSuppliers) {
        this.gameObjectSuppliers = gameObjectSuppliers;
    }

    /**
     * Loads the scene into the game (along with its gameObjects and Components)
     */
    protected void load() {
        // Default layer check is performed by the LayerManager

        this.createLayersArray();

        for (GameObjectSupplier supplier : gameObjectSuppliers) {
            GameObject go = supplier.get();
            objects.add(go);

            // arrange game objects into sorting layers
            registerSortingLayer(go);

            // give scene context to go
            go.scene = this;

            go.load();
        }

        // debugging the layers to console
        if (debugSortingLayers) {
            StringBuilder stringBuilder = new StringBuilder("[Scene]\nSorting Layers:\n");
            for (int i = 0; i < objectsByLayer.length; i++) {
                stringBuilder.append("  [Layer \"").append(LayerManager.getSortingLayerName(i)).append("\"]: \n");
                stringBuilder.append("    ").append(objectsByLayer[i]).append("\n");
            }

            Debug.log(stringBuilder.toString());
        }

        GameProcess.nextFrame(() -> this.loaded = true);
    }

    /**
     * The main render method, re-paints the background, sets up the camera view and renders GameObjects based on sorting layers
     */
    protected void render() {
        if (!loaded) return;

        // refresh background
        if (context.settings.backgroundImage != null)
            context.background(context.settings.backgroundImage.getPImage());
        else
            context.background(context.settings.background.getHex());

        try {
            Camera cam = this.getCamera();
            cam.applyCamera();
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(0);
        }

        for (List<GameObject> layer : this.objectsByLayer)
            if (layer != null)
                for (GameObject go : layer)
                    if (go.renderer != null && go.isActive) {
                        go.transform.bindAll(context);
                        go.renderer.render();
                        if (!go.renderer.handlesReset)
                            go.renderer.reset();
                        go.transform.unbindAll(context);
                    }

        this.objects.forEach((go) -> {
            if (!go.isDestroyed && go.isActive) {
                go.transform.bind(this.context);
                go.gizmosDraw();
                go.transform.unbind(this.context);
            }
        });

        // really shitty implementation, but is accurate and for debug purposes only
        // also for some reason the lines display under everything and do not blend alpha with textures ????
        if (grid) {
            Rect camRect = getCamera().getRect();
            context.stroke(0xffffffff);
            for (int i = (int) camRect.left() - 1; i <= (int) camRect.right(); i++) {
                for (int j = (int) camRect.top() - 1; j <= (int) camRect.bottom(); j++) {
                    context.line(i, j, i + 1, j);
                    context.line(i, j, i, j + 1);
                }
            }
            context.noStroke();
        }
    }

    /**
     * Creates the layers array based on the sorting layers of the game
     */
    private void createLayersArray() {
        // in case this was called before, we don't want to override the array
        if (objectsByLayer != null) return;

        // this is how it would make sense for it to work \/
        // objectsByLayer = new ArrayList<GameObject> [handlingProcess.settings.sortingLayers.size()];
        // but java, for some god-damned reason doesn't want to allow it, so...
        // giant fucking no-no right here \/
        objectsByLayer = (ArrayList<GameObject>[]) Array.newInstance(ArrayList.class, LayerManager.sortingLayers.size());
    }

    /**
     * Adds a GameObjects to the objectsByLayer map and sorts the map (in case a new layer was added)
     * @param gameObject the game object to register
     */
    public void registerSortingLayer(GameObject gameObject) {
        if (gameObject.renderer == null)
            return;

        // just in case the objectsByLayer array doesn't exist, we can try to create one
        createLayersArray();

        int sortingLayerId = LayerManager.getSortingLayerId(gameObject.renderer.getSortingLayer());

        // in case the sorting layer doesn't exist, we default
        if (sortingLayerId == -1)
            sortingLayerId = LayerManager.getSortingLayerId("Default");

        if (objectsByLayer[sortingLayerId] == null)
            objectsByLayer[sortingLayerId] = new ArrayList<>() {{ add(gameObject); }};
        else
            objectsByLayer[sortingLayerId].add(gameObject);

        // sort layer
        objectsByLayer[sortingLayerId].sort(Comparator.comparingInt(o -> o.renderer.getOrderInLayer()));
    }

    /**
     * Looks for the game object in the sorting layers and removes it if it is found
     * @param gameObject the game object to unregister
     */
    public void unregisterSortingLayer(GameObject gameObject) {
        if (objectsByLayer != null)
            for (List<GameObject> layer : objectsByLayer)
                if (layer != null) {
                    layer.remove(gameObject);
                    break;
                }
    }

    /**
     * Propagates a mouse event throughout all game objects
     * @param event the event to propagate
     */
    protected void propagateMouseEvent(MouseEvent event) {
        for (List<GameObject> layer : objectsByLayer)
            if (layer != null)
                layer.forEach(go -> {
                    if (!go.isDestroyed && go.isActive)
                        go.propagateMouseEvent(event);
                });
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
     * [Internal] Adds a game object to the list of objects <b>before</b> the scene loads (can cause ConcurrentEx)
     * @param gameObject the game object to add
     */
    protected void addGameObjectBeforeLoad(GameObject gameObject) {
        gameObject.scene = this;

        objects.add(gameObject);
        registerSortingLayer(gameObject);
        gameObject.load();
    }

    /**
     * Dynamically adds a GameObject to the scene<br/>
     * Tip: Use Component#Instantiate()
     * @param gameObject the game object to add
     * @see Component#instantiate(GameObject)
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
     * @see Component#instantiate(GameObject)
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
     * @see Component#instantiate(GameObject)
     * @see Scene#getGameObjectDeep(String)
     */
    public void addGameObject(GameObject gameObject, String parentName) {
        gameObject.scene = this;

        GameObject parentGo = getGameObjectDeep(parentName);
        if (parentGo == null)
            throw new RuntimeException("The parent could not be found after a deep search.");

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
     * Enables/disables drawing a grid in the background
     */
    public void drawGrid(boolean draw) {
        this.grid = draw;
    }
    /**
     * Enables/disables showing of the sorting layers in the console
     */
    public void debugSortingLayers(boolean debug) {
        this.debugSortingLayers = debug;
    }

    /**
     * Removes a game object from the scene<br/>
     * <i>Note: the GameObject is not destroyed, use its destroy() method if that is what is needed.</i>
     * @param gameObject the GameObject to remove
     * @see GameObject#destroy()
     */
    // this is not next frame due to usage issues, if this method is used, it needs to be used inside GameProcess#nextFrame()
    protected void removeGameObject(GameObject gameObject) {
        unregisterSortingLayer(gameObject);

        objects.remove(gameObject);
    }

    /**
     * Destroys this scene, along with its game objects and their components
     */
    public void destroy() {
        GameProcess.nextFrame(() -> {
            for (GameObject go : objects)
                // don't automatically destroy persistent objects
                if (!go.persistent)
                    go.destroy();

            objects.clear();
            objectsByLayer = null;
            loaded = false;
            mainCam = null; // AAAAAAAAAAAAAAH not resetting this took me 30 minutes of debugging hash codes ʘ‿ʘ
            context = null;
        });
    }
}
