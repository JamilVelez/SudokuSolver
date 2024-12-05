// Graph to Solve Suduko
// Why use this? Because it can easily model the relationship between nodes
// Keep track of valid possible values and update dynamically as assigning values
// Apply graph traversal algorithms (BFS or DLS) to search for solutions 


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
            possibleValues.put(i, new HashSet<>());
            for (int val= 1; val<= size; val++) {
                possibleValues.get(i).add(val); //all values are possible (initially)
            
            }
        }

        // Create edges based on Sudoku rules
        for (int i = 0; i < size * size; i++) {
            int row = i / size, col = i % size;

            // Same row and column
            for (int j = 0; j < size; j++) {
                if (j != col) adjacencyList.get(i).add(row * size + j); // Same row
                if (j != row) adjacencyList.get(i).add(j * size + col); // Same column
            }

            // Same sub-grid (sqrt(size)xsqrt(size))
            int subGridSize = (int) Math.sqrt(size);
            int startRow= (row / subGridSize) * subGridSize, startCol = (col / subGridSize) *subGridSize;
            for (int r = startRow; r < startRow + subGridSize; r++) {
                for (int c = startCol; c < startCol + subGridSize; c++) {
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

        for (int neighbor : adjacencyList.get(node)) {
            possibleValues.get(neighbor).remove(value);
        }
    }

    public void resetValue(int node) {
        Set<Integer>possible = new HashSet<>();
        for (int val = 1; val <= size; val++){
            possible.add(val);
        }

        possibleValues.put(node, possible);
    }
    
}
