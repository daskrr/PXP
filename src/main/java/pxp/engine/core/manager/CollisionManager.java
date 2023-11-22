package pxp.engine.core.manager;

import java.util.ArrayList;
import java.util.List;

import pxp.engine.core.GameObject;
import pxp.engine.core.component.Collider;
import pxp.engine.data.CollisionEvent;
import pxp.engine.data.Vector2;
import pxp.engine.data.collision.Collision;
import pxp.util.Pair;
import pxp.util.Pointer;
import pxp.util.SwitchPair;

/**
 * The Collision Manager handles all the collision checking and takes the appropriate steps in regard to what happens
 * with a collision. It holds all Game Objects that have colliders and each frame checks those colliders against one another.<br/>
 * When a collision happens, it creates collision events and dispatches them for the colliders to handle.
 */
public class CollisionManager
{
    /**
     * The currently registered game objects (which have colliders)
     */
    private final List<GameObject> gameObjects = new ArrayList<>();
    /**
     * The game objects that are currently colliding with another game object
     */
    private final List<Pair<GameObject, GameObject>> ongoingCollisions = new ArrayList<>();

    private final List<SwitchPair<Integer, Integer>> ignoredLayers = new ArrayList<>();

    public CollisionManager(List<Pair<String, String>> ignoredLayers) {
        for (Pair<String, String> ignoredLayer : ignoredLayers)
            this.ignoredLayers.add(new SwitchPair<>(LayerManager.getLayerId(ignoredLayer.left), LayerManager.getLayerId(ignoredLayer.right)));
    }

    /**
     * Registers a GameObject to be checked every frame for collisions
     * @param obj the game object to register
     */
    public void register(GameObject obj) {
        if (!this.gameObjects.contains(obj))
            this.gameObjects.add(obj);
    }

    /**
     * Unregisters a GameObject from the step check
     * @param obj the game object to unregister
     */
    public void unregister(GameObject obj) {
        this.gameObjects.remove(obj);
    }

    /**
     * Disables two layers' collision
     * @param layers the layers to disable the collision of
     */
    public void setIgnoreLayers(Pair<String, String> layers) {
        SwitchPair<Integer, Integer> ignored = new SwitchPair<>(LayerManager.getLayerId(layers.left), LayerManager.getLayerId(layers.right));
        if (this.ignoredLayers.contains(ignored)) return;

        this.ignoredLayers.add(ignored);
    }

    /**
     * Enables the collision of two layers if it was previously set
     * @param layers the layers to collide again
     */
    public void setDontIgnoreLayers(Pair<String, String> layers) {
        SwitchPair<Integer, Integer> ignored = new SwitchPair<>(LayerManager.getLayerId(layers.left), LayerManager.getLayerId(layers.right));
        this.ignoredLayers.remove(ignored);
    }

    /**
     * Runs every step and performs hit tests on all colliders
     */
    public void update() {
        List<GameObject> lock = new ArrayList<>(this.gameObjects);
        lock.forEach((object) -> {
            Collider[] components = object.getComponentsOfType(Collider.class);

            for (Collider component : components)
                component.collisionUpdate();

            List<Collider> incomingColliders = new ArrayList<>(object.colliders);

            incomingColliders.forEach((incomingCol) -> {

                lock.forEach((other) -> {

                    if (!other.equals(object)) {

                        List<Collider> otherColliders = new ArrayList<>(other.colliders);
                        otherColliders.forEach((otherCol) -> {

                            if (this.ignoredLayers.contains(new SwitchPair<>(incomingCol.layer, otherCol.layer)))
                                return;

                            Pointer<Vector2> reflectVelocity = new Pointer<>(null);
                            Pointer<Float> toi = new Pointer<>(0.0F);
                            Collision collision = new Collision(incomingCol, otherCol);
                            CollisionEvent event;
                            if (incomingCol.collisionCheck(otherCol, reflectVelocity, toi, collision.contactPoint)) {
                                if (this.ongoingCollisions.contains(new Pair<>(object, other)))
                                    event = new CollisionEvent(CollisionEvent.Time.STAY, collision);
                                else {
                                    this.ongoingCollisions.add(new Pair<>(object, other));
                                    event = new CollisionEvent(CollisionEvent.Time.ENTER, collision);
                                }

                                incomingCol.collide(event, reflectVelocity, toi.value);
                            }
                            else if (this.ongoingCollisions.contains(new Pair<>(object, other))) {
                                event = new CollisionEvent(CollisionEvent.Time.EXIT, collision);
                                incomingCol.stoppedColliding(event);
                                this.ongoingCollisions.remove(new Pair<>(object, other));
                            }
                        });
                    }
                });
            });
        });
    }
}
