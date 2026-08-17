package objects;

import meshes.SevenSegmentMeshes;

import java.awt.Color;

// Repräsentiert die 3D-Punkteanzeige über dem Spielfeld
public class SevenSegmentDisplay extends Entity {

    // Initialisiert das Display-Mesh auf den Punktestand 0:0
    public SevenSegmentDisplay() {
        super(Color.GREEN, Color.GREEN);
        setMesh(SevenSegmentMeshes.getDisplayMesh(0, 0));
    }

    // Aktualisiert die Anzeige auf den neuen Punktestand (begrenzt auf Ziffern von 0 bis 9)
    public void setScore(int left, int right) {
        // Anzeige kann nur einstellige Ziffern (0-9) darstellen, daher begrenzen
        int clampedLeft = Math.clamp(left, 0, 9);
        int clampedRight = Math.clamp(right, 0, 9);
        setMesh(SevenSegmentMeshes.getDisplayMesh(clampedLeft, clampedRight));
    }
}
