import java.util.*;

public class SudokuGraph {
    private final int size; // Size of the grid (9 for standard Sudoku)
    private final Map<Integer, Set<Integer>> adjacencyList; // Graph representation
    private final Map<Integer, Set<Integer>> possibleValues; // Possible values for each node

    public SudokuGraph(int size) {
        this.size = size;
        this.adjacencyList = new HashMap<>();
        this.possibleValues = new HashMap<>();
        initializeGraph();
    }

    private void initializeGraph() {
        // Initialize nodes and their connections
        for (int i = 0; i < size * size; i++) {
            adjacencyList.put(i, new HashSet<>());
            possibleValues.put(i, new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9)));
        }

        // Create edges based on Sudoku rules
        for (int i = 0; i < size * size; i++) {
            int row = i / size, col = i % size;

            // Same row and column
            for (int j = 0; j < size; j++) {
                if (j != col) adjacencyList.get(i).add(row * size + j); // Same row
                if (j != row) adjacencyList.get(i).add(j * size + col); // Same column
            }

            // Same 3x3 sub-grid
            int startRow = (row / 3) * 3, startCol = (col / 3) * 3;
            for (int r = startRow; r < startRow + 3; r++) {
                for (int c = startCol; c < startCol + 3; c++) {
                    int node = r * size + c;
                    if (node != i) adjacencyList.get(i).add(node);
                }
            }
        }
    }

    public Set<Integer> getNeighbors(int node) {
        return adjacencyList.get(node);
    }

    public Set<Integer> getPossibleValues(int node) {
        return possibleValues.get(node);
    }

    public void setValue(int node, int value) {
        possibleValues.get(node).clear();
        possibleValues.get(node).add(value);
    }

    public void resetValue(int node) {
        possibleValues.put(node, new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9)));
    }
}
