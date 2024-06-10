import java.util.ArrayList;
import java.util.List;

/* Palette class representing the set of shapes the player can select
from will also refresh palette when all the shapes are used */
public class Palette {
    List<Shape> shapes = new ArrayList<>();
    List<Piece> pieces = new ArrayList<>();
    int paletteCellSize = 20;

    public Palette() {
        initialisePalette();
    }

    /* Initialises the palette with predefined shapes and their corresponding
    pieces, positioning them at specified x and y pixel coordinates */
    public void initialisePalette() {
        shapes.addAll(new ShapeSet().getShapes());
        int x = 10;
        int y = 475;
        for (Shape shape : shapes) {
            pieces.add(new Piece(shape, x, y, 40));
            // Use shape width and additional spacing for consistent spacing between shapes
            x += shape.getShapeWidth() * paletteCellSize + 75;
        }
    }

    // Return all pieces in palette
    public List<Piece> getPieces() {
        return pieces;
    }

    // Remove a piece from the palette
    public void removePiece(Piece piece) {
        pieces.remove(piece);
    }

    /* Refreshes the palette by clearing existing shapes/pieces
    and then initializes a new set of shapes */
    public void refresh() {
        shapes.clear();
        pieces.clear();
        initialisePalette();
    }
}
