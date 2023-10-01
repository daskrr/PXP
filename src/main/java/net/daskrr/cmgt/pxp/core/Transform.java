package net.daskrr.cmgt.pxp.core;

import net.daskrr.cmgt.pxp.data.Vector3;

/**
 * The Transform contains the position, scale and rotation of a GameObject
 */
public class Transform
{
    /**
     * The position of the game object
     */
    public Vector2 position = new Vector2();
    /**
     * The rotation of the game object
     */
    public Vector2 scale = new Vector2(1,1);
    /**
     * The rotation of the game object
     */
    public Vector3 rotation = new Vector3();

    /**
     * Z position for exceptional cases<br/>
     * <b>USE SORTING LAYERS (under renderers) TO DETERMINE SORTING ORDER!</b>
     */
    public float zPosition = 0;

    /**
     * Creates a new Transform with default position, rotation and scale
     */
    public Transform() { }

    /**
     * Creates a new Transform given a position and the default rotation and scale
     */
    public Transform(Vector2 position) {
        this(position, new Vector3());
    }

    /**
     * Creates a new Transform given a position, rotation and the default scale
     */
    public Transform(Vector2 position, Vector3 rotation) {
        this(position, rotation, new Vector2(1,1));
    }

    /**
     * Creates a new Transform given a position, rotation and scale
     */
    public Transform(Vector2 position, Vector3 rotation, Vector2 scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    /**
     * Binds the transform's matrix
     */
    protected void bind() {
        GameProcess.getInstance().pushMatrix();
        GameProcess.getInstance().translate(position.x, position.y, zPosition);
        GameProcess.getInstance().rotateX((float) Math.toRadians(rotation.x));
        GameProcess.getInstance().rotateY((float) Math.toRadians(rotation.y));
        GameProcess.getInstance().rotateZ((float) Math.toRadians(rotation.z));
        GameProcess.getInstance().scale(scale.x, scale.y, 1);
    }

    /**
     * Unbinds the transform's matrix
     */
    public void unbind() {
        GameProcess.getInstance().popMatrix();
    }

    /**
     * Clones the Transform
     * @return the cloned transform
     */
    public Transform clone() {
        return new Transform(position, rotation, scale);
    }
}
