/*
Jamil Velez, Wafae Benkassou, Riley Rutigliano
11/19/24
Program 3 - Sudoku Solving
Program that will solve sudoku puzzles fed into it using Depth First Search and/or Depth Limited Search
 */

import java.util.*;
import java.io.*;

//BFS: https://stackoverflow.com/questions/5262308/how-do-implement-a-breadth-first-traversal
//DLS: https://tutorialedge.net/artificial-intelligence/depth-limited-search-in-java/
public class Main {
    public static void main(String[] args) {
        String SudokuFile = "Program3 Sudoku Solving/src/SudokuPuzzles.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(SudokuFile))){
            String line;
            System.out.println("Sudoku puzzles:\n");
            while ((line = br.readLine()) != null){
                System.out.println(line);
            }
        }
        catch (IOException e){
            System.err.println("Error reading file " + e.getMessage());
        }
    }
}