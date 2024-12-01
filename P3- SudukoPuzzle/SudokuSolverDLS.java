import java.util.*;

public class SudokuSolverDLS {
    public static boolean solve(SudokuGraph graph, int depthLimit) {
        int[] board = new int[81]; // Start with an empty board (0 for empty cells)
        return depthLimitedSearch(graph, board, 0, depthLimit);
    }

    private static boolean depthLimitedSearch(SudokuGraph graph, int[] board, int depth, int depthLimit) {
        if (depth == depthLimit) {
            // Print board only when solution is found
            printBoard(board);
            return true;
        }

        int emptyCell = findEmptyCell(board, graph); // Find the next empty cell with MRV heuristic
        if (emptyCell == -1) {
            return false; // No empty cells left, puzzle is solved
        }

        for (int value : graph.getPossibleValues(emptyCell)) {
            if (isValidMove(graph, board, emptyCell, value)) {
                board[emptyCell] = value; // Place value

                if (depthLimitedSearch(graph, board, depth + 1, depthLimit)) {
                    return true; // Solution found
                }

                board[emptyCell] = 0; // Reset the cell for backtracking
            }
        }
        return false; // No solution found at this depth
    }

    private static int findEmptyCell(int[] board, SudokuGraph graph) {
        int minOptions = Integer.MAX_VALUE;
        int selectedCell = -1;

        // Find the cell with the fewest possible valid values (MRV heuristic)
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0) {
                int options = graph.getPossibleValues(i).size();
                if (options < minOptions) {
                    minOptions = options;
                    selectedCell = i;
                }
            }
        }
        return selectedCell;
    }

    private static boolean isValidMove(SudokuGraph graph, int[] board, int cell, int value) {
        for (int neighbor : graph.getNeighbors(cell)) {
            if (board[neighbor] == value) return false; // Invalid move if value conflicts with neighbors
        }
        return true;
    }

    private static void printBoard(int[] board) {
        // Print the board when the solution is found
        for (int i = 0; i < board.length; i++) {
            System.out.print(board[i] + " ");
            if ((i + 1) % 9 == 0) System.out.println();  // New line after each row
        }
        System.out.println();  // Extra line for spacing
    }
}