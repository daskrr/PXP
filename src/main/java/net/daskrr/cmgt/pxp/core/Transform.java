package net.daskrr.cmgt.pxp.core;

import net.daskrr.cmgt.pxp.data.Vector3;

public class Transform
{
    public Vector2 position = new Vector2();
    public Vector2 scale = new Vector2(1,1);
    public Vector3 rotation = new Vector3();

    public Transform() { }

    public Transform(Vector2 position) {
        this(position, new Vector3());
    }

    public Transform(Vector2 position, Vector3 rotation) {
        this(position, rotation, new Vector2(1,1));
    }

    public Transform(Vector2 position, Vector3 rotation, Vector2 scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public void bind() {
        GameProcess.getInstance().pushMatrix();
        GameProcess.getInstance().translate(position.x, position.y);
        GameProcess.getInstance().rotateX((float) Math.toRadians(rotation.x));
        GameProcess.getInstance().rotateY((float) Math.toRadians(rotation.y));
        GameProcess.getInstance().rotateZ((float) Math.toRadians(rotation.z));
        GameProcess.getInstance().scale(scale.x, scale.y, 1);
    }

    public void unbind() {
        GameProcess.getInstance().popMatrix();
    }

    public Transform clone() {
        return new Transform(position, rotation, scale);
    }
}
