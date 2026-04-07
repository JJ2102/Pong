package sceneManagement;

import Sound.SoundManager;
import enums.EnumOverlays;
import enums.EnumScenes;
import sceneManagement.overlays.*;
import sceneManagement.scenes.GameScene;
import sceneManagement.scenes.MenuScene;
import sceneManagement.scenes.SettingsScene;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    private EnumScenes currentScene;
    private final Dimension SIZE;

    private boolean DEBUG = false;

    // Manager
    private final SceneManager sceneManager;
    private final SoundManager soundManager;

    private ShatteredGlassOverlay shatteredGlassOverlay;

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
        // Scenen Erstellen und registrieren
        MenuScene menuScene = new MenuScene(this);
        sceneManager.registerScene(EnumScenes.MENU, menuScene);

        gameScene = new GameScene(this);
        sceneManager.registerScene(EnumScenes.GAME, gameScene);

        SettingsScene settingsScene = new SettingsScene(this);
        sceneManager.registerScene(EnumScenes.SETTINGS, settingsScene);

        // Overlay erstellen und registrieren
        PauseOverlay pauseOverlay = new PauseOverlay(this);
        sceneManager.registerOverlay(EnumOverlays.PAUSE, pauseOverlay);

        DifficultyOverlay difficultyOverlay = new DifficultyOverlay(this);
        sceneManager.registerOverlay(EnumOverlays.DIFFICULTY, difficultyOverlay);

        InfoOverlay infoOverlay = new InfoOverlay(this);
        sceneManager.registerOverlay(EnumOverlays.INFO, infoOverlay);

        WinOverlay winOverlay = new WinOverlay(this, EnumOverlays.WIN);
        sceneManager.registerOverlay(EnumOverlays.WIN, winOverlay);

        WinOverlay loseOverlay = new WinOverlay(this, EnumOverlays.LOSE);
        sceneManager.registerOverlay(EnumOverlays.LOSE, loseOverlay);

        shatteredGlassOverlay = new ShatteredGlassOverlay(this);
        sceneManager.registerOverlay(EnumOverlays.SHATTERED_GLASS, shatteredGlassOverlay);

        currentScene = EnumScenes.MENU; // Standard-Szene festlegen

        // ===== soundManager =====
        // Soundeffekte laden
        soundManager.loadSoundEffekt("pong", "res/sounds/pong.wav");
        soundManager.loadSoundEffekt("score", "res/sounds/score.wav");
        soundManager.loadSoundEffekt("win", "res/sounds/win.wav");
        soundManager.loadSoundEffekt("lose", "res/sounds/lose.wav");

        // Hintergrundmusik laden und abspielen
        soundManager.loadBackgroundMusik("bg1", "res/musik/BgSong1.wav");
        soundManager.playBackgroundMusik("bg1"); // Hintergrundmusik starten
    }

    private void setDefaultWindowOptions() {
        this.setUndecorated(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // start / stop Game
    public void startGame() {
        setCurrentScene(EnumScenes.GAME);
    }

    public void returnToMenu() {
        sceneManager.hideOverlay(EnumOverlays.PAUSE);
        sceneManager.hideOverlay(EnumOverlays.WIN);
        sceneManager.hideOverlay(EnumOverlays.LOSE);

        gameScene.reset();
        setCurrentScene(EnumScenes.MENU);
    }

    // Overlay Methoden
    public void toggleOverlay(EnumOverlays overlayID) {
        if (!sceneManager.isOverlayVisible(overlayID)) {
            sceneManager.showOverlay(overlayID);
        } else {
            sceneManager.hideOverlay(overlayID);
        }
    }

    public void showOverlay(EnumOverlays overlayID, boolean show) {
        if (show && !sceneManager.isOverlayVisible(overlayID)) {
            sceneManager.showOverlay(overlayID);
        } else if (sceneManager.isOverlayVisible(overlayID)) {
            sceneManager.hideOverlay(overlayID);
        }
    }

    // Getters und Setters
    public void setCurrentScene(EnumScenes scene) {
        this.currentScene = scene;
        sceneManager.setScene(scene);
    }

    public Dimension getSIZE() {
        return SIZE;
    }

    public GameScene getGameScene() {
        return gameScene;
    }

    public ShatteredGlassOverlay getShatteredGlassOverlay() {
        return shatteredGlassOverlay;
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
