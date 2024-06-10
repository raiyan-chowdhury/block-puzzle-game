import java.awt.*;
import java.util.List;
import java.util.ArrayList;

/* Piece class representing a shape placed on the grid at a given
location in pixel coordinates, with specified cell size */
public class Piece {
    // (7,30) = top-left corner x and y coordinates of frame
    final int FRAME_TOP_LEFT_X = 7;
    final int FRAME_TOP_LEFT_Y = 30;
    // yOffset = 55
    final int Y_OFFSET = 55;

    Shape shape;
    // Top left of piece
    int x, y, cellSize;
    PieceState state = PieceState.IN_PALETTE;

    public Piece(Shape shape, int x, int y, int cellSize) {
        this.shape = shape;
        this.x = x;
        this.y = y;
        this.cellSize = cellSize;
    }

    public Shape getShape() {
        return shape;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /* Checks if the given pixel coordinates (Point) are within the boundaries of the piece.
    Adjusts for the frame's top-left corner and considers the palette cell size. */
    public boolean contains(Point point, int cellSize) {
        int palettePieceWidth = shape.getShapeWidth() * cellSize;
        int palettePieceHeight = shape.getShapeHeight() * cellSize;

        return (point.x >= this.x + FRAME_TOP_LEFT_X && point.x <= this.x + FRAME_TOP_LEFT_X + palettePieceWidth &&
                point.y >= this.y + FRAME_TOP_LEFT_Y && point.y <= this.y + FRAME_TOP_LEFT_Y + palettePieceHeight);
    }

    /* Snaps the piece to the nearest grid position, adjusting for the frame and cell size.
    Returns a new shape representing the snapped position, or null if outside the grid. */
    public Shape snapToGrid() {
        // Adjust for default top-left y coordinate
        int yOffset = Y_OFFSET - FRAME_TOP_LEFT_Y;

        // Adjust for the frame's top-left corner
        int adjustedX = x - FRAME_TOP_LEFT_X;
        int adjustedY = y - FRAME_TOP_LEFT_Y;

        // Adjust for the yOffset
        int relativeY = adjustedY - yOffset;

        // Calculate the grid cell coordinates based on the top-left position of the piece
        int cellX = (int) Math.floor((double) adjustedX / cellSize);
        int cellY = (int) Math.floor((double) relativeY / cellSize);

        // Ensure the snapped cells are inside the grid
        if (cellX >= 0 && cellX <= 8 && cellY >= 0 && cellY <= 8) {
            // Create a new shape with cells adjusted to the grid coordinates
            List<Cell> snappedCells = new ArrayList<>();
            for (Cell cell : shape) {
                int snappedX = cellX + cell.x();
                int snappedY = cellY + cell.y();
                snappedCells.add(new Cell(snappedX, snappedY));
            }
            return new Shape(snappedCells, shape.getShapeWidth(), shape.getShapeHeight());
        } else {
            return null;
        }
    }
}
