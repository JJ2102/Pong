package sound;

/**
 * Hält die aktuellen Lautstärkeeinstellungen für Soundeffekte und Hintergrundmusik.
 * Alle Werte werden auf den Bereich 0.0 bis 1.0 begrenzt.
 */
public class SoundSettings {
    private float effectsVolume = 0.5f; // Lautstärke für Soundeffekte (0.0 bis 1.0)
    private float musicVolume = 0.1f; // Lautstärke für Hintergrundmusik (0.0 bis 1.0)

    // Effects Volume

    /**
     * Gibt die Lautstärke der Soundeffekte zurück.
     *
     * @return Lautstärke der Soundeffekte (0.0 bis 1.0)
     */
    public float getEffectsVolume() {
        return effectsVolume;
    }

    /**
     * Setzt die Lautstärke der Soundeffekte.
     *
     * @param effectsVolume neue Lautstärke, wird auf 0.0 bis 1.0 begrenzt
     */
    public void setEffectsVolume(float effectsVolume) {
        this.effectsVolume = clamp(effectsVolume);
    }

    // Music Volume

    /**
     * Gibt die Lautstärke der Hintergrundmusik zurück.
     *
     * @return Lautstärke der Hintergrundmusik (0.0 bis 1.0)
     */
    public float getMusicVolume() {
        return musicVolume;
    }

    /**
     * Setzt die Lautstärke der Hintergrundmusik.
     *
     * @param musicVolume neue Lautstärke, wird auf 0.0 bis 1.0 begrenzt
     */
    public void setMusicVolume(float musicVolume) {
        this.musicVolume = clamp(musicVolume);
    }

    /**
     * Begrenzt eine Lautstärke auf den Bereich [0.0, 1.0].
     *
     * @param volume zu begrenzende Lautstärke
     * @return begrenzte Lautstärke; {@code 0} bei {@code NaN}
     */
    private float clamp(float volume) {
        if (Float.isNaN(volume)) {
            return 0f;
        }
        return Math.clamp(volume, 0f, 1f);
    }
}
