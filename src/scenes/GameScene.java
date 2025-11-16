package scenes;

import math.Vektor3;
import objekts.Panel;
import utility.MouseSettings;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class GameScene extends Scene {
    Panel player;
    private Vektor3 mousePos = new Vektor3(0,0,0); // Aktuelle Mausposition

    public GameScene() {
        super();
        initScene();
        startScene();
    }

    @Override
    protected void initScene() {
        player = new Panel(new Vektor3(100, 100, 0));
        setCursor(MouseSettings.getInvisibleCursor());
    }

    public void update() {
        player.setPosition(mousePos);

        repaint();
    }

    public void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        // Zeichnen des Spiels hier
        player.paintMe(g);
    }

    // ===== KeyListener Methoden =====
    @Override
    public void mouseMoved(MouseEvent e) {
        double mouseShift =  (double) player.getSIZE() / 2;
        mousePos = new Vektor3(e.getX() - mouseShift, e.getY() - mouseShift, 0);
        player.setPosition(mousePos);
    }
}
