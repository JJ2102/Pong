package sceneManagement;

import Sound.SoundManager;
import enums.EnumScenes;
import sceneManagement.overlays.DifficultyOverlay;
import sceneManagement.overlays.PauseOverlay;
import sceneManagement.scenes.GameScene;
import sceneManagement.scenes.MenuScene;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    private EnumScenes currentScene;
    private final Dimension SIZE;

    private boolean DEBUG = false;

    // Manager
    private final SceneManager sceneManager;
    private final SoundManager soundManager;

    // Overlays
    private PauseOverlay pauseOverlay;
    private DifficultyOverlay difficultyOverlay;

    // Scenen
    private MenuScene menuScene;
    private GameScene gameScene;

    public GameWindow(Dimension size) {
        super("Pong 3D"); // Fenstertitel
        SIZE = size; // Fenstergröße speichern

        // Manager initialisieren
        sceneManager = new SceneManager(SIZE);
        this.setContentPane(sceneManager.getLayeredPane());

        soundManager = new SoundManager();

        initManagers(); // Szenen initialisieren
        setDefaultWindowOptions();
        sceneManager.setScene(currentScene); // Startszene anzeigen
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initManagers() {
        // ===== sceneManager =====
        // Scenes Erstellen und registrieren
        menuScene = new MenuScene(this);
        gameScene = new GameScene(this);

        sceneManager.registerScene(EnumScenes.MENU, menuScene);
        sceneManager.registerScene(EnumScenes.GAME, gameScene);

        // Overlay erstellen (wird über SceneManager angezeigt/verdeckt)
        pauseOverlay = new PauseOverlay(this);
        difficultyOverlay = new DifficultyOverlay(this);

        currentScene = EnumScenes.MENU; // Standard-Szene festlegen

        // ===== soundManager =====
        // Soundeffekte laden
        soundManager.loadSoundEffekt("pong", "res/sounds/pong.wav");
        soundManager.loadSoundEffekt("score", "res/sounds/score.wav");

        // Hintergrundmusik laden und abspielen
        soundManager.loadBackgroundMusik("bg1", "res/musik/BgSong1.wav");
        soundManager.playBackgroundMusik("bg1");
    }

    private void setDefaultWindowOptions() {
        this.setUndecorated(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // start / stop Game
    public void startGame() {
        setCurrentScene(EnumScenes.GAME);
        gameScene.startScene();
    }

    public void returnToMenu() {
        if (gameScene.isRunning()) {
            gameScene.stopScene();
            gameScene.reset();
        }
        setCurrentScene(EnumScenes.MENU);
    }

    // Overlay Methoden
    public void togglePauseOverlay() {
        if (!sceneManager.isOverlayVisible(pauseOverlay)) {
            if (currentScene == EnumScenes.GAME && gameScene != null) { // Spielszene pausieren
                gameScene.stopScene();
            }
            sceneManager.showOverlay(pauseOverlay);
        } else {
            sceneManager.hideOverlay(pauseOverlay);
            if (currentScene == EnumScenes.GAME && gameScene != null) { // Spielszene fortsetzen
                gameScene.startScene();
            }
        }
    }

    public void toggleDifficultyOverlay() {
        if (!sceneManager.isOverlayVisible(difficultyOverlay)) {
            sceneManager.showOverlay(difficultyOverlay);
        } else {
            sceneManager.hideOverlay(difficultyOverlay);
        }
    }

    // Getters und Setters
    public void setCurrentScene(EnumScenes scene) {
        this.currentScene = scene;
        sceneManager.setScene(scene);
    }

    public EnumScenes getCurrentScene() {
        return currentScene;
    }

    public Dimension getSIZE() {
        return SIZE;
    }

    public GameScene getGameScene() {
        return gameScene;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public boolean isDebug() {
        return DEBUG;
    }

    public void toggleDebug() {
        this.DEBUG = !this.DEBUG;
    }
}
