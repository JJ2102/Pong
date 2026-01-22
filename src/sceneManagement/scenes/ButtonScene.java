package sceneManagement.scenes;

import sceneManagement.GameWindow;
import utility.Settings;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ButtonScene extends Scene {
    private final ArrayList<Component> components = new ArrayList<>();
    protected JLabel titleLabel;

    public ButtonScene(GameWindow window, String title) {
        super(window);
        setLayout(new GridBagLayout()); // Zentrierte Inhalte
        setBackground(Settings.getBackgroundColor());

        titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 72));
        titleLabel.setForeground(Settings.getFontColor());
        components.add(titleLabel);
    }

    protected void addComponent(Component component) {
        components.add(component);
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
    protected void update() {}
}
