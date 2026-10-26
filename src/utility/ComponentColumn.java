package utility;

import javax.swing.JLabel;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

/**
 * Sammelt einen Titel und weitere Elemente, um sie später zentriert untereinander anzuordnen.
 * <p>
 * Wird von Menü-Szenen und Overlays genutzt, damit beide gleich aufgebaut sind.
 */
public class ComponentColumn {
    // Titel und alle weiteren Elemente in der Reihenfolge, in der sie angezeigt werden
    private final List<Component> components = new ArrayList<>();

    /**
     * Erstellt die Spalte mit einer Überschrift im Spiel-Font als erstem Element.
     *
     * @param title Überschrift über allen weiteren Elementen
     */
    public ComponentColumn(String title) {
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(Globals.getMainFont(72));
        titleLabel.setForeground(Globals.getFontColor());
        components.add(titleLabel);
    }

    /**
     * Reiht ein weiteres Element (z. B. Button oder Slider) unter den bisherigen ein.
     *
     * @param component anzuzeigendes Element
     */
    public void add(Component component) {
        components.add(component);
    }

    /**
     * Fügt alle gesammelten Elemente untereinander zentriert in den Container ein.
     *
     * @param container Ziel-Container, dessen Layout ein {@link java.awt.GridBagLayout} sein muss
     */
    public void placeIn(Container container) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // Alles landet in einer einzigen Spalte
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0); // 10 px Abstand oben und unten zwischen den Elementen

        // Jedes Element bekommt seine eigene Zeile, von oben nach unten durchnummeriert
        int yPos = 0;
        for (Component component : components) {
            gbc.gridy = yPos++;
            container.add(component, gbc);
        }
    }
}
