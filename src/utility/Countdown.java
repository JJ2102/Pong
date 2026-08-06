package utility;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.LongConsumer;

// Rückwärtszähler auf Basis eines Swing-Timers, der andere Klassen per Listener über Ticks und das Ende informiert
public class Countdown {

    private final long durationMs; // Gesamtdauer eines Durchlaufs in Millisekunden

    private long remainingMs; // aktuell verbleibende Zeit in Millisekunden
    private long endTime; // Zeitpunkt (in ms), an dem der Countdown abläuft

    private final Timer timer; // ruft update() regelmäßig auf

    private final List<LongConsumer> tickListeners = new ArrayList<>(); // werden bei jedem Tick benachrichtigt
    private final List<Runnable> finishListeners = new ArrayList<>(); // werden einmal beim Ablaufen benachrichtigt

    // Legt die Dauer fest und erstellt den Timer, gestartet wird er erst in start()
    public Countdown(long durationMs) {
        this.durationMs = durationMs;

        this.timer = new Timer(100, _ -> update()); // Standard-Tick-Intervall: 100ms
    }

    // Wird bei jedem Tick vom Timer aufgerufen, aktualisiert die Restzeit und benachrichtigt die Listener
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
    // Startet den Countdown von vorne mit der vollen Dauer
    public void start() {
        endTime = System.currentTimeMillis() + durationMs; // Endzeitpunkt berechnen
        timer.start();
    }

    // Hält den Countdown an, die Restzeit bleibt dabei stehen
    public void stop() {
        timer.stop();
    }

    // Setzt den Countdown zurück und lässt ihn erneut komplett durchlaufen
    public void restart() {
        stop();
        start();
    }

    // ===== Listeners =====
    // Meldet einen Listener an, der bei jedem Tick die verbleibende Zeit in ms bekommt
    public void onTick(LongConsumer listener) {
        tickListeners.add(listener);
    }

    // Meldet einen Listener an, der einmal ausgeführt wird, sobald die Zeit abgelaufen ist
    public void onFinish(Runnable listener) {
        finishListeners.add(listener);
    }

    // ===== Information =====
    // Gibt die Restzeit in vollen Sekunden zurück, praktisch für die Anzeige
    public long getRemainingSeconds() {
        return (long) Math.ceil(remainingMs / 1000.0); // Aufrunden auf volle Sekunden
    }

    // Gibt zurück, ob der Countdown gerade läuft
    public boolean isRunning() {
        return timer.isRunning();
    }
}
