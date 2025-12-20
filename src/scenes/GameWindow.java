package scenes;

import enums.Scenes;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private Scenes currentScene;
    private final Dimension SIZE;

    private PauseOverlay pauseOverlay;

    public GameWindow(Dimension size) {
        super("Pong 3D"); // Fenstertitel
        SIZE = size; // Fenstergröße speichern

        // Hauptpanel mit CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        this.add(mainPanel); // Hauptpanel zum JFrame hinzufügen

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
        /*
         * Szenen initialisieren und zum Hauptpanel hinzufügen
         */

        // MenuScene
        MenuScene menuScene = new MenuScene(this);
        mainPanel.add(menuScene, Scenes.MENU.name());

        // GameScene
        GameScene gameScene = new GameScene(this);
        mainPanel.add(gameScene, Scenes.GAME.name());

        // PauseScene
        pauseOverlay = new PauseOverlay(this);
        setGlassPane(pauseOverlay);
        pauseOverlay.setVisible(false);

        currentScene = Scenes.MENU; // Standard-Szene festlegen
    }


    public void showScene() {
        cardLayout.show(mainPanel, currentScene.name());
    }

    // Szene-Pause Overlay Methoden
    public void togglePauseOverlay() {
        pauseOverlay.setVisible(!pauseOverlay.isVisible());
        if (pauseOverlay.isVisible()) {
            pauseOverlay.requestFocusInWindow();
        } else {
            Component gameScene = mainPanel.getComponent(1); // idx 1 = GameScene
            gameScene.requestFocusInWindow();
        }
    }

    public boolean isPauseActive() {
        return pauseOverlay.isVisible();
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

    public Scenes getCurrentScene() {
        return currentScene;
    }

    public Dimension getSIZE() {
        return SIZE;
    }
}
