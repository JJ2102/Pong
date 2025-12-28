package enums;

public enum Difficulty {
    EASY(0.1),
    MEDIUM(0.2),
    HARD(0.5),
    INSANE(0.9);

    private final double value;

    Difficulty(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
