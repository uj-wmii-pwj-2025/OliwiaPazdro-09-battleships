package battleships.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OpponentBoardTest {

    @Test
    void boardStartsWithAllUnknown() {
        OpponentBoard b = new OpponentBoard();
        String out = b.outputBoard();
        assertTrue(out.chars().allMatch(ch -> ch == '?' || ch == '\n'));
    }

    @Test
    void markWaterMarksCorrectCell() {
        OpponentBoard b = new OpponentBoard();
        b.markWater(new Coordinate(3, 4));
        assertEquals(CellState.WATER.c, b.outputBoard().split("\n")[3].charAt(4));
    }

    @Test
    void markHitMarksCorrectCell() {
        OpponentBoard b = new OpponentBoard();
        b.markHit(new Coordinate(2, 2));
        assertEquals(CellState.SHIP.c, b.outputBoard().split("\n")[2].charAt(2));
    }

    @Test
    void markSunkMarksWholeShip() {
        OpponentBoard b = new OpponentBoard();
        b.markHit(new Coordinate(5, 5));
        b.markHit(new Coordinate(5, 6));
        b.markHit(new Coordinate(5, 7));
        b.markSunk(new Coordinate(5, 6));

        String row = b.outputBoard().split("\n")[5];
        assertEquals("????.###.?".substring(0, 10), row);
    }

    @Test
    void markSunkMarksSurroundingWater() {
        OpponentBoard b = new OpponentBoard();
        b.markHit(new Coordinate(1, 1));
        b.markHit(new Coordinate(1, 2));

        b.markSunk(new Coordinate(1, 1));

        String out = b.outputBoard();
        assertEquals(CellState.WATER.c, out.split("\n")[0].charAt(0));
        assertEquals(CellState.WATER.c, out.split("\n")[2].charAt(3));
    }

    @Test
    void revealAllUnknownAsWaterReplacesAllUnknowns() {
        OpponentBoard b = new OpponentBoard();
        b.markHit(new Coordinate(0, 0));
        b.revealAllUnknownAsWater();

        String out = b.outputBoard();
        assertTrue(out.contains(String.valueOf(CellState.WATER.c)));
        assertFalse(out.contains("?"));
    }

}
