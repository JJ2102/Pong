package scenemanagement;

import sound.SoundManager;
import enums.OverlayType;
import enums.SceneType;
import scenemanagement.overlays.*;
import scenemanagement.scenes.GameScene;
import scenemanagement.scenes.MenuScene;
import scenemanagement.scenes.SettingsScene;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    private SceneType currentScene; // speichert die aktuelle Szene
    private final Dimension windowSize;

    private boolean debug = false; // wird für debugausgaben genutzt

    // Manager
    private final SceneManager sceneManager;
    private final SoundManager soundManager;

    private ShatteredGlassOverlay shatteredGlassOverlay;

    private GameScene gameScene;

    public GameWindow(Dimension windowSize) {
        super("Pong 3D"); // Fenstertitel
        this.windowSize = windowSize; // Fenstergröße speichern

        // Manager initialisieren
        sceneManager = new SceneManager(this.windowSize);
        this.setContentPane(sceneManager.getLayeredPane());

        soundManager = new SoundManager();

        initManagers(); // Szenen und Sound laden

        // Fenster Einstellungen Setzen
        setDefaultWindowOptions();
        sceneManager.setScene(currentScene); // Startszene anzeigen
        pack();
        this.setLocationRelativeTo(null);
        setVisible(true);
    }

    // Erstellt und Läd die Szenen und die Sounds
    private void initManagers() {
        // ===== sceneManager =====
        // Scenen Erstellen und registrieren
        MenuScene menuScene = new MenuScene(this);
        sceneManager.registerScene(SceneType.MENU, menuScene);

        gameScene = new GameScene(this);
        sceneManager.registerScene(SceneType.GAME, gameScene);

        SettingsScene settingsScene = new SettingsScene(this);
        sceneManager.registerScene(SceneType.SETTINGS, settingsScene);

        // Overlay erstellen und registrieren
        PauseOverlay pauseOverlay = new PauseOverlay(this);
        sceneManager.registerOverlay(OverlayType.PAUSE, pauseOverlay);

        DifficultyOverlay difficultyOverlay = new DifficultyOverlay(this);
        sceneManager.registerOverlay(OverlayType.DIFFICULTY, difficultyOverlay);

        InfoOverlay infoOverlay = new InfoOverlay(this);
        sceneManager.registerOverlay(OverlayType.INFO, infoOverlay);

        WinOverlay winOverlay = new WinOverlay(this, OverlayType.WIN);
        sceneManager.registerOverlay(OverlayType.WIN, winOverlay);

        WinOverlay loseOverlay = new WinOverlay(this, OverlayType.LOSE);
        sceneManager.registerOverlay(OverlayType.LOSE, loseOverlay);

        shatteredGlassOverlay = new ShatteredGlassOverlay(this);
        sceneManager.registerOverlay(OverlayType.SHATTERED_GLASS, shatteredGlassOverlay);

        currentScene = SceneType.MENU; // Standard-Szene festlegen

        // ===== soundManager =====
        // Soundeffekte laden
        soundManager.loadSoundEffect("pong", "res/sounds/pong.wav");
        soundManager.loadSoundEffect("score", "res/sounds/score.wav");
        soundManager.loadSoundEffect("win", "res/sounds/win.wav");
        soundManager.loadSoundEffect("lose", "res/sounds/lose.wav");

        // Hintergrundmusik laden und abspielen
        soundManager.loadBackgroundMusic("bg1", "res/musik/BgSong1.wav");
        soundManager.playBackgroundMusic("bg1"); // Hintergrundmusik starten
    }

    // Setzt die Standardoptionen für das Fenster
    private void setDefaultWindowOptions() {
        this.setUndecorated(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Startet das Spiel
    public void startGame() {
        setCurrentScene(SceneType.GAME);
    }

    public void returnToMenu() {
        // versteckt alle overlays
        sceneManager.hideOverlay(OverlayType.PAUSE);
        sceneManager.hideOverlay(OverlayType.WIN);
        sceneManager.hideOverlay(OverlayType.LOSE);

        // Resetet das spiel für das nächste mal
        gameScene.reset();

        // zeigt Menu als Szene
        setCurrentScene(SceneType.MENU);
    }

    // Overlay Methoden
    // Falls das Overlay nicht sichtbar ist, zeige es an, sonst verstecke es
    public void toggleOverlay(OverlayType overlayID) {
        if (!sceneManager.isOverlayVisible(overlayID)) {
            sceneManager.showOverlay(overlayID);
        } else {
            sceneManager.hideOverlay(overlayID);
        }
    }

    // Falls show true ist, zeige das Overlay an, sonst verstecke es
    public void showOverlay(OverlayType overlayID, boolean show) {
        if (show) {
            sceneManager.showOverlay(overlayID); // zeige overlay an
        } else {
            sceneManager.hideOverlay(overlayID); // verstecke overlay
        }
    }

    // Getter und Setter
    public void setCurrentScene(SceneType scene) {
        this.currentScene = scene;
        sceneManager.setScene(scene);
    }

    public Dimension getWindowSize() {
        return windowSize;
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
        return debug;
    }

    public void toggleDebug() {
        this.debug = !this.debug;
    }
}
