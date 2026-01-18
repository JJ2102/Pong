package Sound;

public class SoundSettings {
    private float effectsVolume = 1.0f; // Lautstärke für Soundeffekte (0.0 bis 1.0)
    private float musicVolume = 0.1f;   // Lautstärke für Hintergrundmusik (0.0 bis 1.0)
    private boolean muted = false;    // Stummschaltung

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

    // Muted
    public boolean isMuted() {
        return muted;
    }
    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    // Hilfsmethode zum Begrenzen der Lautstärke auf den Bereich [0.0, 1.0]
    private float clamp(float v) {
        if (Float.isNaN(v)) return 0f;
        return Math.max(0f, Math.min(1f, v));
    }
}
