package Sound;

import javax.sound.sampled.*;
import java.io.File;
import java.util.HashMap;

public class SoundManager {
    HashMap<String, Clip> soundEffekts = new HashMap<>(); // Name -> Soundeffekt-Clip
    HashMap<String, Clip> backgroundMusik = new HashMap<>(); // Name -> HintergrundMusik-Clip
    private Clip currentBackgroundMusik; // aktuelle HintergrundMusik

    private final SoundSettings settings;

    public SoundManager() {
        settings = new SoundSettings();
    }

    private Clip createClip(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                AudioInputStream sound = AudioSystem.getAudioInputStream(file); // Audiodatei laden
                Clip clip = AudioSystem.getClip(); // Clip erstellen
                clip.open(sound); // Clip mit Audiodaten füllen
                return clip;
            } else {
                System.out.println("Fehler: Audiodatei nicht gefunden: " + path);
                throw new RuntimeException("Sound: File Not Found: " + path);
            }
        } catch (Exception e) {
            System.out.println("Fehler: " + path + ": \n" + e);
        }
        return null;
    }

    // ===== Volume =====
    private void setVolume(Clip clip, float volume) {
        if (clip == null) return;
        try {
            FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN); // Lautstärkeregler holen
            float dB = volumeToDB(volume, gain.getMinimum(), gain.getMaximum()); // Lautstärke in dB umrechnen
            gain.setValue(dB); // Lautstärke setzen
        } catch (IllegalArgumentException ignored) {
            // MASTER_GAIN nicht verfügbar ⇾ nichts tun
        }
    }

    private float volumeToDB(float volume, float min, float max) {
        float dB = (float) (20.0 * Math.log10(volume)); // dB-Wert berechnen
        return Math.clamp(dB, min, max); // dB-Wert begrenzen
    }

    // ===== Soundeffekte =====
    // Soundeffekt laden
    public void loadSoundEffekt(String name, String path) {
        Clip clip = createClip(path); // clip erstellen
        if (clip != null) {
            setVolume(clip, settings.getEffectsVolume());
            soundEffekts.put(name, clip); // Clip in die HashMap speichern
        }
    }

    // Soundeffekt abspielen
    public void playSoundEffekt(String name) {
        Clip clip = soundEffekts.get(name);
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop(); // Stoppe den Sound, wenn er bereits läuft
            }
            setVolume(clip, settings.getEffectsVolume());
            clip.setFramePosition(0);
            clip.start();
        }
    }

    // ===== Hintergrundmusik =====
    // Hintergrundmusik laden
    public void loadBackgroundMusik(String name, String path) {
        Clip clip = createClip(path);
        if (clip != null) {
            setVolume(clip, settings.getMusicVolume());
            backgroundMusik.put(name, clip);
        }
    }

    // Hintergrundmusik abspielen
    public void playBackgroundMusik(String name) {
        Clip clip = backgroundMusik.get(name);
        if (clip == null) return;

        // Stoppe und schließe aktuelle Musik sauber
        stopBackgroundMusik();

        setVolume(clip, settings.getMusicVolume());
        clip.setFramePosition(0); // Setze die Position auf den Anfang
        clip.loop(Clip.LOOP_CONTINUOUSLY); // Schleife die Musik endlos
        clip.start(); // Starte die Musik
        currentBackgroundMusik = clip;
    }

    // Hintergrundmusik stoppen
    public void stopBackgroundMusik() {
        if (currentBackgroundMusik != null) {
            if (currentBackgroundMusik.isRunning()) currentBackgroundMusik.stop();
            currentBackgroundMusik.close();
            currentBackgroundMusik = null;
        }
    }

    // ===== utility =====
    public void applyVolumes() {
        for (Clip clip : soundEffekts.values()) {
            setVolume(clip, settings.getEffectsVolume());
        }
        for (Clip clip : backgroundMusik.values()) {
            setVolume(clip, settings.getMusicVolume());
        }
        if (currentBackgroundMusik != null) setVolume(currentBackgroundMusik, settings.getMusicVolume());
    }

    public void setEffectsVolume(float v) {
        settings.setEffectsVolume(v);
        applyVolumes();
    }

    public void setMusicVolume(float v) {
        settings.setMusicVolume(v);
        applyVolumes();
    }

    public SoundSettings getSoundSettings() {
        return settings;
    }
}
