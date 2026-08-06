package rendering;

import math.Vector3;

// Kapselt Position, Rotation und Skalierung eines Objekts im 3D-Raum
// Kapselt Position, Rotation und Skalierung eines Objekts im 3D-Raum
public class Transform {
    private Vector3 position;
    private Vector3 rotation;
    private Vector3 scale;

    // Initialisiert eine Standard-Transformation (Ursprung, keine Rotation, Skalierung 1)
    public Transform() {
        this.position = new Vector3(0, 0, 0);
        this.rotation = new Vector3(0, 0, 0);
        this.scale = new Vector3(1, 1, 1);
    }

    // Initialisiert eine Transformation mit vorgegebenen Vektoren
    public Transform(Vector3 position, Vector3 rotation, Vector3 scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    // Liefert eine Invertierung der gewählten Transformations-Komponenten (z.B. für die Kamera)
    public Transform invert(boolean position, boolean rotation, boolean scale) {
        Transform inverted = new Transform(this.position, this.rotation, this.scale); // Kopie der aktuellen Werte erstellen

        if (position) inverted.position = this.position.invert(); // Position umkehren (aus +5 wird -5)
        if (rotation) inverted.rotation = this.rotation.invert(); // Rotation umkehren
        if (scale) inverted.scale = this.scale.invert(); // Skalierung invertieren (aus 2 wird 1/2)

        return inverted; // Liefert die invertierte Kopie zurück, ohne das Original zu verändern
    }

    // ===== Hilfsmethoden =====
    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Vector3 getRotation() {
        return rotation;
    }

    public void setRotation(Vector3 rotation) {
        this.rotation = rotation;
    }

    public Vector3 getScale() {
        return scale;
    }

    public void setScale(Vector3 scale) {
        this.scale = scale;
    }

    public String toString() {
        return "Transform{ " + "position=" + position + ", rotation=" + rotation + ", scale=" + scale + '}';
    }
}
