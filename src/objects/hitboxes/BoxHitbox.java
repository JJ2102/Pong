package objects.hitboxes;

import math.Vector3;
import meshes.CuboidMesh;
import objects.Entity;

import java.awt.Color;

/**
 * Achsenparallele 3D-Hitbox (AABB), die über ihren Mittelpunkt und ihre Größe definiert wird.
 * Sie ist selbst eine {@link Entity} und kann daher zur Fehlersuche dargestellt werden.
 */
public class BoxHitbox extends Entity {
    private final Vector3 size; // Breite, Höhe, Tiefe

    /**
     * Erstellt eine Hitbox an der angegebenen Position mit der angegebenen Größe.
     *
     * @param center Mittelpunkt der Hitbox
     * @param size Breite, Höhe und Tiefe der Hitbox
     * @param color Farbe, in der die Hitbox dargestellt wird
     */
    public BoxHitbox(Vector3 center, Vector3 size, Color color) {
        super(color, color, new CuboidMesh(size.getX(), size.getY(), size.getZ()));
        setPosition(center);
        this.size = size;
    }

    /**
     * Verschiebt die Hitbox auf einen neuen Mittelpunkt.
     *
     * @param center neuer Mittelpunkt der Hitbox
     */
    public void setPosition(Vector3 center) {
        getTransform().setPosition(center);
    }

    /**
     * Gibt die Abmessungen der Box (Breite, Höhe, Tiefe) als Kopie zurück.
     *
     * @return neuer Vektor mit den Abmessungen der Box
     */
    public Vector3 getSize() {
        return new Vector3(size.getX(), size.getY(), size.getZ());
    }

    /**
     * Gibt die Ecke unten, vorne, links der Box zurück.
     *
     * @return Ecke mit den kleinsten Koordinaten auf allen Achsen
     */
    public Vector3 getMin() {
        return new Vector3(
                getTransform().getPosition().getX() - size.getX() / 2,
                getTransform().getPosition().getY() - size.getY() / 2,
                getTransform().getPosition().getZ() - size.getZ() / 2
        );
    }

    /**
     * Gibt die Ecke oben, hinten, rechts der Box zurück.
     *
     * @return Ecke mit den größten Koordinaten auf allen Achsen
     */
    public Vector3 getMax() {
        return new Vector3(
                getTransform().getPosition().getX() + size.getX() / 2,
                getTransform().getPosition().getY() + size.getY() / 2,
                getTransform().getPosition().getZ() + size.getZ() / 2
        );
    }

    /**
     * Prüft, ob sich diese Hitbox mit einer anderen überschneidet.
     * <p>
     * Zwei achsenparallele Boxen überschneiden sich genau dann, wenn sich ihre Intervalle
     * auf allen drei Achsen überlappen. Berührende Kanten zählen als Überschneidung.
     *
     * @param other andere Hitbox
     * @return {@code true}, wenn sich die beiden Hitboxen überschneiden, sonst {@code false}
     */
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
