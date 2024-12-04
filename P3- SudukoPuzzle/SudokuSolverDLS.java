import java.util.*;

public class SudokuSolverDLS {

    public static boolean solve(SudokuGraph graph, int depthLimit) {
        // Start the search at the root node (empty board)
        int[] initialBoard = new int[81];  // Empty board (0 represents empty cells)

        
        return depthLimitedSearch(graph, initialBoard, 0, depthLimit);
    }
    
    private static boolean depthLimitedSearch(SudokuGraph graph, int[] board, int depth, int depthLimit) {
        // If we've reached the depth limit, stop searching further
        if (depth > depthLimit) {
            return false;
        }
    
        // If the board is solved (all cells are filled), return true
        if (isSolved(graph, board)) {
            printBoard(board); // Print the solution
            return true;
        }
    
        // Find the next empty cell (answer box)
        int emptyCell = findNextEmptyCell(board, graph);
        
        // If there are no empty cells, the board is complete
        if (emptyCell == -1) {
            return true;  // All cells are filled
        }

        if (graph.getPossibleValues(emptyCell).size() == 1) {
            //System.out.println("Skipping pre-filled cell at index " + emptyCell);
            board[emptyCell] = graph.getPossibleValues(emptyCell).iterator().next();
            return depthLimitedSearch(graph, board, depth + 1, depthLimit);
        }
    
        // Try placing values (1-9) in the empty cell
        for (int value = 1; value <= 9; value++) {
            if (isValidMove(graph, board, emptyCell, value)) {
                board[emptyCell] = value;  // Place the value in the empty cell
                //System.out.println("Placing value " + value + " at cell " + emptyCell);
                
                // Recursively explore the next level with the updated board
                if (depthLimitedSearch(graph, board, depth + 1, depthLimit)) {
                    return true;  // Found a valid solution
                }
    
                // If no valid solution, backtrack: reset the cell and try the next value
                board[emptyCell] = 0;
                //System.out.println("Backtracking: Reset cell " + emptyCell);
            }
        }
    
        // If no valid move was found for this node, backtrack
        return false;
    }

    private static boolean isSolved(SudokuGraph graph, int[] board) {
        // Check if all cells are filled (no empty cells remaining)
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0) return false;
            if(!isValidMove(graph, board, i, board[i])) return false;
        }
        return true; // All cells are filled
    }

    private static int findNextEmptyCell(int[] board, SudokuGraph graph) {
        int bestCell = -1;
        int minOptions = 10;
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0) {
                int options = graph.getPossibleValues(i).size();
                if (options < minOptions) {
                    minOptions = options;
                    bestCell = i;
                }
            }
        }
        return bestCell;
    }

    private static boolean isValidMove(SudokuGraph graph, int[] board, int cell, int value) {
        // Check if placing the value in the given cell violates Sudoku constraints
        for (int neighbor : graph.getNeighbors(cell)) {
            if (board[neighbor] == value) return false; // Invalid move if there's a conflict
        }
        return true; // Valid move
    }

    private static void printBoard(int[] board) {
        // Print the board in a readable format
        for (int i = 0; i < board.length; i++) {
            System.out.print((board[i] == 0 ? "." : board[i]) + " ");
            if ((i + 1) % 9 == 0) System.out.println();  // New line after each row
        }
        System.out.println(); // Extra line for spacing
    }
}
