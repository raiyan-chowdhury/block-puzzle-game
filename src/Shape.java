import java.util.ArrayList;
import java.util.List;

// Shape class representing a group of cells forming a shape
public class Shape extends ArrayList<Cell> {
    int shapeWidth;
    int shapeHeight;

    public Shape(List<Cell> cells, int shapeWidth, int shapeHeight) {
        super(cells);
        this.shapeWidth = shapeWidth;
        this.shapeHeight = shapeHeight;
    }

    public int getShapeWidth() {
        return shapeWidth;
    }

    public int getShapeHeight() {
        return shapeHeight;
    }
}
