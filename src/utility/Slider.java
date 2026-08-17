package utility;

import javax.swing.JSlider;

// Eigener JSlider im Stil des Spiels, wird in den Einstellungen z.B. für die Lautstärke genutzt
public class Slider extends JSlider {
    // Erstellt den Slider mit Wertebereich und Startwert und richtet Ticks sowie Farben ein
    public Slider(int min, int max, int value) {
        super(min, max, value);
        setMajorTickSpacing((max - min) / 5); // Hauptstriche alle 20% des Bereichs
        setMinorTickSpacing((max - min) / 20); // Nebenticks alle 5% des Bereichs
        setPaintTicks(true); // Haupt- und Nebenticks anzeigen
        setPaintLabels(true); // Zahlenwerte anzeigen
        setBackground(Globals.getBackgroundColor()); // Hintergrundfarbe
        setForeground(Globals.getFontColor()); // Schriftfarbe
    }
}
