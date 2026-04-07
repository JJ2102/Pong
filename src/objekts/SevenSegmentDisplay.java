package objekts;

import meshes.SevenSegmentMeshes;

import java.awt.*;

public class SevenSegmentDisplay extends Entity {

    public SevenSegmentDisplay() {
        super(Color.GREEN, Color.GREEN);
        this.setMesh(SevenSegmentMeshes.getDisplayMesh(0, 0));
    }

    public void setScore(int left, int right) {
        int left1 = Math.clamp(left, 0, 9);
        int right1 = Math.clamp(right, 0, 9);
        this.setMesh(SevenSegmentMeshes.getDisplayMesh(left1, right1));
    }
}