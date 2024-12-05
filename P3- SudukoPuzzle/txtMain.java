import java.io.*;
import java.util.*;

public class txtMain {
    public static void main(String[] args) {
        //String filePath = "SudokuPuzzles.txt"; // Path to your puzzle file
        //String outputFilePath = "SudokuSolutions.txt"; // Path to the output file
        String filePath = "BigPuzzle.txt"; // Path to your 16x16 puzzle file
        String outputFilePath = "16x16SudokuSolutions.txt"; // Path to the output file
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            // Read Sudoku puzzles from file
            List<int[]> puzzles = readSudokuPuzzles(filePath);

            // Loop through each puzzle
            for (int i = 0; i < puzzles.size(); i++) {
                writer.write("Puzzle " + (i + 1) + ":\n");
                printPuzzle(writer, puzzles.get(i)); // Print the puzzle before solving

                // Dynamically calculate the grid size based on puzzle length
                int gridSize = (int) Math.sqrt(puzzles.get(i).length); // Ensure this is a perfect square
                writer.write("Grid size: " + gridSize + "x" + gridSize + "\n");

                // Create a Sudoku graph for the puzzle
                SudokuGraph graph = new SudokuGraph(gridSize);  // Create graph for variable grid sizes
                
                // Set the initial puzzle values in the graph
                graph.setBoardState(puzzles.get(i)); // Set the board state using setBoardState

                // Solve the puzzle with BFS
                writer.write("\n>>>> Solving with BFS...\n");
                long bfsStart = System.nanoTime();
                List<int[]> bfsSolutions = SudokuSolverBFS.solve(graph); // BFS solver
                long bfsEnd = System.nanoTime();
                printSolutions(writer, "BFS", bfsSolutions, bfsEnd - bfsStart);

                // Solve the puzzle with DLS
                writer.write("\n>>>> Solving with DLS...\n");
                int depthLimit = gridSize * gridSize;  // Set the depth limit based on grid size
                long dlsStart = System.nanoTime();
                List<int[]> dlsSolutions = SudokuSolverDLS.solve(graph, depthLimit); // DLS solver
                long dlsEnd = System.nanoTime();
                printSolutions(writer, "DLS", dlsSolutions, dlsEnd - dlsStart);

                writer.write("\n"); // Add a blank line between puzzles
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

    // Helper method to convert List<Integer> to int[] (array)
    private static int[] convertToPuzzleArray(List<Integer> currentPuzzle) {
        int[] puzzleArray = new int[currentPuzzle.size()];
        for (int i = 0; i < currentPuzzle.size(); i++) {
            puzzleArray[i] = currentPuzzle.get(i);
        }
        return puzzleArray;
    }

    // Method to print Sudoku solutions
    private static void printSolutions(BufferedWriter writer, String method, List<int[]> solutions, long time) throws IOException {
        writer.write(">>> Solutions found using " + method + ":\n");
        if (solutions.isEmpty()) {
            writer.write("No solutions found.\n");
        } else {
            for (int idx = 0; idx < solutions.size(); idx++) {
                writer.write("Solution " + (idx + 1) + ":\n");
                printPuzzle(writer, solutions.get(idx));
                writer.write("\n");
            }
        }
        writer.write(String.format("%s Time: %.2f ms\n\n", method, time / 1e6));
    }

    // Method to print the Sudoku puzzle in a readable format to file
    private static void printPuzzle(BufferedWriter writer, int[] puzzle) throws IOException {
        int size = (int) Math.sqrt(puzzle.length); // Calculate grid size dynamically
        for (int i = 0; i < puzzle.length; i++) {
            writer.write((puzzle[i] == 0 ? "." : Integer.toString(puzzle[i])) + " ");
            if ((i + 1) % size == 0) writer.write("\n");
        }
    }
}
