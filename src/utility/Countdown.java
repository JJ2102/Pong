package utility;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Countdown {
    private final int durationSeconds;
    private int currentSecond;
    private final Timer timer;
    private final List<ActionListener> listeners;
    private boolean isRunning;

    public Countdown(int seconds) {
        this.durationSeconds = seconds;
        this.listeners = new ArrayList<>();
        this.timer = new Timer(1000, e -> {
            currentSecond--;

            // Alle Listener über jeden Tick informieren
            for (ActionListener listener : listeners) {
                listener.actionPerformed(new ActionEvent(this, 0, "tick:" + currentSecond));
            }

            if (currentSecond <= 0) {
                stop();
                // Alle Listener über das Ende informieren
                for (ActionListener listener : listeners) {
                    listener.actionPerformed(new ActionEvent(this, 1, "finished"));
                }
            }
        });
    }

    // Start und Stopp
    public void start() {
        if (!isRunning) {
            currentSecond = durationSeconds;
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
    public int getCurrentSecond() {
        return currentSecond;
    }
}
