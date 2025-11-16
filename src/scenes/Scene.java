package scenes;

import objekts.GameWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public abstract class Scene extends JPanel implements KeyListener, MouseMotionListener {
    protected final GameWindow window;
    protected Timer timer;

    public Scene(GameWindow window) {
        this.window = window;
        setFocusable(true);
        addKeyListener(this);
        addMouseMotionListener(this);
        initScene();
    }

    // Lebenszyklus
    protected abstract void initScene();
    protected abstract void update();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    };

    public void startScene() {
        if (timer == null) {
            timer = new Timer(20, e -> {
                update();
                repaint();
            });
        }
        timer.start();
    }

    public void stopScene() {
        if (timer != null) {
            timer.stop();
        }
    }

    // Default Input-Handling
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyPressed(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void mouseDragged(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
}
