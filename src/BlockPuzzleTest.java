import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/* Unit tests for the BlockPuzzle game logic, covering piece placement, snapping a piece
to the grid, checking for full regions, score calculation, and detecting game over */
public class BlockPuzzleTest {
    private Grid grid;
    private Palette palette;

    @BeforeEach
    public void setUp() {
        grid = new Grid(9, 9);
        palette = new Palette();
    }

    @Test
    public void testPiecePlacement() {
        // Check if able to place all the pieces from the palette
        List<Piece> pieces = palette.getPieces();

        for (Piece piece : pieces) {
            assertTrue(grid.canPlace(piece.getShape()));
        }

        // Check if placing an invalid piece (1x10 horizontal shape) fails
        Shape invalidShape = new Shape(List.of(
                new Cell(0, 0), new Cell(0, 1), new Cell(0, 2),
                new Cell(0, 3), new Cell(0, 4), new Cell(0, 5),
                new Cell(0, 6), new Cell(0, 7), new Cell(0, 8),
                new Cell(0, 9)
        ), 10, 1);

        assertFalse(grid.canPlace(invalidShape));
    }

    @Test
    public void testPieceSnapToGrid() {
        Piece piece1 = palette.getPieces().get(0); // 1st piece in the palette
        // Modify x and y pixel coordinates to be inside the grid
        piece1.x = 180;
        piece1.y = 250;
        Shape snappedShape1 = piece1.snapToGrid();

        // Check if the snapped shape coordinates align with the grid
        assertNotNull(snappedShape1);

        Piece piece2 = palette.getPieces().get(0); // 2nd piece in the palette
        // Modify x and y pixel coordinates to be outside the grid
        piece2.x = 4;
        piece2.y = 22;
        Shape snappedShape2 = piece2.snapToGrid();

        // Check if the snapped shape coordinates do not align with the grid
        assertNull(snappedShape2);
    }

    @Test
    public void testFullRegions() {
        // Fill a row completely
        for (int col = 0; col < grid.getNumCols(); col++) {
            grid.grid[0][col] = 1;
        }
        assertTrue(grid.isRowFull(0, grid.copyGrid()));

        // Fill a column completely
        for (int row = 0; row < grid.getNumRows(); row++) {
            grid.grid[row][0] = 1;
        }
        assertTrue(grid.isColumnFull(0, grid.copyGrid()));

        // Fill a 3x3 square completely
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                grid.grid[row][col] = 1;
            }
        }
        assertTrue(grid.isSquareFull(0, 0, grid.copyGrid()));
    }

    @Test
    public void testScoreCalculation() {
        // Create a 1x9 piece to form a full row - starting from (0,0)
        Shape rowShape = new Shape(List.of(
                new Cell(0, 0), new Cell(0, 1), new Cell(0, 2),
                new Cell(0, 3), new Cell(0, 4), new Cell(0, 5),
                new Cell(0, 6), new Cell(0, 7), new Cell(0, 8)
        ), 9, 1);
        grid.placeShape(rowShape);

        // Clear the full row
        grid.clearFullRegions();

        // Create a 9x1 piece to form a full column - starting from (0,0)
        Shape columnShape = new Shape(List.of(
                new Cell(0, 0), new Cell(1, 0), new Cell(2, 0),
                new Cell(3, 0), new Cell(4, 0), new Cell(5, 0),
                new Cell(6, 0), new Cell(7, 0), new Cell(8, 0)
        ), 1, 9);
        grid.placeShape(columnShape);

        // Clear the full column
        grid.clearFullRegions();

        // Create a 3x3 square piece - starting from (0,0)
        Shape squareShape = new Shape(List.of(
                new Cell(0, 0), new Cell(0, 1), new Cell(0, 2),
                new Cell(1, 0), new Cell(1, 1), new Cell(1, 2),
                new Cell(2, 0), new Cell(2, 1), new Cell(2, 2)
        ), 3, 3);
        grid.placeShape(squareShape);

        // Clear the full 3x3 square
        grid.clearFullRegions();

        // The expected score based on the cleared regions
        int expectedScore = 3;

        // The actual score from after clearing regions
        int actualScore = grid.getScore();

        // Check that the actual score matches the expected score
        assertEquals(expectedScore, actualScore);
    }

    @Test
    public void testGameOver() {
        // Create a grid with all cells filled to simulate a game-over scenario
        Grid grid = new Grid(8, 8);
        for (int row = 0; row < grid.getNumRows(); row++) {
            for (int col = 0; col < grid.getNumCols(); col++) {
                // Mark the cell as filled
                grid.grid[row][col] = 1;
            }
        }

        /*
            - Shape 1 (I-shape, 1x4):
            shapes.add(new Shape(List.of(new Cell(0, 0),
                new Cell(1, 0), new Cell(2, 0), new Cell(3, 0)), 4, 1));

            - Shape 2 (T-shape, 2x3):
            shapes.add(new Shape(List.of(new Cell(0, 0),
                new Cell(1, 0), new Cell(2, 0), new Cell(1, 1)), 3, 2));

            - Shape 3 (O-shape, 2x2):
            shapes.add(new Shape(List.of(new Cell(0, 0),
                new Cell(1, 0), new Cell(0, 1), new Cell(1, 1)), 2, 2));
        */

        /* Check if the game over condition is correctly detected, by checking if any
        more pieces from the palette (shown above) can be placed on the grid */
        assertTrue(grid.isGameOver(palette));
    }
}
