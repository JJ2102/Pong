package sceneManagement.overlays;

import sceneManagement.GameWindow;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;

public class ShatteredGlassOverlay extends Overlay {
    private final ArrayList<Line2D> cracks = new ArrayList<>();
    private final Random random = new Random();

    public ShatteredGlassOverlay(GameWindow window) {
        super(window, "", 0, false);
        setFocusable(false);
    }

    public void generateShatter(int centerX, int centerY, int width, int height) {
        cracks.clear();
        int numRadialLines = 15 + random.nextInt(15); // Anzahl der Hauptstrahlen

        for (int i = 0; i < numRadialLines; i++) {
            // Winkel mit etwas Zufall berechnen
            double angle = (2 * Math.PI / numRadialLines) * i + (random.nextDouble() * 0.5);
            double length = Math.max(width/2, height/2); // Lang genug, um das Panel zu verlassen

            int endX = (int) (centerX + Math.cos(angle) * length);
            int endY = (int) (centerY + Math.sin(angle) * length);

            // 1. Radiale Hauptlinie hinzufügen
            cracks.add(new Line2D.Float(centerX, centerY, endX, endY));

            // 2. Querbrüche (Web-Effekt)
            int numCrossCracks = 3 + random.nextInt(5);
            for (int j = 1; j <= numCrossCracks; j++) {
                double dist = j * (50 + random.nextInt(50)); // Distanz vom Zentrum

                // Verbindung zur nächsten radialen Linie
                double nextAngle = (2 * Math.PI / numRadialLines) * (i + 1) + (random.nextDouble() * 0.5);

                int x1 = (int) (centerX + Math.cos(angle) * dist);
                int y1 = (int) (centerY + Math.sin(angle) * dist);
                int x2 = (int) (centerX + Math.cos(nextAngle) * dist);
                int y2 = (int) (centerY + Math.sin(nextAngle) * dist);

                cracks.add(new Line2D.Float(x1, y1, x2, y2));
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Antialiasing für glatte Linien
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Glas-Optik: Hellgrau/Weiß mit leichter Transparenz
        g2.setColor(new Color(200, 230, 255, 150));
        g2.setStroke(new BasicStroke(1.2f));

        for (Line2D crack : cracks) {
            g2.draw(crack);
        }
    }
}
