package scenemanagement.scenes;

import pong.GameWindow;
import sound.SoundSettings;
import utility.Button;
import utility.InfoLabel;
import utility.Slider;

// Einstellungs-Szene, in der die Lautstärke von Soundeffekten und Musik geregelt wird
public class SettingsScene extends ButtonScene {
    // Baut die Einstellungsseite mit den beiden Lautstärke-Reglern und dem Zurück-Button auf
    public SettingsScene(GameWindow window) {
        super(window, "Settings");

        // Aktuelle Lautstärken aus den Sound-Einstellungen holen (0.0 - 1.0) und als Startwerte der Slider nutzen
        SoundSettings soundSettings = window.getSoundManager().getSoundSettings();
        int effectsVolume = (int) (soundSettings.getEffectsVolume() * 100); // Effekte Lautstärke in %
        int musicVolume = (int) (soundSettings.getMusicVolume() * 100); // Musik Lautstärke in %

        // Regler für die Soundeffekte
        InfoLabel volumeEffectsLabel = new InfoLabel("Volume Effects:");
        Slider volumeEffectsSlider = new Slider(0, 100, effectsVolume);
        volumeEffectsSlider.addChangeListener(_ -> {
            float volume = volumeEffectsSlider.getValue() / 100f; // Prozent zurück in 0.0 - 1.0 umrechnen
            window.getSoundManager().setEffectsVolume(volume);
            // Kurze Hörprobe, damit man die neue Lautstärke direkt merkt
            window.getSoundManager().playSoundEffect("pong");
        });

        // Regler für die Hintergrundmusik
        InfoLabel volumeMusicLabel = new InfoLabel("Volume Music:");
        Slider volumeMusicSlider = new Slider(0, 100, musicVolume);
        volumeMusicSlider.addChangeListener(_ -> {
            float volume = volumeMusicSlider.getValue() / 100f;
            window.getSoundManager().setMusicVolume(volume); // Wirkt sofort auf die laufende Musik
        });

        Button exitButton = new Button("Back to Menu");
        exitButton.addActionListener(_ -> window.returnToMenu());

        // Elemente in der gewünschten Reihenfolge einreihen
        addComponent(volumeEffectsLabel);
        addComponent(volumeEffectsSlider);

        addComponent(volumeMusicLabel);
        addComponent(volumeMusicSlider);

        addComponent(exitButton);

        positionComponents(); // Elemente untereinander zentriert anordnen
    }
}
