package sceneManagement.overlays;

import sceneManagement.GameWindow;
import utility.Button;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Overlay extends JPanel implements KeyListener {
    //TODO: make it possible to close overlay by pressing esc
    protected final GameWindow window;
    private final ArrayList<Component> components = new ArrayList<>();
    private final int transparency;
    protected JLabel titleLabel;

    public Overlay(GameWindow window, String title, int transparency) {
        this.window = window;
        setOpaque(false); // Hintergrund transparent
        setLayout(new GridBagLayout()); // Zentrierte Inhalte

        this.transparency = Math.max(0, Math.min(transparency, 255)); // Transparenzwert begrenzen

        titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 72));
        titleLabel.setForeground(Color.WHITE);
        components.add(titleLabel);
    }

    protected void addButton(Button button) {
        components.add(button);
    }

    protected void positionComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0);

        int yPos = 0;
        for (Component component : components) {
            gbc.gridy = yPos++;
            add(component, gbc);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Halbtransparenter Hintergrund
        g.setColor(new Color(0, 0, 0, transparency));
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
