package meshes;

import rendering.Mesh;
import math.Vektor3;
import java.util.Arrays;

public class RectangleMesh extends Mesh {
    public RectangleMesh(double xSize, double ySize, double zSize) {
        super(
                Arrays.asList(
                        new Vektor3(-xSize, -ySize, zSize), // Ecke
                        new Vektor3(xSize, -ySize, zSize),
                        new Vektor3(xSize, ySize, zSize),
                        new Vektor3(-xSize, ySize, zSize),
                        new Vektor3(-xSize, -ySize, -zSize),
                        new Vektor3(xSize, -ySize, -zSize),
                        new Vektor3(xSize, ySize, -zSize),
                        new Vektor3(-xSize, ySize, -zSize)
                ),
                new int[][] { // Kanten
                        {0,1},{1,2},{2,3},{3,0},
                        {4,5},{5,6},{6,7},{7,4},
                        {0,4},{1,5},{2,6},{3,7}
                },
                new int[][] { // Flächen (als Vierecke)
                        {0,1,2,3}, {4,5,6,7},
                        {0,1,5,4}, {2,3,7,6},
                        {0,3,7,4}, {1,2,6,5}
                }
        );
    }
}
