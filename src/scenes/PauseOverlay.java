package scenes;

import enums.Scenes;
import utility.Button;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PauseOverlay extends JPanel implements KeyListener {
    private final GameWindow window;

    public PauseOverlay(GameWindow window) {
        this.window = window;
        setOpaque(false); // Hintergrund transparent
        setLayout(new GridBagLayout()); // Zentrierte Inhalte

        // Titel-Label
        JLabel title = new JLabel("Paused");
        title.setFont(new Font("Arial", Font.BOLD, 72));
        title.setForeground(Color.WHITE);

        // Buttons
        Button resumeBtn = new Button("Resume");
        Button menuBtn = new Button("Back to Menu");
        Button quitBtn = new Button("Quit Game");

        // Aktionen
        resumeBtn.addActionListener(_ -> window.togglePauseOverlay());
        menuBtn.addActionListener(_ -> {
            window.togglePauseOverlay();
            window.setCurrentScene(Scenes.MENU);
        });
        quitBtn.addActionListener(_ -> System.exit(0));

        // Layout mit GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 20, 0);

        gbc.gridy = 0;
        add(title, gbc);

        gbc.gridy = 1;
        add(resumeBtn, gbc);

        gbc.gridy = 2;
        add(menuBtn, gbc);

        gbc.gridy = 3;
        add(quitBtn, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Halbtransparenter Hintergrund
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            window.togglePauseOverlay(); // Overlay schließen
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
