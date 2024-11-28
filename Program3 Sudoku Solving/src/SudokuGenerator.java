// https://stackoverflow.com/questions/6963922/java-sudoku-generatoreasiest-solution
import java.util.*;

public class SudokuGenerator {
    static final int SIZE = 9;
    static final int SUBGRID = 3;

    public static void main(String[] args ) {
        int[][] grid = new int[SIZE][SIZE];

        if (generateSudoku(grid)) {
            System.out.println("\nEasy Level:");
            printGrid(generatePuzzle(grid, "easy"));

            System.out.println("\nMedium Level:");
            printGrid(generatePuzzle(grid, "medium"));

            System.out.println("\nHard Level:");
            printGrid(generatePuzzle(grid, "hard"));
        } else {
            System.out.println("FAILED TO GENERATE");
        }
    }
    private static boolean generateSudoku(int[][] grid) {
        return fillGrid(grid, 0, 0);
    }
    private static boolean fillGrid(int[][] grid, int row, int col) {
        if (row == SIZE) {
            return true;
        }
        if (col == SIZE) {
            return fillGrid(grid, row + 1, 0);
        }

        Random random = new Random();
        int[] nums = randomOrder();

        for (int num : nums) {
            if (isSafe(grid, row, col, num)){
                grid[row][col] = num;
                if(fillGrid(grid, row, col +1)){
                    return true;
                }
                grid[row][col] = 0;
            }
        }
        return false;
    }
    private static int[] randomOrder() {
        int[] nums = new int[SIZE];
        for (int i = 0; i < SIZE; i++) {
            nums[i] = i + 1;
        }

        Random random = new Random();
        for (int i = 0; i < SIZE; i++) {
            int j = random.nextInt(SIZE);
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }

        return nums;
    }

    private static boolean isSafe(int[][] grid, int row, int col, int num) {
        for (int x = 0; x < SIZE; x++) {
            if (grid[row][x] == num || grid[x][col] == num ||
                    grid[row - row % SUBGRID + x / SUBGRID][col - col % SUBGRID + x % SUBGRID] == num) {
                return false;
            }
        }
        return true;
    }

    private static int[][] generatePuzzle(int[][] completeGrid, String difficulty) {
        int[][] puzzle = copyGrid(completeGrid);
        int clues = getHints(difficulty);

        Random random = new Random();
        int cellsToRemove = SIZE * SIZE - clues;

        while (cellsToRemove > 0) {
            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);

            if (puzzle[row][col] != 0) {
                puzzle[row][col] = 0;
                cellsToRemove--;
            }
        }

        return puzzle;
    }
    private static int getHints(String difficulty) {
        return switch (difficulty.toLowerCase()) {
            case "easy" -> 40;
            case "medium" -> 30;
            case "hard" -> 20;
            default -> 30;
        };
    }
    private static int[][] copyGrid(int[][] grid) {
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(grid[i], 0, copy[i], 0, SIZE);
        }
        return copy;
    }
    private static void printGrid(int[][] grid) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print((grid[i][j] == 0 ? "." : grid[i][j]) + " ");
            }
            System.out.println();
        }
    }
}

