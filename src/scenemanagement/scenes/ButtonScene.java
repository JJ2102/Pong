package scenemanagement.scenes;

import scenemanagement.GameWindow;
import utility.Globals;

import javax.swing.JLabel;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

// Basis für alle Menü-Szenen, die aus einem Titel und untereinander zentrierten Buttons bestehen
public class ButtonScene extends Scene {
    // Alle Elemente in der Reihenfolge, in der sie angezeigt werden
    private final transient List<Component> components = new ArrayList<>();

    // Erstellt die Menü-Szene mit Hintergrundfarbe und der Überschrift als erstem Element
    public ButtonScene(GameWindow window, String title) {
        super(window);
        setLayout(new GridBagLayout()); // Zentrierte Inhalte
        setBackground(Globals.getBackgroundColor());

        // Titel im Spiel-Font erstellen und als erstes Element einreihen
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(Globals.getMainFont(72));
        titleLabel.setForeground(Globals.getFontColor());
        components.add(titleLabel);
    }

    // Reiht ein weiteres Element (z.B. Button oder Slider) unter den bisherigen ein
    protected void addComponent(Component component) {
        components.add(component);
    }

    // Fügt alle gesammelten Elemente untereinander zentriert ins Layout ein
    protected void positionComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // Alles landet in einer einzigen Spalte
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0); // 10px Abstand oben und unten zwischen den Elementen

        // Jedes Element bekommt seine eigene Zeile, von oben nach unten durchnummeriert
        int yPos = 0;
        for (Component component : components) {
            gbc.gridy = yPos++;
            add(component, gbc);
        }
    }

    // Menü-Szenen brauchen standardmäßig keine Update-Logik, Unterklassen können das überschreiben
    @Override
    protected void update() {
    }
}
