package objekts;

import meshes.SevenSegmentMeshes;

import java.awt.*;

public class SevenSegmentDisplay extends Entity {
    private int left = 0;
    private int right = 0;

    public SevenSegmentDisplay() {
        super(Color.BLACK);
        this.setMesh(SevenSegmentMeshes.getDisplayMesh(0, 0));
    }

    public void setScore(int left, int right) {
        this.left = Math.max(0, Math.min(9, left));
        this.right = Math.max(0, Math.min(9, right));
        this.setMesh(SevenSegmentMeshes.getDisplayMesh(this.left, this.right));
    }

    public int getLeft() { return left; }
    public int getRight() { return right; }
}