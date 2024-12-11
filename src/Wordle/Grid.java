package Wordle;

import java.util.Arrays;

public class Grid {
    private final String[][] grid;
    private final int rows;
    private final int columns;

    public Grid(int rows, int columns) {
        this.columns = columns;
        this.rows = rows;
        this.grid = new String[rows][columns];

        // Fill the grid with null or empty placeholders initially
        for (String[] row : grid) {
            Arrays.fill(row, null);
        }
    }

    public String[][] getGrid() {
        return grid;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public void fillRow(int rowIndex, String[] feedback) {
        if (rowIndex < 0 || rowIndex >= rows) {
            throw new IllegalArgumentException("Invalid row index");
        }
        if (feedback.length != columns) {
            throw new IllegalArgumentException("Feedback must have exactly " + columns + " columns");
        }
        grid[rowIndex] = feedback;
    }

    public void print() {
        System.out.print("\033[H\033[2J"); // Clear console
        System.out.flush(); // Flush console output
        System.out.println(this); // Print the grid representation
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        // Top border of the grid
        output.append("┌───┬───┬───┬───┬───┐\n");
        // Iterate through each row
        for (int i = 0; i < rows; i++) {
            output.append("│");
            for (int j = 0; j < columns; j++) {
                String cellContent = (grid[i][j] != null) ? grid[i][j] : " "; // Empty cell if null
                String strippedContent = cellContent.replaceAll("\033\\[[;\\d]*m", ""); // Remove ANSI codes
                int padding = 3 - strippedContent.length(); // Calculate padding for alignment
                // Add the cell content with padding
                output.append(" ".repeat(padding / 2))
                        .append(cellContent)
                        .append(" ".repeat((padding + 1) / 2))
                        .append("│");
            }
            output.append("\n");
            // Add row separators
            if (i < rows - 1) {
                output.append("├───┼───┼───┼───┼───┤\n");
            }
        }
        // Bottom border of the grid
        output.append("└───┴───┴───┴───┴───┘\n");

        return output.toString();
    }
}