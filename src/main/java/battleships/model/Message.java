package battleships.model;

import java.util.Objects;

public record Message(CommandType command, Coordinate coordinate) {
    public Message(CommandType command, Coordinate coordinate) {

        this.command = Objects.requireNonNull(command);
        this.coordinate = coordinate;
    }

    public String serialize() {

        if (command == CommandType.LAST_SUNK)
            return command.text() + "\n";
        return command.text() + ";" + coordinate.toString() + "\n";
    }

    public static Message parse(String line) {

        line = line.trim();
        if (line.equalsIgnoreCase(CommandType.LAST_SUNK.text()))
            return new Message(CommandType.LAST_SUNK, null);

        String[] parts = line.split(";");
        if (parts.length != 2)
            throw new IllegalArgumentException("Zły format wiadomości: " + line);

        CommandType ct = CommandType.fromText(parts[0].trim());
        Coordinate c = Coordinate.parse(parts[1].trim());
        return new Message(ct, c);
    }

    @Override
    public String toString() {
        return serialize().trim();
    }

    public CommandType getCommandType() {
        return command;
    }
}
