package objects;

import meshes.SevenSegmentMeshes;

import java.awt.Color;

/**
 * Stellt die 3D-Punkteanzeige im Stil einer Sieben-Segment-Anzeige über dem Spielfeld dar.
 */
public class SevenSegmentDisplay extends Entity {

    /**
     * Initialisiert das Display-Mesh mit dem Punktestand 0:0.
     */
    public SevenSegmentDisplay() {
        super(Color.GREEN, Color.GREEN, SevenSegmentMeshes.getDisplayMesh(0, 0));
    }

    /**
     * Aktualisiert die Anzeige auf den neuen Punktestand.
     * Werte außerhalb von 0 bis 9 werden auf diesen Bereich begrenzt.
     *
     * @param left Punktestand der linken Seite
     * @param right Punktestand der rechten Seite
     */
    public void setScore(int left, int right) {
        // Anzeige kann nur einstellige Ziffern (0-9) darstellen, daher begrenzen
        int clampedLeft = Math.clamp(left, 0, 9);
        int clampedRight = Math.clamp(right, 0, 9);
        setMesh(SevenSegmentMeshes.getDisplayMesh(clampedLeft, clampedRight));
    }
}
