package Sound;

import javax.sound.sampled.*;
import java.io.File;
import java.util.HashMap;

public class SoundManager {
    HashMap<String, Clip> soundEffekts = new HashMap<>();
    HashMap<String, Clip> backgroundMusik = new HashMap<>();
    private Clip currentBackgroundMusik;

    private final SoundSettings settings;

    public SoundManager() {
        settings = new SoundSettings();
    }

    private Clip createClip(String path) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path)); // AudioInputStream mit rohdaten erstellen
            DataLine.Info info = new DataLine.Info(Clip.class, audioInputStream.getFormat()); // Clip Info erstellen
            Clip clip = (Clip) AudioSystem.getLine(info); // Leeren Clip erstellen
            clip.open(audioInputStream); // Clip mit Sounddaten füllen
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setVolume(Clip clip, boolean isMusic) {
        if (clip == null) return;
        try {
            FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float vol = settings.isMuted() ? 0f : (isMusic ? settings.getMusicVolume() : settings.getEffectsVolume());
            if (vol <= 0f) { // Stumm
                gain.setValue(gain.getMinimum());
            } else { // volume in dB umrechnen
                float dB = (float) (20.0 * Math.log10(vol)); // dB-Wert berechnen
                dB = Math.max(gain.getMinimum(), Math.min(gain.getMaximum(), dB)); // dB-Wert begrenzen
                gain.setValue(dB); // Lautstärke setzen
            }
        } catch (IllegalArgumentException ignored) {
            // MASTER_GAIN nicht verfügbar -> nichts tun
        }
    }

    // Soundeffekt laden
    public void loadSoundEffekt(String name, String path) {
        Clip clip = createClip(path);
        if (clip != null) {
            setVolume(clip, false);
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
            setVolume(clip, false);
            clip.setFramePosition(0);
            clip.start();
        }
    }

    // Hintergrundmusik laden
    public void loadBackgroundMusik(String name, String path) {
        Clip clip = createClip(path);
        if (clip != null) {
            setVolume(clip, true);
            backgroundMusik.put(name, clip);
        }
    }

    // Hintergrundmusik abspielen
    public void playBackgroundMusik(String name) {
        Clip clip = backgroundMusik.get(name);
        if (clip == null) return;

        // Stoppe und schließe aktuelle Musik sauber
        stopBackgroundMusik();

        setVolume(clip, true);
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

    private void applyVolumes() {
        for (Clip clip : soundEffekts.values()) {
            setVolume(clip, false);
        }
        for (Clip clip : backgroundMusik.values()) {
            setVolume(clip, true);
        }
        if (currentBackgroundMusik != null) setVolume(currentBackgroundMusik, true);
    }

    public void setEffectsVolume(float v) {
        settings.setEffectsVolume(v);
        applyVolumes();
    }

    public void setMusicVolume(float v) {
        settings.setMusicVolume(v);
        applyVolumes();
    }

    public void setMuted(boolean muted) {
        settings.setMuted(muted);
    }

    public SoundSettings getSoundSettings() {
        return settings;
    }
}
