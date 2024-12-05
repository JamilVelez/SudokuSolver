// Graph to Solve Suduko
// Why use this? Because it can easily model the relationship between nodes
// Keep track of valid possible values and update dynamically as assigning values
// Apply graph traversal algorithms (BFS or DLS) to search for solutions 

import java.util.*;

public class SudokuGraph {
    private int gridSize; // 9 for 9x9, 16 for 16x16, etc.
    private int subGridSize; // 3 for 9x9, 4 for 16x16, etc.
    private int[] board; // The current state of the board

    public SudokuGraph(int gridSize) {
        this.gridSize = gridSize;
        this.subGridSize = (int) Math.sqrt(gridSize);
        this.board = new int[gridSize * gridSize]; // Initialize an empty board
    }

    // Method to set the board state
    public void setBoardState(int[] initialState) {
        if (initialState.length != board.length) {
            throw new IllegalArgumentException("Invalid board size.");
        }
        this.board = initialState.clone();
    }

    // Get possible values for a cell
    public List<Integer> getPossibleValues(int cellIndex) {
        Set<Integer> possibleValues = new HashSet<>();
        for (int i = 1; i <= gridSize; i++) {
            possibleValues.add(i);
        }

        // Remove values in the same row, column, and sub-grid
        for (int neighbor : getRelatedCells(cellIndex)) {
            possibleValues.remove(board[neighbor]);
        }

        return new ArrayList<>(possibleValues);
    }

    // Get indices of related cells (row, column, sub-grid)
    public List<Integer> getRelatedCells(int cellIndex) {
        Set<Integer> related = new HashSet<>();

        int row = cellIndex / gridSize;
        int col = cellIndex % gridSize;

        // Add row indices
        for (int c = 0; c < gridSize; c++) {
            related.add(row * gridSize + c);
        }

        // Add column indices
        for (int r = 0; r < gridSize; r++) {
            related.add(r * gridSize + col);
        }

        // Add sub-grid indices
        int subGridRowStart = (row / subGridSize) * subGridSize;
        int subGridColStart = (col / subGridSize) * subGridSize;
        for (int r = subGridRowStart; r < subGridRowStart + subGridSize; r++) {
            for (int c = subGridColStart; c < subGridColStart + subGridSize; c++) {
                related.add(r * gridSize + c);
            }
        }

        // Exclude the current cell (optional)
        related.remove(cellIndex);

        return new ArrayList<>(related);
    }
    // Method to get the grid size
    public int getSize() {
        return this.gridSize;
    }
}
