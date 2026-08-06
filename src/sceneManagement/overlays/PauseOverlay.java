package scenemanagement.overlays;

import enums.OverlayType;
import scenemanagement.GameWindow;
import utility.Button;

// Pausemenü, das während des Spiels mit ESC eingeblendet wird
public class PauseOverlay extends Overlay {
    // Erstellt die Buttons zum Fortsetzen, für den Rücksprung ins Menü und zum Beenden
    public PauseOverlay(GameWindow window) {
        super(window, "Paused", 150, true); // pausiert das laufende Spiel im Hintergrund

        // Buttons
        Button resumeButton = new Button("Resume");
        Button menuButton = new Button("Back to Menu");
        Button quitButton = new Button("Quit Game");

        // Aktionen
        resumeButton.addActionListener(_ -> window.toggleOverlay(OverlayType.PAUSE)); // Overlay ausblenden, Spiel läuft weiter
        menuButton.addActionListener(_ -> {
            // Erst das Pausemenü schließen, dann das Spiel zurücksetzen und ins Hauptmenü wechseln
            window.toggleOverlay(OverlayType.PAUSE);
            window.returnToMenu();
        });
        quitButton.addActionListener(_ -> System.exit(0)); // beendet das Programm sofort

        addComponent(resumeButton);
        addComponent(menuButton);
        addComponent(quitButton);

        positionComponents(); // ordnet Titel und Buttons untereinander an
    }
}
