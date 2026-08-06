package hitboxes;

import math.Vector3;

public class BoxHitbox {
    private Vector3 position; // Mittelpunkt der Box
    private final Vector3 size;   // Breite, Höhe, Tiefe

    public BoxHitbox(Vector3 center, Vector3 size) {
        position = center;
        this.size = size;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Vector3 getMin() {
        // Gibt die Ecke unten, hinten, links der Box zurück
        return new Vector3(
            position.x - size.x / 2,
            position.y - size.y / 2,
            position.z - size.z / 2
        );
    }

    public Vector3 getMax() {
        // Gibt die Ecke oben, vorne, rechts der Box zurück
        return new Vector3(
            position.x + size.x / 2,
            position.y + size.y / 2,
            position.z + size.z / 2
        );
    }

    public boolean intersects(BoxHitbox other) {
        Vector3 aMin = this.getMin();
        Vector3 aMax = this.getMax();
        Vector3 bMin = other.getMin();
        Vector3 bMax = other.getMax();

        return (aMin.x <= bMax.x && aMax.x >= bMin.x) && // X-Achsen überlappen
               (aMin.y <= bMax.y && aMax.y >= bMin.y) && // Y-Achsen überlappen
               (aMin.z <= bMax.z && aMax.z >= bMin.z);   // Z-Achsen überlappen
    }

    public String toString() {
        return "BoxHitbox{" + "position=" + position + ", size=" + size + '}';
    }
}
