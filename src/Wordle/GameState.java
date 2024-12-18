package Wordle;

import java.sql.*;
import java.util.*;

public class GameState {
    private final int gameId;

    public GameState(int gameId) {
        this.gameId = gameId; // This identifies the saved game uniquely
    }

    /**
     * Saves the current game session to the GameState table.
     * @param con Database connection
     * @param attempts List of attempts; each attempt is a word guessed by the player
     */
    public void saveGame(Connection con, List<String> attempts, int gameId, int wordId) {
        try {
            // Clear existing save for this gameId
            String deleteQuery = """
                    DELETE FROM game_state
                    WHERE EXISTS (
                        SELECT 1
                        FROM game_state
                    );
                    """;
            try (Statement deleteStmt = con.createStatement()) {
                deleteStmt.execute(deleteQuery);
            } catch (SQLException e) {
                System.out.println("Error deleting save: " + e.getMessage());
            }

            // Insert new save into the GameState table
            String insertQuery = "INSERT INTO game_state (game_id, word_id, attempt_number, letter_position, letter) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement insertStmt = con.prepareStatement(insertQuery)) {
                for (int attemptNumber = 1; attemptNumber <= attempts.size(); attemptNumber++) {
                    String word = attempts.get(attemptNumber - 1);
                    for (int pos = 0; pos < word.length(); pos++) {
                        insertStmt.setInt(1, gameId);
                        insertStmt.setInt(2, wordId);
                        insertStmt.setInt(3, attemptNumber);
                        insertStmt.setInt(4, pos + 1); // letter_position starts at 1
                        insertStmt.setString(5, String.valueOf(word.charAt(pos)).toUpperCase());

                        insertStmt.addBatch();
                    }
                }
                insertStmt.executeBatch();
            }
            System.out.println("Game session saved successfully!");
        } catch (SQLException e) {
            System.err.println("Error saving game session: " + e.getMessage());
        }
    }

    /**
     * Loads the saved game session from the GameState table.
     * @param con Database connection
     * @return A list of words (attempts) from the saved game session
     */
    public StringBuilder loadWord(Connection con, int attemptNumber, int wordId) {
        String query = "SELECT letter " +
                "FROM game_state " +
                "WHERE attempt_number = ? " +
                "ORDER BY letter_position ASC";

        StringBuilder loadedWord = new StringBuilder();

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, attemptNumber); // Specify the attempt number to load

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Append each letter to the StringBuilder
                    String letter = rs.getString("letter");
                    loadedWord.append(letter);
                }
            }

            if (loadedWord.isEmpty()) {
                System.out.println("Failed to load the word");
            }
        } catch (SQLException e) {
            System.out.println("Error loading game state: " + e.getMessage());
        }
        return loadedWord;
    }
}
