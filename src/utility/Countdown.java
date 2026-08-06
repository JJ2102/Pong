package utility;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.LongConsumer;

public class Countdown {

    private final long durationMs;

    private long remainingMs;
    private long endTime;

    private final Timer timer;

    private final List<LongConsumer> tickListeners = new ArrayList<>();
    private final List<Runnable> finishListeners = new ArrayList<>();

    public Countdown(long durationMs) {
        this.durationMs = durationMs;

        this.timer = new Timer(100, _ -> update()); // Standard-Tick-Intervall: 100ms
    }

    private void update() {
        remainingMs = endTime - System.currentTimeMillis(); // berechnet die übrigen Ms

        if (remainingMs <= 0) { // Countdown beendet
            remainingMs = 0;

            timer.stop();

            for (Runnable r : finishListeners) {
                r.run(); // Finish-Listener ausführen
            }
        }

        for (LongConsumer l : tickListeners) {
            l.accept(remainingMs); // Aktuelle verbleibende Zeit in ms an die Listener übergeben
        }
    }

    // ===== Control =====
    public void start() {
        endTime = System.currentTimeMillis() + durationMs; // Endzeitpunkt berechnen
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    public void restart() {
        stop();
        start();
    }

    // ===== Listeners =====
    public void onTick(LongConsumer listener) {
        tickListeners.add(listener);
    }

    public void onFinish(Runnable listener) {
        finishListeners.add(listener);
    }

    // ===== Information =====
    public long getRemainingSeconds() {
        return (long) Math.ceil(remainingMs / 1000.0); // Aufrunden auf volle Sekunden
    }

    public boolean isRunning() {
        return timer.isRunning();
    }
}
