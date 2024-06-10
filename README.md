# Block Puzzle Game

Created an implementation of a block puzzle game. The idea was inspired by Blockudoku, which is like a cross between Tetris (filling lines to erase them) and Sudoku’s rows, columns and mini-squares.

## Model Classes

- **Cell**: Represents a single cell in the grid.
- **Shape**: Group of cells forming a shape, enabling the creation of various game shapes.
- **ShapeSet**: Generates a set of predefined shapes.
- **PieceState**: Enum representing the state of a piece (IN_PALETTE, IN_PLAY, PLACED).
- **Piece**: Represents a shape placed on the grid at a specific location, includes methods for initialisation, selection, and snapping to the grid.
- **Palette**: Manages the set of shapes available for player selection, including initialisation, removal, and refreshing.
- **Grid**: Represents the game grid, handling initialisation, piece placement validation, region clearing, score updating, and game over checks.

## Controller and View Classes

- **GameController**: Handles user input, such as selecting, dragging, and placing pieces. Updates the view and model and initialises the game environment.
- **GameView**: Represents the GUI, displaying the grid, palette, selected piece, ghost shape, and poppable regions.

## Design Patterns

- **MVC Pattern**: Separates concerns into Model, View, and Controller.
- **Factory Pattern**: Used in ShapeSet for creating different shapes.

## OO Design Principles

- **Single Responsibility Principle (SRP)**: Each class has a specific responsibility.
- **Open/Closed Principle (OCP)**: Design allows for extension without modifying existing code.
- **Liskov Substitution Principle (LSP)**: Subtypes can be used interchangeably with their base types.