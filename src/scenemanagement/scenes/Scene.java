package scenemanagement.scenes;

import pong.GameWindow;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

// Basisklasse für alle Szenen, kümmert sich um Game-Loop, Zeichenfläche und Eingaben
public abstract class Scene extends JPanel implements KeyListener, MouseMotionListener {
    protected final GameWindow window; // Referenz auf das Hauptfenster, um z.B. Szenen zu wechseln
    private final Timer timer; // Taktgeber der Szene, ruft update() und repaint() auf

    // Richtet die Szene ein: Fokus für Tastatur, Maus-Listener und den Game-Loop-Timer
    public Scene(GameWindow window) {
        this.window = window;
        setFocusable(true); // Ohne Fokus kommen keine Tastatureingaben an
        addKeyListener(this);
        addMouseMotionListener(this);

        // Game-Loop: alle 20ms (~50 FPS) Zustand aktualisieren und neu zeichnen
        timer = new Timer(20, _ -> {
            update();
            repaint();
        });
    }

    // ===== Lebenszyklus =====

    // Wird bei jedem Timer-Tick aufgerufen, jede Szene füllt das mit ihrer eigenen Logik
    protected abstract void update();

    // Zeichnet den Hintergrund der Szene, abgeleitete Klassen zeichnen darüber ihre Objekte
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    // Startet den Game-Loop der Szene
    public void startScene() {
        timer.start();
    }

    // Stoppt den Game-Loop der Szene
    public void stopScene() {
        timer.stop();
    }

    // Wird vom SceneManager aufgerufen, wenn die Szene verdeckt oder verlassen wird
    public void onPause() {
        stopScene();
    }

    // Wird vom SceneManager aufgerufen, wenn die Szene wieder sichtbar/aktiv wird
    public void onResume() {
        startScene();
    }

    protected Timer getTimer() {
        return timer;
    }

    // ===== Default Input-Handling =====
    // Leere Standardimplementierungen, damit Unterklassen nur überschreiben, was sie brauchen
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
