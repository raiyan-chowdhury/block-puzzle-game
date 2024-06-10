import javax.swing.*;
import java.awt.*;
import java.util.List;

// Manages the graphical user interface (GUI) elements for the block puzzle game
public class GameView extends JFrame {
    final int GRID_CELL_SIZE = 40;
    final int PALETTE_CELL_SIZE = GRID_CELL_SIZE / 2;
    final Color GRID_COLOR = Color.GRAY;
    final Color MINI_GRID_COLOR = Color.BLACK;
    final Color GRID_CELL_FILLED_COLOR = Color.GREEN;
    final Color PALETTE_BACKGROUND = Color.GRAY;
    final Color GHOST_SHAPE_COLOR = new Color(0, 255,255, 128);
    final Color POPPABLE_REGION_COLOR = new Color(0, 0,0,128);

    final int Y_OFFSET = 55;

    GameController controller;
    Grid grid;
    Palette palette;
    Piece selectedPiece;
    Shape ghostShape;
    List<Shape> poppableRegions;
    JLabel scoreLabel;
    JLabel gameOverLabel;

    public GameView(Grid grid, Palette palette) {
        this.grid = grid;
        this.palette = palette;
        this.selectedPiece = null;
        this.ghostShape = null;
        this.poppableRegions = null;
        initialiseUI();
    }

    public void initialiseUI() {
        setTitle("Block Puzzle Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // mainPanel will hold the game components
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintGrid(g);
                paintMiniGrid((Graphics2D) g);
                paintGhostShape(g);
                paintPoppableRegions(g);
                paintPalette(g);
                paintDraggedPiece(g);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(GRID_CELL_SIZE * grid.numRows, 600);
            }
        };

        scoreLabel = new JLabel("Score: " + grid.getScore());

        gameOverLabel = new JLabel("Game Over!");
        gameOverLabel.setVisible(false);

        JPanel labelsPanel = new JPanel();
        labelsPanel.add(scoreLabel);
        labelsPanel.add(gameOverLabel);

        mainPanel.add(labelsPanel, BorderLayout.NORTH);

        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }

    public void setSelectedPiece(Piece piece) {
        this.selectedPiece = piece;
    }

    public void setPalette(Palette palette) {
        this.palette = palette;
    }

    public void setGhostShape(Shape ghostShape) {
        this.ghostShape = ghostShape;
    }

    public void setPoppableRegions(List<Shape> poppableRegions) {
        this.poppableRegions = poppableRegions;
    }

    public void paintGrid(Graphics g) {
        g.setColor(GRID_COLOR);
        for (int i = 0; i < grid.getNumRows(); i++) {
            for (int j = 0; j < grid.getNumCols(); j++) {
                if (grid.isCellFilled(i, j)) {
                    g.setColor(GRID_CELL_FILLED_COLOR);
                    g.fillRect(i * GRID_CELL_SIZE, Y_OFFSET + j * GRID_CELL_SIZE, GRID_CELL_SIZE, GRID_CELL_SIZE);
                    g.setColor(GRID_COLOR);
                }
                g.drawRect(i * GRID_CELL_SIZE, Y_OFFSET + j * GRID_CELL_SIZE, GRID_CELL_SIZE, GRID_CELL_SIZE);
            }
        }
    }

    public void paintMiniGrid(Graphics2D g) {
        g.setColor(MINI_GRID_COLOR);
        g.setStroke(new BasicStroke(2)); // thickness of 2px
        for (int i = 0; i < grid.getNumRows(); i += 3) {
            for (int j = 0; j < grid.getNumCols(); j += 3) {
                g.drawRect(j * GRID_CELL_SIZE, Y_OFFSET + i * GRID_CELL_SIZE, 3 * GRID_CELL_SIZE, 3 * GRID_CELL_SIZE);
            }
        }
    }

    public void paintDraggedPiece(Graphics g) {
        if (selectedPiece != null) {
            if (selectedPiece.state == PieceState.IN_PLAY) {
                for (Cell cell : selectedPiece.getShape()) {
                    int cellX = selectedPiece.getX() + cell.x() * GRID_CELL_SIZE;
                    int cellY = selectedPiece.getY() + cell.y() * GRID_CELL_SIZE;

                    // Draw the cell
                    g.setColor(Color.BLUE);
                    g.fillRect(cellX, cellY, PALETTE_CELL_SIZE, PALETTE_CELL_SIZE);

                    // Draw the border
                    g.setColor(Color.BLACK);
                    g.drawRect(cellX, cellY, PALETTE_CELL_SIZE, PALETTE_CELL_SIZE);
                }
            }
        }
    }

    public void paintGhostShape(Graphics g) {
        g.setColor(GHOST_SHAPE_COLOR);
        if (ghostShape != null) {
            for (Cell cell : ghostShape) {
                g.fillRect(cell.x() * GRID_CELL_SIZE, Y_OFFSET + cell.y() * GRID_CELL_SIZE, GRID_CELL_SIZE, GRID_CELL_SIZE);
            }
        }
    }

    public void paintPoppableRegions(Graphics g) {
        g.setColor(POPPABLE_REGION_COLOR);
        if (poppableRegions != null && ghostShape != null) {
            for (Shape region : poppableRegions) {
                for (Cell cell : region) {
                    g.fillRect(cell.x() * GRID_CELL_SIZE, Y_OFFSET + cell.y() * GRID_CELL_SIZE, GRID_CELL_SIZE, GRID_CELL_SIZE);
                }
            }
        }
    }

    public void paintPalette(Graphics g) {
        g.setColor(PALETTE_BACKGROUND);
        g.fillRect(0, 420, getWidth(), getHeight());

        List<Piece> pieces = palette.getPieces();

        for (Piece piece : pieces) {
            g.setColor(Color.BLUE);
            for (Cell cell : piece.getShape()) {
                int cellX = piece.x + cell.x() * PALETTE_CELL_SIZE;
                int cellY = piece.y + cell.y() * PALETTE_CELL_SIZE;
                g.fillRect(cellX, cellY, PALETTE_CELL_SIZE, PALETTE_CELL_SIZE);
            }

            // Draw borders for each cell
            g.setColor(Color.BLACK);
            for (Cell cell : piece.getShape()) {
                int cellX = piece.x + cell.x() * PALETTE_CELL_SIZE;
                int cellY = piece.y + cell.y() * PALETTE_CELL_SIZE;
                g.drawRect(cellX, cellY, PALETTE_CELL_SIZE, PALETTE_CELL_SIZE);
            }
        }
    }

    public void updateScoreLabel(int score) {
        scoreLabel.setText("Score: " + score);
    }

    public void showGameOverMessage() {
        gameOverLabel.setVisible(true);
    }
}
