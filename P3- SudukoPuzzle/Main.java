import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String filePath = "SudokuPuzzles.txt";

        try {
            // Read Sudoku puzzles from file
            List<int[]> puzzles = readSudokuPuzzles(filePath);

            for (int i = 0; i < puzzles.size(); i++) {
                System.out.println("Puzzle " + (i + 1) + ":");
                printPuzzle(puzzles.get(i));

                // Create a Sudoku graph for the puzzle
                SudokuGraph graph = new SudokuGraph(9);
                for (int j = 0; j < puzzles.get(i).length; j++) {
                    if (puzzles.get(i)[j] != 0) {
                        graph.setValue(j, puzzles.get(i)[j]);
                    }
                }

                System.out.println("\nSolving with BFS...");
                boolean solved = SudokuSolverBFS.solve(graph);

                if (!solved) {
                    System.out.println("No solution found for Puzzle " + (i + 1) + ".");
                }

                System.out.println();
            }
        } catch (IOException e) {
            System.err.println("Error reading Sudoku puzzles: " + e.getMessage());
        }
    }

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

    private static int[] convertToPuzzleArray(List<Integer> currentPuzzle) {
        int[] puzzleArray = new int[81];
        for (int i = 0; i < currentPuzzle.size(); i++) {
            puzzleArray[i] = currentPuzzle.get(i);
        }
        return puzzleArray;
    }

    private static void printPuzzle(int[] puzzle) {
        for (int i = 0; i < puzzle.length; i++) {
            System.out.print((puzzle[i] == 0 ? "." : puzzle[i]) + " ");
            if ((i + 1) % 9 == 0) System.out.println();
        }
    }
}