import math.Vektor3;
import objekts.Panel;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Scene extends JPanel implements KeyListener {
    Timer t;
    Panel player;

    public Scene() {
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);

        initGame();
    }

    private void initGame() {
        player = new Panel(new Vektor3(100, 100, 0));

        t = new Timer(16, e -> update());
    }

    public void update() {
        repaint();
    }

    public void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        // Zeichnen des Spiels hier
        player.paintMe(g);
    }

    // ===== KeyListener Methoden =====
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
