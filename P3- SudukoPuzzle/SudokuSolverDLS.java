import java.util.*;

public class SudokuSolverDLS {

    public static List<int[]> solve(SudokuGraph graph, int depthLimit) {
        List<int[]> solutions = new ArrayList<>();
        int[] initialBoard = new int[81];  // Empty board (0 represents empty cells)

        depthLimitedSearch(graph, initialBoard, 0, depthLimit, solutions);
        return solutions;
    }

    private static void depthLimitedSearch(SudokuGraph graph, int[] board, int depth, int depthLimit, List<int[]> solutions) {
        // If we've reached the depth limit, stop searching further
        if (depth > depthLimit) {
            return;
        }

        // If the board is solved (all cells filled), add it to solutions
        if (isSolved(graph, board)) {
            solutions.add(board.clone()); // Add a clone to the solutions list
            return;
        }

        // Find the next empty cell (answer box)
        int emptyCell = findNextEmptyCell(board, graph);

        // If there are no empty cells, the board is complete
        if (emptyCell == -1) {
            return; // No further search needed
        }

        // Try placing values (1-9) in the empty cell
        for (int value : graph.getPossibleValues(emptyCell)) {
            if (isValidMove(graph, board, emptyCell, value)) {
                board[emptyCell] = value; // Place the value in the empty cell

                // Recursively explore the next level with the updated board
                depthLimitedSearch(graph, board, depth + 1, depthLimit, solutions);

                // If no valid solution, backtrack: reset the cell and try the next value
                board[emptyCell] = 0;
            }
        }
    }

    private static boolean isSolved(SudokuGraph graph, int[] board) {
        // Check if all cells are filled (no empty cells remaining)
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0) return false;
            if (!isValidMove(graph, board, i, board[i])) return false;
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
}
