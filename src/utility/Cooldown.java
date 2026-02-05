package utility;

public class Cooldown {
    private final long duration;
    private long nextAllowed;

    public Cooldown(long durationMs) {
        this.duration = durationMs;
        this.nextAllowed = 0;
    }

    public boolean isReady() {
        return System.currentTimeMillis() >= nextAllowed;
    }

    public void trigger() {
        nextAllowed = System.currentTimeMillis() + duration;
    }

    public long getRemainingTime() {
        long remaining = nextAllowed - System.currentTimeMillis();
        return Math.max(remaining, 0);
    }
}
