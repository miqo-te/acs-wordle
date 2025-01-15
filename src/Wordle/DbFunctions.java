package Wordle;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;


public class DbFunctions {
    public Connection connectToDb() {
        Connection con = null;
        try {
            // Load PostgreSQL driver
            Class.forName("org.postgresql.Driver");

            // Use your Railway connection URL here
            String url = "jdbc:postgresql://junction.proxy.rlwy.net:47406/railway";
            String user = "postgres";  // Username provided in the URL
            String password = "xdfLqGhKZxCTPIwBAqbkCHmsYKRvcmPt";  // Password provided in the URL

            // Establish the connection
            con = DriverManager.getConnection(url, user, password);

            System.out.println("Connected to the database successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL Driver not found. Ensure it's in the classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection failed.");
            e.printStackTrace();
        }
        return con;
    }




    public void dropConstraints(Connection con) {
        Statement statement;
        try {
            statement = con.createStatement();
            statement.executeUpdate("""
                     ALTER TABLE game_state
                               DROP CONSTRAINT IF EXISTS fk_word_id,
                               DROP CONSTRAINT IF EXISTS fk_game_id;
                    
                           ALTER TABLE games
                               DROP CONSTRAINT IF EXISTS fk_word_id,
                               DROP CONSTRAINT IF EXISTS fk_player_id;
                    """);
            System.out.println("Table constraints dropped");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void dropTables(Connection con) {
        Statement statement;
        try {
            statement = con.createStatement();
            statement.executeUpdate("DROP TABLE IF EXISTS players, words, game_state, games;");
            System.out.println("Tables dropped");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void createTable(Connection con) {
        Statement statement;
        try {
            statement = con.createStatement();
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS players
                      (
                          player_id   SERIAL PRIMARY KEY,
                          player_name VARCHAR(3) NOT NULL UNIQUE,
                          email       VARCHAR(30) UNIQUE
                      );
                    """);
            System.out.println("Table players created");
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS words
                    (
                        word_id SERIAL PRIMARY KEY,
                        word    CHAR(5) NOT NULL UNIQUE
                    );
                    """);
            System.out.println("Table words created");
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS game_state
                    (
                        letter_id       SERIAL PRIMARY KEY,
                        game_id         INT     NOT NULL,
                        word_id         INT     NOT NULL,
                        attempt_number  INT     NOT NULL
                            CONSTRAINT ch_attempt_nr_range CHECK (attempt_number > 0 AND attempt_number < 6),
                        letter_position INT     NOT NULL
                            CONSTRAINT ch_letter_position_range CHECK (letter_position > 0 AND letter_position < 6),
                        letter          CHAR(1) NOT NULL
                            CONSTRAINT ch_valid_letter CHECK (letter >= 'A' AND letter <= 'Z')
                    );
                    """);
            System.out.println("Table game state created");
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS games
                      (
                          game_id   SERIAL PRIMARY KEY,
                          player_id INT  NOT NULL,
                          word_id   INT  NOT NULL,
                          score     INT  NOT NULL
                              CONSTRAINT ch_valid_score CHECK ( score >= 0 ),
                          date      DATE NOT NULL
                      );
                    """);
            statement.executeUpdate("""
                    ALTER TABLE game_state
                        ADD 
                            CONSTRAINT fk_game_id FOREIGN KEY (game_id) REFERENCES games (game_id),
                        ADD CONSTRAINT fk_word_id FOREIGN KEY (word_id) REFERENCES words (word_id);
                    """);
            statement.executeUpdate("""
                    ALTER TABLE games
                    ADD
                    CONSTRAINT fk_player_id FOREIGN KEY (player_id) REFERENCES players (player_id),
                    ADD
                    CONSTRAINT fk_word_id FOREIGN KEY (word_id) REFERENCES words (word_id);
                    """);
            System.out.println("Table games created");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void addWordsToDb(Connection con, File file) throws FileNotFoundException {
        Scanner keyboard = new Scanner(file);
        String query = "INSERT INTO words (word) VALUES (?)";
        try {
            while (keyboard.hasNextLine()) {
                try {
                    PreparedStatement statement = con.prepareStatement(query);
                    statement.setString(1, keyboard.nextLine().toUpperCase());
                    statement.executeUpdate();
                } catch (Exception e) {
                    System.out.println("Failed to add words to the database.");
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to add words to the database.");
        }
    }

    public int getPlayerIdByName(Connection con, String playerName) {
        String query = "SELECT player_id FROM players WHERE player_name = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            // Set the player_name parameter
            statement.setString(1, playerName.toUpperCase()); // Ensure case-insensitivity if needed

            // Execute the query
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("player_id"); // Return the player_id
                } else {
                    System.out.println("No player found with the name '" + playerName + "'.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving player_id: " + e.getMessage());
        }
        return -1; // Return -1 if no player is found or an error occurs
    }

    public int getWordIdByWord(Connection con, String secretWord) {
        String query = "SELECT word_id FROM words WHERE word = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            // Set the player_name parameter
            statement.setString(1, secretWord.toUpperCase()); // Ensure case-insensitivity if needed

            // Execute the query
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("word_id"); // Return the player_id
                } else {
                    System.out.println("No word found in the database.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving word_id: " + e.getMessage());
        }
        return -1; // Return -1 if no player is found or an error occurs
    }

    public int getWordIdFromGameState(Connection con) {
        String query = "SELECT word_id FROM game_state";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            // Execute the query
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("word_id"); // Return the player_id
                } else {
                    System.out.println("No word found in the database.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving word_id: " + e.getMessage());
        }
        return -1; // Return -1 if no player is found or an error occurs
    }

    public String getWord(Connection con, int wordId) {
        String query = "SELECT word FROM words WHERE word_id = ?";
        String word = "";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, wordId);
            // Execute the query
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("word"); // Return the player_id
                } else {
                    System.out.println("No word found in the database.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving word: " + e.getMessage());
        }
        return " "; // Return -1 if no player is found or an error occurs
    }

    public void addGamesToDb(Connection con, int playerId, int wordId, int score) {
        String insertScoreSQL = "INSERT INTO games (player_id, word_id, score, date) VALUES (?, ?, ?, CURRENT_DATE)";
        try (PreparedStatement insertScoreStmt = con.prepareStatement(insertScoreSQL)) {
            // Set the parameters for the INSERT query
            insertScoreStmt.setInt(1, playerId);
            insertScoreStmt.setInt(2, wordId);
            insertScoreStmt.setInt(3, score);

            // Execute the insertion
            int rowsAffected = insertScoreStmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Game session added to the database.");
            } else {
                System.out.println("Failed to add the game to the database.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding the game session: " + e.getMessage());
        }
    }

    public int getNumberOfAttempts(Connection con) {
        String query = "SELECT attempt_number FROM game_state ORDER BY attempt_number DESC LIMIT 1";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            // Execute the query
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("attempt_number"); // Return the player_id
                } else {
                    System.out.println("Error retrieving number attempts from the game state table.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving word_id: " + e.getMessage());
        }
        return -1; // Return -1 if no player is found or an error occurs
    }

    public int getGameId(Connection con) {
        int gameId = 0;
        String query = "SELECT game_id FROM games ORDER BY game_id DESC LIMIT 1";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            // Execute the query
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("game_id"); // Return the player_id
                } else {
                    System.out.println("No game found in the database.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving game_id: " + e.getMessage());
        }
        return -1; // Return -1 if no player is found or an error occurs
    }

    public void printLeaderboard(Connection con) {
        Statement statement;
        ResultSet rs;
        String query = """
                SELECT player_name, score, date
                FROM games g
                         JOIN players p ON g.player_id = p.player_id
                ORDER BY score DESC
                    FETCH NEXT 5 ROWS ONLY;
                """;
        try {
            statement = con.createStatement();
            rs = statement.executeQuery(query);
            System.out.println("Leaderboard:");
            System.out.println("Player     Score     Date\n");
            while (rs.next()) {
                System.out.println(rs.getString("player_name") + "        " + rs.getInt("score") + "       " + rs.getDate("date"));
            }
        }
        catch (Exception e) {
            System.out.println("Error retrieving leaderboard: " + e.getMessage());
        }
    }

    public void printLeaderboard (Connection con, String playerName) {
        ResultSet rs = null;
        String query = """
            SELECT player_name, score, date
            FROM games g
                     JOIN players p ON g.player_id = p.player_id
            WHERE player_name = ?
            ORDER BY score DESC;
            """;
        try {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, playerName);
            rs = preparedStatement.executeQuery();
            System.out.println("Scores for " + playerName + ":");
            System.out.println("Score     Date\n");
            while (rs.next()) {
                System.out.println(rs.getInt("score") + "       " + rs.getDate("date"));
            }
        } catch (Exception e) {
            System.out.println("Error retrieving scores for " + playerName + ": " + e.getMessage());
        }
    }

    public void closeConnection(Connection con) {
        assert con != null;
        System.out.println("Connection closed!");
    }
}