package battleships.game;

import battleships.model.Coordinate;
import java.util.HashSet;
import java.util.Set;

public final class Ship {
    private final Set<Coordinate> segments = new HashSet<>();
    private final Set<Coordinate> hits = new HashSet<>();

    public void addSegment(Coordinate c) {
        segments.add(c);
    }

    public boolean occupies(Coordinate c) {
        return segments.contains(c);
    }

    public void hit(Coordinate c) {
            hits.add(c);
    }

    public boolean isSunk() {
        return hits.size() == segments.size();
    }
}
