import java.util.ArrayList;
import java.util.List;

/* ShapeSet class that creates all the different shapes
(made up of 4 cells) to be placed in the palette */
public class ShapeSet {

    public List<Shape> getShapes() {
        List<Shape> shapes = new ArrayList<>();

        // Shape 1 (I-shape, 1x4)
        shapes.add(new Shape(List.of(new Cell(0, 0),
                new Cell(1, 0), new Cell(2, 0), new Cell(3, 0)), 4, 1));

        // Shape 2 (T-shape, 2x3)
        shapes.add(new Shape(List.of(new Cell(0, 0),
                new Cell(1, 0), new Cell(2, 0), new Cell(1, 1)), 3, 2));

        // Shape 3 (O-shape, 2x2)
        shapes.add(new Shape(List.of(new Cell(0, 0),
                new Cell(1, 0), new Cell(0, 1), new Cell(1, 1)), 2, 2));

        return shapes;
    }
}
