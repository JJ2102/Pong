package scenemanagement.overlays;

import enums.OverlayType;
import pong.GameWindow;
import utility.ComponentColumn;
import utility.Globals;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Basisklasse für alle Overlays, also Menüs, die über einer Szene liegen.
 * Zeichnet einen halbtransparenten Hintergrund, zeigt einen Titel an und schließt sich mit ESC.
 */
public abstract class Overlay extends JPanel implements KeyListener {
    // Referenz auf das Hauptfenster, damit Unterklassen Szenen wechseln können
    protected final transient GameWindow window;

    // Titel und alle weiteren Elemente des Overlays in Anzeigereihenfolge
    private final transient ComponentColumn column;

    private final int transparency; // Alphawert des Hintergrunds (0 = unsichtbar, 255 = deckend)
    private final Color bgColor = Globals.getBackgroundColor();
    private final boolean pauseUnderlying; // Legt fest, ob die Szene darunter pausiert werden soll
    private final OverlayType overlayType;

    /**
     * Baut das Overlay mit Titel, Transparenz und Pausierverhalten auf.
     *
     * @param window Hauptfenster, über das Szenen und Overlays gewechselt werden
     * @param title Überschrift, die als erstes Element angezeigt wird
     * @param transparency Alphawert des Hintergrunds, wird auf 0 bis 255 begrenzt
     * @param pauseUnderlying {@code true}, wenn die Szene darunter pausiert werden soll
     * @param overlayType Typ, unter dem das Overlay registriert ist und mit ESC geschlossen wird
     */
    protected Overlay(GameWindow window, String title, int transparency, boolean pauseUnderlying,
                      OverlayType overlayType) {
        this.window = window;
        setOpaque(false); // Hintergrund transparent
        setLayout(new GridBagLayout()); // Zentrierte Inhalte

        addKeyListener(this);

        this.transparency = Math.clamp(transparency, 0, 255); // Transparenzwert begrenzen
        this.pauseUnderlying = pauseUnderlying;
        this.overlayType = overlayType;

        column = new ComponentColumn(title);
    }

    // ===== Aufbau des Overlays =====

    /**
     * Merkt sich ein weiteres Element, das später unter dem Titel angezeigt wird.
     *
     * @param component anzuzeigendes Element
     */
    protected void addComponent(Component component) {
        column.add(component);
    }

    /**
     * Ordnet alle gemerkten Elemente zentriert und untereinander an.
     */
    protected void positionComponents() {
        column.placeIn(this);
    }

    // ===== Getter =====

    /**
     * Gibt an, ob die Szene unter diesem Overlay pausiert werden soll.
     *
     * @return {@code true}, wenn die darunterliegende Szene pausiert werden soll, sonst {@code false}
     */
    public boolean shouldPauseUnderlying() {
        return pauseUnderlying;
    }

    // ===== Zeichnen =====

    /**
     * Zeichnet den halbtransparenten Hintergrund, durch den die Szene darunter noch durchscheint.
     *
     * @param g Grafikkontext, in den gezeichnet wird
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Halbtransparenter Hintergrund
        g.setColor(new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), transparency));
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    // ===== Tastenereignisse =====

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Schließt das Overlay, wenn ESC gedrückt wird.
     *
     * @param e Tastenereignis
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            window.toggleOverlay(overlayType);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
