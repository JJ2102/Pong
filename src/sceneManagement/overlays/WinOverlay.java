package scenemanagement.overlays;

import enums.OverlayType;
import scenemanagement.GameWindow;
import utility.Button;

// Endbildschirm nach einer Partie, wird je nach übergebenem Typ als Sieg- oder Niederlage-Overlay genutzt
public class WinOverlay extends Overlay {
    // Erstellt das Overlay mit passendem Titel und den Buttons für Revanche oder Hauptmenü
    public WinOverlay(GameWindow window, OverlayType overlayType) {
        // Der Titel hängt davon ab, ob dieses Overlay als LOSE oder als WIN registriert wurde
        super(window, overlayType == OverlayType.LOSE ? "You Lose!" : "You Win!", 150, true, overlayType);

        // Buttons
        Button playAgainButton = new Button("Play Again");
        Button menuButton = new Button("Back to Menu");

        // Aktionen
        playAgainButton.addActionListener(_ -> {
            // Overlay schließen und direkt eine neue Runde mit demselben Schwierigkeitsgrad starten
            window.toggleOverlay(overlayType);
            window.getGameScene().restart();
        });
        menuButton.addActionListener(_ -> {
            // Overlay schließen und zurück ins Hauptmenü wechseln
            window.toggleOverlay(overlayType);
            window.returnToMenu();
        });

        addComponent(playAgainButton);
        addComponent(menuButton);

        positionComponents(); // ordnet Titel und Buttons untereinander an
    }
}
