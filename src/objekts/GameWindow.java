package objekts;

import enums.Scenes;
import scenes.GameScene;
import scenes.MenuScene;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private Scenes currentScene;
    private final Dimension SIZE;

    public GameWindow(Dimension size) {
        super("Pong 3D");
        SIZE = size;

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        this.add(mainPanel);

        initScenes(); // Szenen initialisieren
        showScene();  // Anfangsszene anzeigen

        // JFrame Einstellungen
        this.setSize(size);
        this.setUndecorated(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private void initScenes() {
        // MenuScene
        MenuScene menuScene = new MenuScene(this);
        mainPanel.add(menuScene, Scenes.MENU.name());

        // GameScene
        GameScene gameScene = new GameScene(this);
        mainPanel.add(gameScene, Scenes.GAME.name());

        currentScene = Scenes.MENU; // Standard-Szene festlegen
    }


    public void showScene() {
        cardLayout.show(mainPanel, currentScene.name());
    }

    // Getters und Setters
    public void setCurrentScene(Scenes scene) {
        this.currentScene = scene;
        showScene();

        Component activeScene = null;
        switch (scene) {
            case MENU -> activeScene = mainPanel.getComponent(0); // Menü-Szene
            case GAME -> activeScene = mainPanel.getComponent(1); // Spiel-Szene
        }
        if (activeScene != null) {
            activeScene.requestFocusInWindow(); // Fokus auf die aktive Szene setzen
        }
    }

    public Dimension getSIZE() {
        return SIZE;
    }
}
