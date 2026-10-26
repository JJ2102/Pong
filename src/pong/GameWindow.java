package pong;

import enums.OverlayType;
import enums.SceneType;
import scenemanagement.SceneManager;
import scenemanagement.overlays.DifficultyOverlay;
import scenemanagement.overlays.InfoOverlay;
import scenemanagement.overlays.PauseOverlay;
import scenemanagement.overlays.ShatteredGlassOverlay;
import scenemanagement.overlays.WinOverlay;
import scenemanagement.scenes.GameScene;
import scenemanagement.scenes.MenuScene;
import scenemanagement.scenes.SettingsScene;
import sound.SoundManager;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;
import java.util.List;

/**
 * Hauptfenster des Spiels.
 * Verwaltet über den {@link SceneManager} alle Szenen und Overlays und über den
 * {@link SoundManager} die Soundausgabe.
 */
public class GameWindow extends JFrame {
    private final Dimension windowSize;

    // Manager
    private final transient SceneManager sceneManager;
    private final transient SoundManager soundManager;

    private boolean debug = false; // Wird für Debugausgaben genutzt

    private ShatteredGlassOverlay shatteredGlassOverlay;

    private GameScene gameScene;

    /**
     * Initialisiert das Hauptfenster, die Manager und lädt die Ressourcen.
     * <p>
     * Am Ende wird das Fenster zentriert und sichtbar gemacht; als erste Szene wird das Hauptmenü angezeigt.
     *
     * @param windowSize Größe des Fensters (in der Regel die Bildschirmgröße)
     * @throws IllegalArgumentException wenn ein Fenster-Icon nicht im Classpath gefunden wird
     */
    public GameWindow(Dimension windowSize) {
        super("Pong 3D");
        this.windowSize = windowSize;

        // Fenster-Icon in mehreren Größen setzen; Windows wählt die passende für Titelleiste, Taskleiste und Alt-Tab
        setIconImages(List.of(
                loadIcon("/icon/pong-16.png"),
                loadIcon("/icon/pong-32.png"),
                loadIcon("/icon/pong-64.png")
        ));

        // Manager initialisieren
        sceneManager = new SceneManager(this.windowSize);
        // JLayeredPane als Container setzen, um Ebenen zu ermöglichen
        setContentPane(sceneManager.getLayeredPane());

        soundManager = new SoundManager();

        initManagers(); // Szenen und Sound laden

        setDefaultWindowOptions();
        pack(); // Fenstergröße an den Inhalt anpassen
        setLocationRelativeTo(null); // Fenster auf dem Bildschirm zentrieren
        setVisible(true);
    }

    /**
     * Lädt ein Fenster-Icon aus dem Classpath.
     *
     * @param resourcePath Pfad der Bilddatei im Classpath (z. B. {@code "/icon/pong-16.png"})
     * @return geladenes Icon-Bild
     * @throws IllegalArgumentException wenn die Ressource nicht gefunden wird
     */
    private Image loadIcon(String resourcePath) {
        URL url = GameWindow.class.getResource(resourcePath);
        if (url == null) {
            throw new IllegalArgumentException("Icon nicht gefunden: " + resourcePath);
        }
        return new ImageIcon(url).getImage();
    }

    /**
     * Erstellt und registriert alle verfügbaren Szenen und Overlays sowie die Soundeffekte.
     * <p>
     * Zusätzlich wird das Hauptmenü als aktive Szene gesetzt und die Hintergrundmusik gestartet.
     */
    private void initManagers() {
        // Szenen erstellen und beim SceneManager registrieren
        MenuScene menuScene = new MenuScene(this);
        sceneManager.registerScene(SceneType.MENU, menuScene);

        gameScene = new GameScene(this);
        sceneManager.registerScene(SceneType.GAME, gameScene);

        SettingsScene settingsScene = new SettingsScene(this);
        sceneManager.registerScene(SceneType.SETTINGS, settingsScene);

        // Overlays (für Menüs über dem Spiel) erstellen und registrieren
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

        sceneManager.setScene(SceneType.MENU);

        // Kurze Soundeffekte in den Speicher laden
        soundManager.loadSoundEffect("pong", "/sounds/pong.wav");
        soundManager.loadSoundEffect("score", "/sounds/score.wav");
        soundManager.loadSoundEffect("win", "/sounds/win.wav");
        soundManager.loadSoundEffect("lose", "/sounds/lose.wav");

        // Hintergrundmusik laden und sofort abspielen (läuft standardmäßig in Schleife)
        soundManager.loadBackgroundMusic("bg1", "/musik/BgSong1.wav");
        soundManager.playBackgroundMusic("bg1");
    }

    /**
     * Konfiguriert das Fenster: ohne Rahmen und Titelleiste, nicht vergrößerbar,
     * und beim Schließen wird das Programm beendet.
     */
    private void setDefaultWindowOptions() {
        setUndecorated(true); // Windows-Titelleiste ausblenden
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Startet das Spiel durch Wechsel zur Spielszene.
     */
    public void startGame() {
        sceneManager.setScene(SceneType.GAME);
    }

    /**
     * Setzt das Spiel zurück und kehrt zum Hauptmenü zurück.
     * <p>
     * Dabei werden das Pause-, Sieg- und Niederlage-Overlay ausgeblendet und
     * Punktestand sowie Positionen der {@link GameScene} zurückgesetzt.
     */
    public void returnToMenu() {
        // Alle aktiven Spiel-Overlays ausblenden
        sceneManager.hideOverlay(OverlayType.PAUSE);
        sceneManager.hideOverlay(OverlayType.WIN);
        sceneManager.hideOverlay(OverlayType.LOSE);

        // Punktestand und Positionen zurücksetzen
        gameScene.reset();

        sceneManager.setScene(SceneType.MENU);
    }

    /**
     * Wechselt zur Einstellungsszene.
     */
    public void showSettings() {
        sceneManager.setScene(SceneType.SETTINGS);
    }

    // ===== Overlay-Methoden =====

    /**
     * Schaltet die Sichtbarkeit eines Overlays um (an/aus).
     *
     * @param overlayType Typ des Overlays, das umgeschaltet werden soll
     */
    public void toggleOverlay(OverlayType overlayType) {
        if (!sceneManager.isOverlayVisible(overlayType)) {
            sceneManager.showOverlay(overlayType);
        } else {
            sceneManager.hideOverlay(overlayType);
        }
    }

    /**
     * Zeigt oder versteckt ein bestimmtes Overlay explizit.
     *
     * @param overlayType Typ des Overlays
     * @param show {@code true}, um das Overlay anzuzeigen, {@code false}, um es zu verstecken
     */
    public void showOverlay(OverlayType overlayType, boolean show) {
        if (show) {
            sceneManager.showOverlay(overlayType);
        } else {
            sceneManager.hideOverlay(overlayType);
        }
    }

    // ===== Getter und Setter =====

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

    /**
     * Schaltet den Debug-Modus ein bzw. aus.
     */
    public void toggleDebug() {
        this.debug = !this.debug;
    }
}
