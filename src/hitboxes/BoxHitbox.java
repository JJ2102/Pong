package hitboxes;

import math.Vector3;

// Achsenparallele 3D-Hitbox, die über Mittelpunkt und Größe definiert wird
public class BoxHitbox {
    private final Vector3 size; // Breite, Höhe, Tiefe
    private Vector3 position; // Mittelpunkt der Box

    public BoxHitbox(Vector3 center, Vector3 size) {
        this.position = center;
        this.size = size;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    // Gibt den Mittelpunkt der Box zurück
    public Vector3 getCenter() {
        return new Vector3(position.getX(), position.getY(), position.getZ());
    }

    // Gibt die Abmessungen der Box zurück (Breite, Höhe, Tiefe)
    public Vector3 getSize() {
        return new Vector3(size.getX(), size.getY(), size.getZ());
    }

    public Vector3 getMin() {
        // Gibt die Ecke unten, hinten, links der Box zurück
        return new Vector3(
                position.getX() - size.getX() / 2,
                position.getY() - size.getY() / 2,
                position.getZ() - size.getZ() / 2
        );
    }

    public Vector3 getMax() {
        // Gibt die Ecke oben, vorne, rechts der Box zurück
        return new Vector3(
                position.getX() + size.getX() / 2,
                position.getY() + size.getY() / 2,
                position.getZ() + size.getZ() / 2
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
        return "BoxHitbox{" + "position=" + position + ", size=" + size + '}';
    }
}
