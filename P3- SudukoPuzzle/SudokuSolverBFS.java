import java.util.*;

public class SudokuSolverBFS {
    public static boolean solve(SudokuGraph graph) {
        Queue<int[]> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        int[] initialBoard = new int[81];

        // Initialize queue with the initial board
        queue.add(initialBoard);
        visited.add(Arrays.toString(initialBoard));

        while (!queue.isEmpty()) {
            int[] board = queue.poll();
            int emptyCell = findNextEmptyCell(graph, board);

            if (emptyCell == -1) {
                // No empty cells left; solution found
                printBoard(board);
                return true;
            }

            for (int value : graph.getPossibleValues(emptyCell)) {
                if (isValidMove(graph, board, emptyCell, value)) {
                    board[emptyCell] = value; // Temporarily place value
                    String boardState = Arrays.toString(board);

                    if (!visited.contains(boardState)) {
                        queue.add(board.clone()); // Only clone for the queue
                        visited.add(boardState);
                    }

                    board[emptyCell] = 0; // Reset for backtracking
                }
            }
        }

        return false; // No solution found
    }

    private static int findNextEmptyCell(SudokuGraph graph, int[] board) {
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0) return i;
        }
        return -1;
    }

    private static boolean isValidMove(SudokuGraph graph, int[] board, int cell, int value) {
        for (int neighbor : graph.getNeighbors(cell)) {
            if (board[neighbor] == value) return false;
        }
        return true;
    }

    private static void printBoard(int[] board) {
        for (int i = 0; i < board.length; i++) {
            System.out.print((board[i] == 0 ? "." : board[i]) + " ");
            if ((i + 1) % 9 == 0) System.out.println();
        }
        System.out.println();
    }
}