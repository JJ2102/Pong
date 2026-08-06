package scenemanagement.scenes;

import scenemanagement.GameWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public abstract class Scene extends JPanel implements KeyListener, MouseMotionListener {
    protected final GameWindow window;
    private Timer timer;

    public Scene(GameWindow window) {
        this.window = window;
        setFocusable(true);
        addKeyListener(this);
        addMouseMotionListener(this);

        // Game-Loop: alle 20ms (~50 FPS) Zustand aktualisieren und neu zeichnen
        timer = new Timer(20, _ -> {
            update();
            repaint();
        });
    }

    // Lebenszyklus
    protected abstract void update();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void startScene() {
        timer.start();
    }

    public void stopScene() {
        if (timer != null) {
            timer.stop();
        }
    }

    public void onPause() {
        stopScene();
    }

    public void onResume() {
        startScene();
    }

    protected Timer getTimer() {
        return timer;
    }

    // Default Input-Handling
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyPressed(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void mouseDragged(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
}
