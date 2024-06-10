import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

// Controls the game logic and user interactions for the block puzzle game
public class GameController extends MouseAdapter {
    GameView gameView;
    Grid grid;
    Palette palette;
    int paletteCellSize;
    Piece selectedPiece;
    Shape ghostShape;
    List<Shape> poppableRegions;
    Point lastMousePoint = null;

    public GameController(Grid grid, Palette palette, int paletteCellSize, GameView gameView) {
        this.grid = grid;
        this.palette = palette;
        this.paletteCellSize = paletteCellSize;
        this.gameView = gameView;
    }

    // Make the ghost shape reflect the snapped position of the selected piece on the grid
    public void setGhostShape() {
        if (selectedPiece != null) {
            ghostShape = selectedPiece.snapToGrid();
            if (ghostShape != null && grid.canPlace(ghostShape)) {
                gameView.setGhostShape(ghostShape);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        /* Check if the click is on a palette shape - if true
        set the current piece to the selected shape */
        for (Piece piece : palette.getPieces()) {
            if (piece.contains(e.getPoint(), paletteCellSize)) {
                selectedPiece = piece;
                selectedPiece.state = PieceState.IN_PLAY;
                lastMousePoint = e.getPoint();
                gameView.setSelectedPiece(selectedPiece);
                palette.removePiece(piece);
                gameView.setPalette(palette);
                break;
            }
        }
        gameView.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        /* Update the position of the selected piece based on the mouse drag and set
        the ghost shape to follow the selected piece and check for any poppable regions */
        if (selectedPiece != null && lastMousePoint != null) {
            selectedPiece.x += e.getX() - lastMousePoint.x;
            selectedPiece.y += e.getY() - lastMousePoint.y;
            if (grid.insideGrid(selectedPiece)) {
                setGhostShape();
                poppableRegions = grid.getPoppableRegions(ghostShape);
            }
            else {
                gameView.setGhostShape(null);
            }
            lastMousePoint = e.getPoint();
        }
        gameView.setPoppableRegions(poppableRegions);
        gameView.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        /* Place the selectedPiece on the grid if the placement is legal, and
        clear any full regions, update the score and check for game over */
        if (selectedPiece != null) {
            Shape shape = selectedPiece.snapToGrid();
            if (grid.canPlace(shape)) {
                grid.placeShape(shape);
                grid.clearFullRegions();
                selectedPiece.state = PieceState.PLACED;
            } else {
                selectedPiece.state = PieceState.IN_PALETTE;
            }
        }
        gameView.updateScoreLabel(grid.getScore());
        palette.refresh();
        if (grid.isGameOver(palette)) {
            gameView.showGameOverMessage();
        }
        gameView.setSelectedPiece(null);
        gameView.setGhostShape(null);
        gameView.repaint();
    }

    /* Initializes the game components (grid, palette, view, and controller)
    and sets up the necessary listeners to run the block puzzle game */
    public static void main(String[] args) {
        Grid grid = new Grid(9, 9);
        Palette palette = new Palette();

        GameView gameView = new GameView(grid, palette);
        GameController controller = new GameController(grid, palette, 20, gameView);

        gameView.setController(controller);
        gameView.addMouseListener(controller);
        gameView.addMouseMotionListener(controller);
    }
}
