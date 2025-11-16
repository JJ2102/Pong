package scenes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public abstract class Scene extends JPanel implements KeyListener, MouseMotionListener {
    protected Timer timer;

    public Scene() {
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
        addMouseMotionListener(this);
    }

    // Lebenszyklus
    protected abstract void initScene();
    protected abstract void update();
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    };

    public void startScene() {
        timer = new Timer(20, e -> update());
        timer.start();
    }

    public void stopScene() {
        if (timer != null) timer.stop();
    }

    // Default Input-Handling
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void mouseDragged(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
}
