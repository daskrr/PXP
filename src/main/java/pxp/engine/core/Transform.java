package pxp.engine.core;

import pxp.engine.data.Vector2;
import pxp.engine.data.Vector3;
import processing.core.PMatrix3D;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

/**
 * The Transform contains the position, scale and rotation of a GameObject
 */
public class Transform
{
    /**
     * The game object that owns this transform
     */
    public GameObject gameObject;

    /**
     * The <b>local</b> position of the game object
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
     * Creates a new Transform given a position, scale and the default rotation
     */
    public Transform(Vector2 position, Vector2 scale) {
        this(position, new Vector3(), scale);
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
     * Converts the local position of this transform to the world coordinates using all the matrices of the parents
     * @return the transform position scaled to world coordinates
     */
    public Vector2 getWorldPosition() {
        Vector2 position = this.position.clone();
        if (this instanceof RectTransform rt)
            position.add(rt.anchorTranslation);

        // use the parent's localToWorld to calculate the world position (since we need to scale this position in the parent's local space
        if (this.gameObject.parent != null)
            return this.gameObject.parent.transform.localToWorld(position);

        return this.position;
    }

    /**
     * Sets the position of this Game Object given a world position, which in turn is transformed into the local space (of the parent)
     * @param worldPosition the world position of this game object to set
     */
    public void setWorldPosition(Vector2 worldPosition) {
        Vector2 worldToLocal = worldPosition;
        if (this.gameObject.parent != null)
            worldToLocal = this.gameObject.parent.transform.worldToLocal(worldPosition);

        this.position = worldToLocal.clone();
    }

    /**
     * Binds the transform's matrix
     */
    protected void bind() {
        this.bind(GameProcess.getInstance());
    }

    /**
     * Binds the transform's matrix, given a GameProcess context
     */
    protected void bind(GameProcess context) {
        context.pushMatrix();
        // add anchor position
        Vector2 position = this.position.clone();
        if (this instanceof RectTransform rt)
            position.add(rt.anchorTranslation);

        context.translate(position.x, position.y, zPosition);

        context.rotateX((float) Math.toRadians(rotation.x));
        context.rotateY((float) Math.toRadians(rotation.y));
        context.rotateZ((float) Math.toRadians(rotation.z));

        context.scale(scale.x, scale.y, 1);
    }

    /**
     * Unbinds the transform's matrix
     */
    protected void unbind() {
        this.unbind(GameProcess.getInstance());
    }

    /**
     * Unbinds the transform's matrix
     */
    public void unbind(GameProcess context) {
        context.popMatrix();
    }
    /**
     * Binds the transform's matrix as well as all the parents' transform matrices
     */
    protected void bindAll(GameProcess context) {
        if (gameObject.parent != null)
            gameObject.parent.transform.bindAll(context);

        bind(context);
    }

    protected void unbindAll(GameProcess context) {
        unbind(context);

        if (gameObject.parent != null)
            gameObject.parent.transform.unbindAll(context);
    }

    /**
     * Creates a {@link PMatrix3D} (Matrix4) using this transform's properties
     * @return a PMatrix3D (Matrix4)
     */
    protected PMatrix3D getLocalMatrix() {
        PMatrix3D mat = new PMatrix3D();

        // add anchor position
        Vector2 position = this.position.clone();
        if (this instanceof RectTransform rt)
            position.add(rt.anchorTranslation);

        mat.translate(position.x, position.y, zPosition);

        mat.rotateX(this.rotation.x);
        mat.rotateY(this.rotation.y);
        mat.rotateZ(this.rotation.z);

        mat.scale(this.scale.x, this.scale.y, 1);


        /* using jogamp
         * Matrix4 mat = new Matrix4();
         * mat.loadIdentity();
         * mat.scale(this.scale.x, this.scale.y, 1);
         * mat.rotate(new Quaternion(this.rotation.x, this.rotation.y, this.rotation.z, 1f));
         * mat.translate(this.position.x, this.position.y, this.zPosition);
         */

        return mat;
    }

    /**
     * Composes a list of all matrices of this transform's and all parents'
     */
    private List<PMatrix3D> getMatrixStack() {
        List<PMatrix3D> matrices = new ArrayList<>();
        if (gameObject.parent != null)
            matrices.addAll(gameObject.parent.transform.getMatrixStack());

        // add this matrix too (to simulate the order properly)
        matrices.add(getLocalMatrix());
        return matrices;
    }

    /**
     * Creates a {@link PMatrix3D} (Matrix4) using the all the parents' matrices
     * @return a PMatrix3D (Matrix4)
     */
    public PMatrix3D getMatrix() {
        // get all matrices
        List<PMatrix3D> matrices = getMatrixStack();

        // multiply them
        PMatrix3D identity = new PMatrix3D();
        matrices.forEach(identity::apply);

        return identity;
    }

    /**
     * Converts a position from local to world (using all matrices but this transform's)
     * @param local the local position to transform
     * @return the world position
     */
    public Vector2 localToWorld(Vector2 local) {
        PVector pLocal = local.toPVector();

        // we use get matrix to get the total matrix transformation (excluding our own)
        PMatrix3D mat = this.getMatrix();
        mat.mult(pLocal, pLocal); // then multiply the local position by the matrix

        return Vector2.fromPVector(pLocal);
    }

    /**
     * Converts a position from world to the local coordinate space of this game object
     * @param world the world position to transform
     * @return the local position
     */
    public Vector2 worldToLocal(Vector2 world) {
        // clone so no fuckery happens
        world = world.clone();

        // subtract the object's world position
        world.subtract(this.getWorldPosition());

        // and we got ourselves a nice local position
        return world;
    }


    /**
     * Clones the Transform
     * @return the cloned transform
     */
    public Transform clone() {
        return new Transform(position, rotation, scale);
    }

    /**
     * Clones this transform's values into a new {@link RectTransform}
     * @return the cloned RectTransform
     */
    public RectTransform toRectTransform() {
        return new RectTransform(this.position.clone(), this.rotation.clone(), this.scale.clone()) {{
            this.gameObject = Transform.this.gameObject;
            this.zPosition = Transform.this.zPosition;
        }};
    }
}
