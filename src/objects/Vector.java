package objects;

import math.Vector3;
import meshes.VectorMesh;

import java.awt.Color;

/**
 * Stellt einen Richtungsvektor als Linie in der 3D-Szene dar, z. B. zur Anzeige der Koordinatenachsen.
 */
public class Vector extends Entity {
    /**
     * Erstellt einen darstellbaren Vektor, der an der angegebenen Position beginnt.
     *
     * @param position Startpunkt des Vektors
     * @param direction Richtung des Vektors
     * @param length Länge, mit der die Richtung skaliert wird
     * @param color Farbe der Linie
     */
    public Vector(Vector3 position, Vector3 direction, double length, Color color) {
        super(color, color, new VectorMesh(direction, length));
        getTransform().setPosition(position);
    }
}
