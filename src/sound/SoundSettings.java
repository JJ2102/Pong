package sound;

// Hält die aktuellen Lautstärke-Einstellungen für Soundeffekte und Hintergrundmusik
public class SoundSettings {
    private float effectsVolume = 1.0f; // Lautstärke für Soundeffekte (0.0 bis 1.0)
    private float musicVolume = 0.1f; // Lautstärke für Hintergrundmusik (0.0 bis 1.0)

    // Effects Volume
    public float getEffectsVolume() {
        return effectsVolume;
    }

    public void setEffectsVolume(float effectsVolume) {
        this.effectsVolume = clamp(effectsVolume);
    }

    // Music Volume
    public float getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(float musicVolume) {
        this.musicVolume = clamp(musicVolume);
    }

    // Hilfsmethode zum Begrenzen der Lautstärke auf den Bereich [0.0, 1.0]
    private float clamp(float volume) {
        if (Float.isNaN(volume)) {
            return 0f;
        }
        return Math.clamp(volume, 0f, 1f);
    }
}
