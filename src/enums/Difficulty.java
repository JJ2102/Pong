package enums;

public enum Difficulty {
    //TODO: adjust values as necessary
    EASY(0.1),
    MEDIUM(0.2),
    HARD(0.3),
    INSANE(0.9);

    private final double value;

    Difficulty(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
