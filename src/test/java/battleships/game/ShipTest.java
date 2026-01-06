package battleships.game;

import battleships.model.Coordinate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

    @Test
    void addSegmentAndOccupies() {
        Ship s = new Ship();
        Coordinate c = new Coordinate(0,0);
        s.addSegment(c);
        assertTrue(s.occupies(c));
    }

    @Test
    void hitMarksSegment() {
        Ship s = new Ship();
        Coordinate c = new Coordinate(1,1);
        s.addSegment(c);
        s.hit(c);
        assertTrue(s.isSunk());
    }

    @Test
    void notSunkUntilAllSegmentsHit() {
        Ship s = new Ship();
        Coordinate c1 = new Coordinate(2,2);
        Coordinate c2 = new Coordinate(2,3);
        s.addSegment(c1);
        s.addSegment(c2);
        s.hit(c1);
        assertFalse(s.isSunk());
    }
}
