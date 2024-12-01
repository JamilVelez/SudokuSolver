import java.util.*;

public class SudokuSolverBFS {

    public static boolean solve(SudokuGraph graph) {
        Queue<int[]> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>(); // To track visited board states
        
        int[] initialBoard = new int[81]; // Start with an empty board (0s for empty cells)
        
        queue.add(initialBoard);
        visited.add(Arrays.toString(initialBoard)); // Add the initial state to visited list
        
        while (!queue.isEmpty()) {
            int[] board = queue.poll(); // Get the next board state to process
            
            // If the board is complete (all cells filled), return the solution
            if (isSolved(board)) {
                printBoard(board); // Print the solution board
                return true;
            }

            // Find the first empty cell (answer box)
            int emptyCell = findNextEmptyCell(board);
            
            // If the empty cell is a "question box" (pre-filled), continue to the next cell
            if (emptyCell == -1) {
                continue;
            }

            // Try all possible values for this empty cell
            for (int value : graph.getPossibleValues(emptyCell)) {
                if (isValidMove(graph, board, emptyCell, value)) {
                    board[emptyCell] = value; // Assign the value to the cell
                    
                    String boardState = Arrays.toString(board);
                    // If we haven't visited this board state before, process it
                    if (!visited.contains(boardState)) {
                        queue.add(board.clone()); // Add a clone of the new board to the queue
                        visited.add(boardState); // Mark this board state as visited
                    }
                    
                    board[emptyCell] = 0; // Reset the cell for the next attempt (backtracking)
                }
            }
        }
        
        return false; // If no solution is found
    }

    private static boolean isSolved(int[] board) {
        for (int cell : board) {
            if (cell == 0) return false; // If there's still an empty cell, it's not solved
        }
        return true; // All cells are filled
    }

    private static int findNextEmptyCell(int[] board) {
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0) return i; // Return the index of the first empty cell
        }
        return -1; // No empty cells found, puzzle is solved
    }

    private static boolean isValidMove(SudokuGraph graph, int[] board, int cell, int value) {
        // Check if placing the value in the given cell violates Sudoku constraints
        for (int neighbor : graph.getNeighbors(cell)) {
            if (board[neighbor] == value) return false; // Invalid move if there's a conflict
        }
        return true; // If no conflict, the move is valid
    }

    private static void printBoard(int[] board) {
        for (int i = 0; i < board.length; i++) {
            System.out.print((board[i] == 0 ? "." : board[i]) + " ");
            if ((i + 1) % 9 == 0) System.out.println(); // New line after each row
        }
        System.out.println(); // Extra line for spacing
    }
}
