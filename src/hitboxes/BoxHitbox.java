package hitboxes;

import math.Vektor3;

public class BoxHitbox {
    Vektor3 position; // Center position
    Vektor3 size;   // Width, Height, Depth

    public BoxHitbox(Vektor3 center, Vektor3 size) {
        position = center;
        this.size = size;
    }

    public void setPosition(Vektor3 position) {
        this.position = position;
    }

    public Vektor3 getMin() {
        // Gibt die minimale Ecke der Box zurück
        return new Vektor3(
            position.x - size.x / 2,
            position.y - size.y / 2,
            position.z - size.z / 2
        );
    }

    public Vektor3 getMax() {
        // Gibt die maximale Ecke der Box zurück
        return new Vektor3(
            position.x + size.x / 2,
            position.y + size.y / 2,
            position.z + size.z / 2
        );
    }

    public boolean intersects(BoxHitbox other) {
        Vektor3 aMin = this.getMin();
        Vektor3 aMax = this.getMax();
        Vektor3 bMin = other.getMin();
        Vektor3 bMax = other.getMax();

        return (aMin.x <= bMax.x && aMax.x >= bMin.x) && // X axis overlap
               (aMin.y <= bMax.y && aMax.y >= bMin.y) && // Y axis overlap
               (aMin.z <= bMax.z && aMax.z >= bMin.z);   // Z axis overlap
    }

    public String toString() {
        return "BoxHitbox{" + "position=" + position + ", size=" + size + '}';
    }
}
