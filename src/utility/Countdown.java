package utility;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Countdown {
    private final double durationMs;
    private double currentMs;
    private final Timer timer;
    private final List<ActionListener> listeners;
    private boolean isRunning;
    private final int delay; // Intervall des Timers in Millisekunden

    public Countdown(int seconds) {
        this.durationMs = seconds * 1000;
        this.delay = 1000; // 1 Sekunde
        this.listeners = new ArrayList<>();
        this.timer = createTimer();
    }

    public Countdown(double Seconds) {
        this.durationMs = Seconds * 1000;
        this.delay = 10; // 10 Millisekunden
        this.listeners = new ArrayList<>();
        this.timer = createTimer();
    }

    private Timer createTimer() {
        return new Timer(delay, e -> {
            currentMs -= delay;

            // Alle Listener informieren
            for (ActionListener listener : listeners) {
                listener.actionPerformed(new ActionEvent(this, 0, "tick:" + getCurrentTime()));
            }

            if (currentMs <= 0) {
                currentMs = 0;
                stop();
                for (ActionListener listener : listeners) {
                    listener.actionPerformed(new ActionEvent(this, 1, "finished"));
                }
            }
        });
    }

    // Start und Stopp
    public void start() {
        if (!isRunning) {
            currentMs = durationMs;
            timer.start();
            isRunning = true;
        }
    }

    public void stop() {
        timer.stop();
        isRunning = false;
    }

    public void restart() {
        stop();
        start();
    }

    public boolean isRunning() {
        return isRunning;
    }

    // Listener hinzufügen
    public void addTickListener(ActionListener listener) {
        listeners.add(listener);
    }

    // Getter
    /**
     * Gibt die verbleibende Zeit zurück.
     * Wenn mit int (Sekunden) initialisiert, ist der Wert glatt.
     * Wenn mit double (ms) initialisiert, sind die ms enthalten.
     */
    public double getCurrentTime() {
        if (delay == 1000) {
            return (currentMs / 1000); // Gibt ganze Sekunden zurück
        } else {
            return currentMs; // Gibt Millisekunden zurück
        }
    }
}
