package pxp.engine.data.collision;

import pxp.engine.core.GameProcess;
import pxp.engine.core.Transform;
import pxp.engine.data.Vector2;
import pxp.util.Pointer;

/**
 * An axis independent rect shape.
 */
public class Box extends Shape
{
    /**
     * The local offset
     */
    public Vector2 offset;
    /**
     * Half of the size of this rect
     */
    public Vector2 halfSize;
    /**
     * The transform of the game object that the Collider is the child of
     */
    private final Transform transform;

    public Box(Vector2 offset, Vector2 halfSize, Transform transform) {
        this.offset = offset;
        this.halfSize = halfSize;
        this.transform = transform;
    }

    @Override
    public boolean hitTest(Shape other, Vector2 velocity, Pointer<Vector2> reflectVelocity, Pointer<Float> toi, Vector2 contactPoint) {
        if (other instanceof Box box)
            return this.hitTest(box, velocity, reflectVelocity, toi, contactPoint);
        else if (other instanceof Circle circle)
            return this.hitTest(circle, velocity, reflectVelocity, toi, contactPoint);

        return false;
    }

    // Get the four corners of the rectangle in world space
    private Vector2[] getCorners() {
        Vector2 offset = this.offset.clone();

        Vector2[] corners = new Vector2[4];
        corners[0] = this.transform.localToWorld(Vector2.add(offset, new Vector2(-halfSize.x, -halfSize.y)));
        corners[1] = this.transform.localToWorld(Vector2.add(offset, new Vector2(halfSize.x, -halfSize.y)));
        corners[2] = this.transform.localToWorld(Vector2.add(offset, new Vector2(halfSize.x, halfSize.y)));
        corners[3] = this.transform.localToWorld(Vector2.add(offset, new Vector2(-halfSize.x, halfSize.y)));

        // the returned corners are in WORLD position
        return corners;
    }
    // Project points onto an axis
    private Projection projectOntoAxis(Vector2[] points, Vector2 axis) {
        float min = Float.MAX_VALUE;
        float max = -Float.MAX_VALUE;

        for (Vector2 point : points) {
            float projection = point.dot(axis);
            min = Math.min(min, projection);
            max = Math.max(max, projection);
        }

        return new Projection(min, max);
    }

    private boolean hitTest(Box box, Vector2 velocity, Pointer<Vector2> reflectVelocity, Pointer<Float> toi, Vector2 contactPoint) {
        Vector2[] thisCorners = this.getCorners();
        Vector2[] otherCorners = box.getCorners();

        // Combine normals of edges for both rectangles
        Vector2[] axes = new Vector2[4];
        for (int i = 0; i < 2; i++) { // Top and left edges
            axes[i] = Vector2.vector(thisCorners[i], thisCorners[i + 1]).perpendicular().getNormalized();
            axes[i + 2] = Vector2.vector(otherCorners[i], otherCorners[i + 1]).perpendicular().getNormalized();
        }

        float minOverlap = Float.MAX_VALUE;
        Vector2 smallestAxis = null;
        Vector2 movement = velocity.clone();
        float epsilon = 0.001f; // Small value to avoid floating-point issues

        for (Vector2 axis : axes) {
            // Project both rectangles onto the axis
            Projection thisProj = projectOntoAxis(thisCorners, axis);
            Projection otherProj = projectOntoAxis(otherCorners, axis);

            // Check for overlap
            float overlap = thisProj.getOverlap(otherProj);
            if (overlap <= 0) return false; // No collision

            // Skip tiny overlaps caused by precision issues
            if (overlap < epsilon) continue;

            // Use velocity to determine correct resolution direction
            float dot = movement.dot(axis);
            if (dot < 0) {
                axis = axis.multiply(-1); // Flip axis if needed
            }

            // Keep track of the smallest overlap
            if (overlap < minOverlap) {
                minOverlap = overlap;
                smallestAxis = axis;
            }
        }

        // Push vector is along the smallest axis
        float maxPenetration = 1.0f; // Example: Limit penetration resolution
        minOverlap = Math.min(minOverlap, maxPenetration);
        reflectVelocity.value = smallestAxis.multiply(minOverlap);

        return true;

    }

    private boolean hitTest(Circle circle, Vector2 velocity, Pointer<Vector2> reflectVelocity, Pointer<Float> toi, Vector2 contactPoint) {
        Vector2 center = circle.center;
        Vector2[] corners = this.getCorners();

        // Find the closest point on the rectangle to the circle's center
        Vector2 closestPoint = center;
        float minDistance = Float.MAX_VALUE;

        for (int i = 0; i < 4; i++) {
            Vector2 point = Vector2.projectPointOntoLine(center, corners[i], corners[(i + 1) % 4]);
            float distance = point.distance(center);

            if (distance < minDistance) {
                minDistance = distance;
                closestPoint = point;
            }
        }

        float overlap = circle.radius - minDistance;
        if (overlap > 0) {
            reflectVelocity.value = Vector2.vector(closestPoint, center).getNormalized().multiply(overlap);
            return true;
        }
        return false;
    }
}

// Utility class for projections
class Projection {
    public float min, max;

    public Projection(float min, float max) {
        this.min = min;
        this.max = max;
    }

    public float getOverlap(Projection other) {
        return Math.min(this.max, other.max) - Math.max(this.min, other.min);
    }
}
