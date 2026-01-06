package battleships.model;

public enum CommandType {
    START("start"),
    MISS("pudlo"),
    HIT("trafiony"),
    SUNK("trafiony zatopiony"),
    LAST_SUNK("ostatni zatopiony");

    private final String text;
    CommandType(String text) {
        this.text = text;
    }

    public String text() {
        return text;
    }

    public static CommandType fromText(String t) {

        for (CommandType ct : values())
            if (ct.text.equalsIgnoreCase(t.trim()))
                return ct;
        throw new IllegalArgumentException("Nieznana komenda: " + t);
    }
}
