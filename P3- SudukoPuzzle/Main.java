/*
Wafae Benkassou, Riley Rutigliano, Jamil Velez
11/19/24
Program 3 - Sudoku Puzzle
Program that will solve sudoku puzzles fed into it using Breadth First Search and Depth Limited Search
 */

/*
Lina, Tirsa Ninia, and Matheus Supriyanto Rumetna. “Comparison Analysis of Breadth First Search and Depth Limited Search Algorithms in Sudoku Game.”
Bulletin of Computer Science and Electrical Engineering, vol. 2, no. 2, 2021, pp. 74–83.
https://www.researchgate.net/publication/358642884_Comparison_Analysis_of_Breadth_First_Search_and_Depth_Limited_Search_Algorithms_in_Sudoku_Game
 */

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Prompt user for puzzle size
        System.out.println("Select the type of Sudoku puzzle to solve:");
        System.out.println("1. 9x9");
        System.out.println("2. 16x16");
        System.out.println("3. 25x25");
        System.out.print("Enter your choice (1/2/3): ");
        int choice = scanner.nextInt();

        // Map user choice to the appropriate file
        String filePath;
        switch (choice) {
            case 1:
                filePath = "SudokuPuzzles.txt"; // 9x9 puzzle file
                break;
            case 2:
                filePath = "BigPuzzle.txt"; // 16x16 puzzle file
                break;
            case 3:
                filePath = "25x25Sudoku.txt"; // 25x25 puzzle file
                break;
            default:
                System.out.println("Invalid choice. Exiting...");
                return;
        }

        try {
            // Read Sudoku puzzles from the selected file
            List<int[]> puzzles = readSudokuPuzzles(filePath);

            // Loop through each puzzle
            for (int i = 0; i < puzzles.size(); i++) {
                System.out.println("Puzzle " + (i + 1) + ":");
                printPuzzle(puzzles.get(i)); // Print the puzzle before solving

                // Dynamically calculate the grid size based on puzzle length
                int gridSize = (int) Math.sqrt(puzzles.get(i).length); // Ensure this is a perfect square
                System.out.println("Grid size: " + gridSize + "x" + gridSize);

                // Create a Sudoku graph for the puzzle
                SudokuGraph graph = new SudokuGraph(gridSize); // Create graph for variable grid sizes

                // Set the initial puzzle values in the graph
                graph.setBoardState(puzzles.get(i)); // Set the board state using setBoardState

                // Solve the puzzle with BFS
                System.out.println("\n>>>> Solving with BFS...");
                long bfsStart = System.nanoTime();
                List<int[]> bfsSolutions = SudokuSolverBFS.solve(graph); // BFS solver
                long bfsEnd = System.nanoTime();
                printSolutions("BFS", bfsSolutions, bfsEnd - bfsStart);

                // Solve the puzzle with DLS
                System.out.println("\n>>>> Solving with DLS...");
                int depthLimit = gridSize * gridSize; // Set the depth limit based on grid size
                long dlsStart = System.nanoTime();
                List<int[]> dlsSolutions = SudokuSolverDLS.solve(graph, depthLimit); // DLS solver
                long dlsEnd = System.nanoTime();
                printSolutions("DLS", dlsSolutions, dlsEnd - dlsStart);

                System.out.println(); // Add a blank line between puzzles
            }
        } catch (IOException e) {
            System.err.println("Error reading Sudoku puzzles: " + e.getMessage());
        }
    }

    // Method to read the Sudoku puzzles from the file
    private static List<int[]> readSudokuPuzzles(String filePath) throws IOException {
        List<int[]> puzzles = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            List<Integer> currentPuzzle = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                line = line.trim(); // Remove any leading or trailing whitespace
                if (line.isEmpty()) {
                    if (!currentPuzzle.isEmpty()) {
                        // Convert the List<Integer> to an int[] and add it to puzzles list
                        puzzles.add(convertToPuzzleArray(currentPuzzle));
                        currentPuzzle.clear(); // Clear the list for the next puzzle
                    }
                } else {
                    // Split the line into individual parts (numbers or ".")
                    String[] parts = line.split(" ");
                    for (String part : parts) {
                        if (part.equals(".")) {
                            // Represent empty cells as 0
                            currentPuzzle.add(0);
                        } else {
                            try {
                                // Parse the number and add it to the current puzzle
                                currentPuzzle.add(Integer.parseInt(part));
                            } catch (NumberFormatException e) {
                                System.err.println("Invalid number format: " + part);
                            }
                        }
                    }
                }
            }

            // Add the last puzzle if it wasn't followed by a blank line
            if (!currentPuzzle.isEmpty()) {
                puzzles.add(convertToPuzzleArray(currentPuzzle));
            }
        }
        return puzzles;
    }

    // Helper method to convert List<Integer> to int[]
    private static int[] convertToPuzzleArray(List<Integer> currentPuzzle) {
        int[] puzzleArray = new int[currentPuzzle.size()];
        for (int i = 0; i < currentPuzzle.size(); i++) {
            puzzleArray[i] = currentPuzzle.get(i);
        }
        return puzzleArray;
    }

    // Method to print Sudoku solutions
    private static void printSolutions(String method, List<int[]> solutions, long time) {
        System.out.println(">>> Solutions found using " + method + ":");
        if (solutions.isEmpty()) {
            System.out.println("No solutions found.");
        } else {
            for (int idx = 0; idx < solutions.size(); idx++) {
                System.out.println("Solution " + (idx + 1) + ":");
                printPuzzle(solutions.get(idx));
                System.out.println();
            }
        }
        System.out.printf("%s Time: %.2f ms\n\n", method, time / 1e6);
    }

    // Method to print the Sudoku puzzle in a readable format
    private static void printPuzzle(int[] puzzle) {
        int size = (int) Math.sqrt(puzzle.length); // Calculate grid size dynamically
        for (int i = 0; i < puzzle.length; i++) {
            System.out.print((puzzle[i] == 0 ? "." : puzzle[i]) + " ");
            if ((i + 1) % size == 0) System.out.println();
        }
    }
}
