package scenemanagement.scenes;

import pong.GameWindow;
import utility.ComponentColumn;
import utility.Globals;

import java.awt.Component;
import java.awt.GridBagLayout;

/**
 * Basis für alle Menü-Szenen, die aus einem Titel und untereinander zentrierten Buttons bestehen.
 */
public class ButtonScene extends Scene {
    // Titel und alle weiteren Elemente in der Reihenfolge, in der sie angezeigt werden
    private final transient ComponentColumn column;

    /**
     * Erstellt die Menü-Szene mit Hintergrundfarbe und der Überschrift als erstem Element.
     *
     * @param window Hauptfenster, zu dem die Szene gehört
     * @param title Überschrift der Szene
     */
    public ButtonScene(GameWindow window, String title) {
        super(window);
        setLayout(new GridBagLayout()); // Zentrierte Inhalte
        setBackground(Globals.getBackgroundColor());

        column = new ComponentColumn(title);
    }

    /**
     * Reiht ein weiteres Element (z. B. Button oder Slider) unter den bisherigen ein.
     *
     * @param component anzuzeigendes Element
     */
    protected void addComponent(Component component) {
        column.add(component);
    }

    /**
     * Fügt alle gesammelten Elemente untereinander zentriert ins Layout ein.
     */
    protected void positionComponents() {
        column.placeIn(this);
    }

    /**
     * Tut nichts, da Menü-Szenen standardmäßig keine Update-Logik brauchen; Unterklassen können das überschreiben.
     */
    @Override
    protected void update() {
    }
}
