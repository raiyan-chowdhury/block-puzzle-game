import java.util.ArrayList;
import java.util.List;

/* Grid class representing the game grid and its logic,
including updating the score and checking for game over */
public class Grid {
    // (7,30) = top-left corner x and y coordinates of frame
    final int FRAME_TOP_LEFT_X = 7;
    final int FRAME_TOP_LEFT_Y = 30;
    // yOffset = 55
    final int Y_OFFSET = 55;

    int numRows;
    int numCols;
    int[][] grid;
    int gridCellSize;
    int score;

    public Grid(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.grid = new int[numRows][numCols];
        this.gridCellSize = 40;
        this.score = 0;
        initialiseGrid();
    }

    private void initialiseGrid() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                grid[i][j] = 0; // 0 represents an empty cell
            }
        }
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public int getScore() {
        return score;
    }

    // Check if a cell is filled
    public boolean isCellFilled(int row, int col) {
        return grid[row][col] == 1;
    }

    /* Check if you can place shape on the grid, where
    shape has its cells in grid coordinates */
    public boolean canPlace(Shape shape) {
        if (shape == null) {
            return false;
        }
        return canPlaceGrid(shape, grid);
    }

    // Check if the grid location is clear
    private boolean canPlaceGrid(Shape shape, int[][] grid) {
        for (Cell cell : shape) {
            int x = cell.x();
            int y = cell.y();

            // Check if the cell is within the grid bounds and not occupied
            if (x < 0 || x >= numRows || y < 0 || y >= numCols || grid[x][y] == 1) {
                return false;
            }
        }
        return true;
    }

    /* Gets a list of complete regions in the game grid,
    including full rows, columns, and 3x3 squares */
    private List<Shape> getFullRegions() {
        return getFullRegionsGrid(grid);
    }

    // Get the full regions on the grid
    private List<Shape> getFullRegionsGrid(int[][] grid) {
        List<Shape> fullRegions = new ArrayList<>();

        // Check each row
        for (int i = 0; i < numRows; i++) {
            if (isRowFull(i, grid)) {
                fullRegions.add(getRowShape(i));
            }
        }

        // Check each column
        for (int j = 0; j < numCols; j++) {
            if (isColumnFull(j, grid)) {
                fullRegions.add(getColumnShape(j));
            }
        }

        // Check each 3x3 square
        for (int i = 0; i < numRows; i += 3) {
            for (int j = 0; j < numCols; j += 3) {
                if (isSquareFull(i, j, grid)) {
                    fullRegions.add(getSquareShape(i, j));
                }
            }
        }

        return fullRegions;
    }

    // Put the ghost shape in a copy of the grid and get the poppable regions
    public List<Shape> getPoppableRegions(Shape ghostShape) {
        // Create a copy of the grid
        int[][] copyGrid = copyGrid();

        for (Cell cell : ghostShape) {
            if (cell.x() < 0 || cell.x() >= numRows || cell.y() < 0 || cell.y() >= numCols) {
                return null;
            }
        }

        // Place the shape on the copy grid
        placeShapeGrid(ghostShape, copyGrid);

        // Get full regions on the copy grid (poppable regions)
        return getFullRegionsGrid(copyGrid);
    }

    /* Set the cells of full regions to empty again and update
    the score based on the number of cleared regions */
    public void clearFullRegions() {
        List<Shape> fullRegions = getFullRegions();

        for (Shape region : fullRegions) {
            for (Cell cell : region) {
                int x = cell.x();
                int y = cell.y();
                grid[x][y] = 0; // Set the cell to empty
            }
        }

        score += fullRegions.size();
    }

    // Check if a row is full
    boolean isRowFull(int row, int[][] grid) {
        for (int j = 0; j < numCols; j++) {
            if (grid[row][j] == 0) {
                return false; // Found an empty cell in the row
            }
        }
        return true; // All cells in the row are occupied
    }

    // Check if a column is full
    boolean isColumnFull(int col, int[][] grid) {
        for (int i = 0; i < numRows; i++) {
            if (grid[i][col] == 0) {
                return false; // Found an empty cell in the column
            }
        }
        return true; // All cells in the column are occupied
    }

    // Check if a 3x3 square is full
    boolean isSquareFull(int startX, int startY, int[][] grid) {
        for (int i = startX; i < startX + 3; i++) {
            for (int j = startY; j < startY + 3; j++) {
                if (grid[i][j] == 0) {
                    return false; // Found an empty cell in the square
                }
            }
        }
        return true; // All cells in the square are occupied
    }

    // Get the shapes of a full row
    private Shape getRowShape(int row) {
        List<Cell> cells = new ArrayList<>();
        for (int j = 0; j < numRows; j++) {
            cells.add(new Cell(row, j));
        }
        return new Shape(cells, 0, 0);
    }

    // Get the shapes of a full column
    private Shape getColumnShape(int col) {
        List<Cell> cells = new ArrayList<>();
        for (int i = 0; i < numCols; i++) {
            cells.add(new Cell(i, col));
        }
        return new Shape(cells, 0, 0);
    }

    // Get the shapes of a 3x3 square
    private Shape getSquareShape(int startX, int startY) {
        List<Cell> cells = new ArrayList<>();
        for (int i = startX; i < startX + 3; i++) {
            for (int j = startY; j < startY + 3; j++) {
                cells.add(new Cell(i, j));
            }
        }
        return new Shape(cells, 0, 0);
    }

    // Places the shape on the grid
    public void placeShape(Shape shape) {
        placeShapeGrid(shape, grid);
    }

    // Places the shape on the specified grid, marking the corresponding cells as occupied
    private void placeShapeGrid(Shape shape, int[][] grid) {
        for (Cell cell : shape) {
            int x = cell.x();
            int y = cell.y();
            grid[x][y] = 1; // 1 represents an occupied cell
        }
    }

    // Creates a copy of the grid
    public int[][] copyGrid() {
        int[][] copy = new int[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                copy[i][j] = grid[i][j];
            }
        }
        return copy;
    }

    // Checks if the top-left position of the selected piece is within the grid
    public boolean insideGrid(Piece selectedPiece) {
        if (selectedPiece != null) {
            // Calculate the adjusted coordinates based on the top-left position of the piece
            int adjustedX = selectedPiece.getX() - FRAME_TOP_LEFT_X;
            int adjustedY = selectedPiece.getY() - FRAME_TOP_LEFT_Y;

            // Adjust for default top-left y coordinate
            int yOffset = Y_OFFSET - FRAME_TOP_LEFT_Y;

            // Adjust for the yOffset
            int relativeY = adjustedY - yOffset;

            // Calculate the grid cell coordinates
            int cellX = (int) Math.floor((double) adjustedX / (gridCellSize));
            int cellY = (int) Math.floor((double) relativeY / (gridCellSize));

            // Check if the snapped cells are inside the grid
            return (cellX >= 0 && cellX < numRows && cellY >= 0 && cellY < numCols);
        }
        return false;
    }

    // Checks for game over conditions
    public boolean isGameOver(Palette palette) {
        // Loop through every possible grid location
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                // Check if any piece from the palette can be placed in this location
                for (Piece piece : palette.getPieces()) {
                    if (canPlacePiece(piece, row, col)) {
                        // If a piece can be placed, the game is not over
                        return false;
                    }
                }
            }
        }
        // If no piece can be placed in any location, the game is over
        return true;
    }

    // Check if the piece can be placed in the specified grid location
    private boolean canPlacePiece(Piece piece, int row, int col) {
        for (Cell cell : piece.getShape()) {
            int cellX = col + cell.x();
            int cellY = row + cell.y();

            // Check if the cell is within the grid bounds and not already occupied
            if (cellX < 0 || cellX >= numRows || cellY < 0 || cellY >= numCols || grid[cellX][cellY] == 1) {
                return false;
            }
        }
        return true;
    }
}
