import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    private Scene scene = new Scene();

    public Window (Dimension size) {
        this.setTitle("Pong 3D");

        this.add(scene);

        this.setSize(size);
        this.setUndecorated(true);
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
