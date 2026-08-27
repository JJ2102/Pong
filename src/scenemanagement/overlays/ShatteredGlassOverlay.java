package scenemanagement.overlays;

import enums.OverlayType;
import pong.GameWindow;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Overlay, das einen zersplitterten Bildschirm zeichnet, wenn der Gegner einen Punkt macht
public class ShatteredGlassOverlay extends Overlay {
    private final transient List<Line2D> cracks = new ArrayList<>(); // alle Risslinien, die gezeichnet werden
    private final transient Random random = new Random(); // sorgt dafür, dass jeder Sprung anders aussieht

    // Erstellt ein komplett durchsichtiges Overlay ohne Titel, das keine Eingaben abfängt
    public ShatteredGlassOverlay(GameWindow window) {
        super(window, "", 0, false, OverlayType.SHATTERED_GLASS); // Transparenz 0 = kein eingefärbter Hintergrund
        setFocusable(false); // darf den Tastaturfokus nicht vom Spiel wegnehmen
    }

    // Erzeugt ein neues Sprungmuster rund um den übergebenen Einschlagpunkt
    public void generateShatter(int centerX, int centerY, int width, int height) {
        cracks.clear(); // altes Muster verwerfen
        int numRadialLines = 15 + random.nextInt(15); // Anzahl der Hauptstrahlen

        // Jeder Durchlauf erzeugt einen Strahl vom Einschlagpunkt nach außen
        for (int i = 0; i < numRadialLines; i++) {
            // Winkel mit etwas Zufall berechnen, damit die Strahlen nicht perfekt gleichmäßig verteilt sind
            double angle = (2 * Math.PI / numRadialLines) * i + (random.nextDouble() * 0.5);
            double length = Math.max(width / 2.0, height / 2.0); // Lang genug, um das Panel zu verlassen

            // Endpunkt des Strahls über Sinus und Cosinus aus Winkel und Länge bestimmen
            int endX = (int) (centerX + Math.cos(angle) * length);
            int endY = (int) (centerY + Math.sin(angle) * length);

            // 1. Radiale Hauptlinie hinzufügen
            cracks.add(new Line2D.Float(centerX, centerY, endX, endY));

            // 2. Querbrüche (Web-Effekt), die den Strahl mit seinem Nachbarn verbinden
            int numCrossCracks = 3 + random.nextInt(5); // Anzahl der Querlinien pro Strahl
            for (int j = 1; j <= numCrossCracks; j++) {
                double dist = j * (50 + random.nextInt(50)); // Distanz vom Zentrum wächst mit jedem Ring

                // Verbindung zur nächsten radialen Linie
                double nextAngle = (2 * Math.PI / numRadialLines) * (i + 1) + (random.nextDouble() * 0.5);

                // Start- und Endpunkt liegen auf beiden Strahlen im gleichen Abstand zum Zentrum
                int x1 = (int) (centerX + Math.cos(angle) * dist);
                int y1 = (int) (centerY + Math.sin(angle) * dist);
                int x2 = (int) (centerX + Math.cos(nextAngle) * dist);
                int y2 = (int) (centerY + Math.sin(nextAngle) * dist);

                cracks.add(new Line2D.Float(x1, y1, x2, y2));
            }
        }
    }

    // Zeichnet alle zuvor berechneten Risslinien über die Spielszene
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; // Grafics2D bietet Antialiasing und dickere Linien

        // Antialiasing für glatte Linien
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Glas-Optik: Hellgrau/Weiß mit leichter Transparenz
        g2.setColor(new Color(200, 230, 255, 150));
        g2.setStroke(new BasicStroke(1.2f)); // dünne Linien wirken eher wie feine Risse

        // Jede gespeicherte Linie einzeln zeichnen
        for (Line2D crack : cracks) {
            g2.draw(crack);
        }
    }
}
