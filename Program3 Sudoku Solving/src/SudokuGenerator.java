//https://stackoverflow.com/questions/6963922/java-sudoku-generatoreasiest-solution
//Added generated Sudoku puzzles into SudokuPuzzles.txt
import java.util.*;

public class SudokuGenerator {
    static final int SIZE = 9;          //variable for size
    static final int SUBGRID = 3;       //variable for subgrid

    public static void main(String[] args ) {
        int[][] grid = new int[SIZE][SIZE];                     //9x9 grid gets made

        if (generateSudoku(grid)) {                             //Tries to make grids for each difficulty
            System.out.println("\nEasy Level:");
            printGrid(generatePuzzle(grid, "easy"));    //easy puzzle

            System.out.println("\nMedium Level:");
            printGrid(generatePuzzle(grid, "medium"));  //medium puzzle

            System.out.println("\nHard Level:");
            printGrid(generatePuzzle(grid, "hard"));    //hard puzzle
        } else {
            System.out.println("FAILED TO GENERATE");           //if it fails, prints this
        }
    }

    private static boolean generateSudoku(int[][] grid) {
        return fillGrid(grid, 0, 0);                   //starts generating the grid starting form top-left
    }
    private static boolean fillGrid(int[][] grid, int row, int col) {   //recursive function to fill the grid
        if (row == SIZE) {                                      //if row index gets to 9
            return true;                                        //grid is complete
        }
        if (col == SIZE) {                                      //if col index gets to 9
            return fillGrid(grid, row + 1, 0);         //go to next row
        }

        int[] nums = randomOrder();                             //generates array of numbers from 1 to 9

        for (int num : nums) {                                  //iterates through each number in nums array
            if (isSafe(grid, row, col, num)){                   //checks if the number can be places in current cell
                grid[row][col] = num;                           //places number in row,col (current cell)
                if(fillGrid(grid, row, col +1)){            //recursively calls to fill the next cell
                    return true;
                }
                grid[row][col] = 0;                             //if number can't be placed, returns 0 to the cell
            }
        }
        return false;                                           //resets and checks another number for the cell
    }

    private static int[] randomOrder() {                        //function for randomizing array of 1 to 9
        int[] nums = new int[SIZE];
        for (int i = 0; i < SIZE; i++) {                        //creates an array of size 9
            nums[i] = i + 1;                                    //elements are 1 to 9
        }

        Random random = new Random();
        for (int i = 0; i < SIZE; i++) {                        //generates random indices, up to 9 times
            int j = random.nextInt(SIZE);                       //selects a random position in the array to swap with i
            int temp = nums[i];                                 //temp is i
            nums[i] = nums[j];                                  //swaps i and j elements
            nums[j] = temp;                                     //j is temp
        }

        return nums;                                            //returns shuffled array
    }

    private static boolean isSafe(int[][] grid, int row, int col, int num) {    //function for checking Sudoku rules
        for (int x = 0; x < SIZE; x++) {                        //checks all indices
            if (grid[row][x] == num || grid[x][col] == num ||   //if row or column or 3x3 subgrid have the same number
                    grid[row - row % SUBGRID + x / SUBGRID][col - col % SUBGRID + x % SUBGRID] == num) {
                return false;                                   //if any true, returns false since it fails
            }
        }
        return true;
    }

    private static int[][] generatePuzzle(int[][] completeGrid, String difficulty) { //function to create a puzzle
        int[][] puzzle = copyGrid(completeGrid);                //makes a copy of the original grid
        int hints = getHints(difficulty);                       //generates hints based on puzzle difficulty

        List<int[]> cells = new ArrayList<>();                  //creates a list for all cell coordinates in the grid
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells.add(new int[]{row, col});
            }
        }
        Collections.shuffle(cells);                            //shuffles the order of cell coordinates, ready to remove
        for (int[] cell : cells) {
            if (countHints(puzzle) <= hints) break;            //once hints have been reached, stops the loop
            int row = cell[0], col = cell[1];                  //selects the row and column
            int temp = puzzle[row][col];                       //"removes" element in cell and places it in temp
            puzzle[row][col] = 0;                              //sets the row,col to 0

            if (!UniqueSolution(puzzle)){                      //checks if the puzzle has only one solution
                puzzle[row][col] = temp;                       //if it doesn't, restores the number in temp
            }
        }
        return puzzle;
    }

    private static boolean UniqueSolution(int[][] puzzle) {
        int[][] copy = copyGrid(puzzle);                       //makes a copy of the puzzle grid
        return countSolutions(copy) == 1;                      //calls for countSolutions and makes sure there's only 1
    }

    private static int countSolutions(int[][] grid) {
        return solver(grid, 0 , 0);                   //calls for the solver method, starting at the top-left
    }

    private static int solver(int[][] grid, int row, int col) {
        if (row == SIZE) {                                     //if row index equals size, all rows have been processed
            return 1;
        }
        if (col == SIZE) {                                     //if col index equals size, row is complete
            return solver(grid, row + 1, 0);          //goes to next row
        }
        if (grid[row][col] != 0) {                             //if cells are filled
            return solver(grid, row, col + 1);             //moves to next column
        }
        int count = 0;                                         //count variable for solutions found
        for (int num = 1; num <= SIZE; num++) {                //goes through each number (1-9) for the current cell
            if (isSafe(grid, row, col, num)) {                 //checks if it's safe to place the number
                grid[row][col] = num;                          //places number in the current cell
                count += solver(grid, row, col + 1);       //calls solver for the next cell
                grid[row][col] = 0;                            //resets current cell to 0
                if (count > 1){                                //if more than one solution
                    return count;
                }
            }
        }
        return count;
    }

    private static int countHints(int[][] grid) {
        int count = 0;
        for (int i = 0; i < SIZE; i++) {                      //loops through each row
            for (int j = 0; j < SIZE; j++) {                  //loops through each column
                if (grid[i][j] != 0) {                        //if cell is non-zero
                    count++;                                  //adds to count
                }
            }
        }
        return count;
    }

    private static int getHints(String difficulty) {
        return switch (difficulty.toLowerCase()) {
            case "easy" -> 40;
            case "medium" -> 32;
            case "hard" -> 25;
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

