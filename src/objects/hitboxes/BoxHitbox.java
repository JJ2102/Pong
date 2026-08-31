package objects.hitboxes;

import math.Vector3;
import meshes.CuboidMesh;
import objects.Entity;

import java.awt.*;

// Achsenparallele 3D-Hitbox, die über Mittelpunkt und Größe definiert wird
public class BoxHitbox extends Entity {
    private final Vector3 size; // Breite, Höhe, Tiefe

    public BoxHitbox(Vector3 center, Vector3 size, Color color) {
        super(color, color);
        setPosition(center);
        this.size = size;

        this.setMesh(
                new CuboidMesh(size.getX() / 2, size.getY() / 2, size.getZ() / 2)
        );
    }

    public void setPosition(Vector3 center) {
        getTransform().setPosition(center);
    }

    // Gibt die Abmessungen der Box zurück (Breite, Höhe, Tiefe)
    public Vector3 getSize() {
        return new Vector3(size.getX(), size.getY(), size.getZ());
    }

    public Vector3 getMin() {
        // Gibt die Ecke unten, hinten, links der Box zurück
        return new Vector3(
                getTransform().getPosition().getX() - size.getX() / 2,
                getTransform().getPosition().getY() - size.getY() / 2,
                getTransform().getPosition().getZ() - size.getZ() / 2
        );
    }

    public Vector3 getMax() {
        // Gibt die Ecke oben, vorne, rechts der Box zurück
        return new Vector3(
                getTransform().getPosition().getX() + size.getX() / 2,
                getTransform().getPosition().getY() + size.getY() / 2,
                getTransform().getPosition().getZ() + size.getZ() / 2
        );
    }

    public boolean intersects(BoxHitbox other) {
        Vector3 aMin = this.getMin();
        Vector3 aMax = this.getMax();
        Vector3 bMin = other.getMin();
        Vector3 bMax = other.getMax();

        return (aMin.getX() <= bMax.getX() && aMax.getX() >= bMin.getX()) // X-Achsen überlappen
                && (aMin.getY() <= bMax.getY() && aMax.getY() >= bMin.getY()) // Y-Achsen überlappen
                && (aMin.getZ() <= bMax.getZ() && aMax.getZ() >= bMin.getZ()); // Z-Achsen überlappen
    }

    @Override
    public String toString() {
        return "BoxHitbox{" + "position=" + getTransform().getPosition() + ", size=" + size + '}';
    }
}
