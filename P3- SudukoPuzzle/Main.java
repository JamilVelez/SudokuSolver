import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String filePath = "SudokuPuzzles.txt"; // Path to your puzzle file

        try {
            // Read Sudoku puzzles from file
            List<int[]> puzzles = readSudokuPuzzles(filePath);

            // Loop through each puzzle
            for (int i = 0; i < puzzles.size(); i++) {
                System.out.println("=========================================");
                System.out.println("Puzzle " + (i + 1) + ":");
                printPuzzle(puzzles.get(i)); // Print the puzzle before solving
                System.out.println("=========================================\n");
                
                int size = (int) Math.sqrt(puzzles.get(i).length); //calculate grid size (Dynamically)

                // Create a Sudoku graph for the puzzle
                SudokuGraph graph = new SudokuGraph(size);  //create graph for variable grid sizes
                
                for (int j = 0; j < puzzles.get(i).length; j++) {
                    if (puzzles.get(i)[j] != 0) {
                        graph.setValue(j, puzzles.get(i)[j]);  // Set fixed values (question boxes)
                    }
                }

                // Solve the puzzle with BFS
                System.out.println("\n>>>>Solving with BFS...");
                long bfsStart = System.nanoTime();
                List<int[]> bfsSolutions = SudokuSolverBFS.solve(graph); // BFS solver
                long bfsEnd = System.nanoTime();
                printSolutions("BFS", bfsSolutions, bfsEnd - bfsStart);

                // Solve the puzzle with DLS
                System.out.println("\n>>>>Solving with DLS...");
                int depthLimit = 81;  // Set a depth limit for DLS
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
                line = line.trim();
                if (line.isEmpty()) {
                    if (!currentPuzzle.isEmpty()) {
                        puzzles.add(convertToPuzzleArray(currentPuzzle));
                        currentPuzzle.clear();
                    }
                } else {
                    String[] parts = line.split(" ");
                    for (String part : parts) {
                        if (part.equals(".")) {
                            currentPuzzle.add(0); // Represent empty cells as 0
                        } else {
                            currentPuzzle.add(Integer.parseInt(part));
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

    // Helper method to convert List<Integer> to int[] (array)
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
        int size = (int) Math.sqrt(puzzle.length); //calculate grid size(dynamically)
        for (int i = 0; i < puzzle.length; i++) {
            System.out.print((puzzle[i] == 0 ? "." : puzzle[i]) + " ");
            if ((i + 1) % size == 0) System.out.println();
        }
    }
}
