package objects;

import meshes.SevenSegmentMeshes;

import java.awt.*;

// Repräsentiert die 3D-Punkteanzeige über dem Spielfeld
public class SevenSegmentDisplay extends Entity {

    // Initialisiert das Display-Mesh auf den Punktestand 0:0
    public SevenSegmentDisplay() {
        super(Color.GREEN, Color.GREEN);
        this.setMesh(SevenSegmentMeshes.getDisplayMesh(0, 0));
    }

    // Aktualisiert die Anzeige auf den neuen Punktestand (begrenzt auf Ziffern von 0 bis 9)
    public void setScore(int left, int right) {
        // Anzeige kann nur einstellige Ziffern (0-9) darstellen, daher begrenzen
        int left1 = Math.clamp(left, 0, 9);
        int right1 = Math.clamp(right, 0, 9);
        this.setMesh(SevenSegmentMeshes.getDisplayMesh(left1, right1));
    }
}
