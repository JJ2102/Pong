import scenes.GameScene;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    public Window (Dimension size) {
        this.setTitle("Pong 3D");

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        initScenes();
        //this.setScene("GameScene");

        this.add(mainPanel);
        this.setSize(size);
        this.setUndecorated(true);
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initScenes() {
        // GameScene
        GameScene gameScene = new GameScene();
        mainPanel.add(gameScene, "GameScene");
    }

    public void setScene(String sceneName) {
        cardLayout.show(mainPanel, sceneName);
    }
}
