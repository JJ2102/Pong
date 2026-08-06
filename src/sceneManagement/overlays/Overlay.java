package scenemanagement.overlays;

import scenemanagement.GameWindow;
import utility.Globals;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

// Basisklasse für alle Overlays (Menüs, die über einer Szene liegen) mit halbtransparentem Hintergrund
public abstract class Overlay extends JPanel {
    protected final GameWindow window; // Referenz auf das Hauptfenster, damit Unterklassen Szenen wechseln können
    private final ArrayList<Component> components = new ArrayList<>(); // alle Elemente des Overlays in Anzeigereihenfolge
    private final int transparency; // Alphawert des Hintergrunds (0 = unsichtbar, 255 = deckend)
    private JLabel titleLabel; // Überschrift, die immer ganz oben steht
    private final Color bgColor = Globals.getBackgroundColor();
    private final boolean pauseUnderlying; // legt fest, ob die Szene darunter pausiert werden soll

    // Baut das Overlay mit Titel, Transparenz und Pausierverhalten auf
    public Overlay(GameWindow window, String title, int transparency, boolean pauseUnderlying) {
        this.window = window;
        setOpaque(false); // Hintergrund transparent
        setLayout(new GridBagLayout()); // Zentrierte Inhalte

        this.transparency = Math.max(0, Math.min(transparency, 255)); // Transparenzwert begrenzen
        this.pauseUnderlying = pauseUnderlying;

        // Titel in großer Schrift erzeugen und als erstes Element eintragen
        titleLabel = new JLabel(title);
        titleLabel.setFont(Globals.getMainFont(72));
        titleLabel.setForeground(Globals.getFontColor());
        components.add(titleLabel);
    }

    // ===== Aufbau des Overlays =====

    // Merkt sich ein weiteres Element, das später unter dem Titel angezeigt wird
    protected void addComponent(Component component) {
        components.add(component);
    }

    // Ordnet alle gemerkten Elemente zentriert und untereinander an
    protected void positionComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // alles in einer einzigen Spalte
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0); // etwas Abstand nach oben und unten

        // Jedes Element bekommt eine eigene Zeile, von oben nach unten
        int yPos = 0;
        for (Component component : components) {
            gbc.gridy = yPos++;
            add(component, gbc);
        }
    }

    // ===== Getter =====

    public boolean shouldPauseUnderlying() {
        return pauseUnderlying;
    }

    // ===== Zeichnen =====

    // Zeichnet den halbtransparenten Hintergrund, durch den die Szene darunter noch durchscheint
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Halbtransparenter Hintergrund
        g.setColor(new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), transparency));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
