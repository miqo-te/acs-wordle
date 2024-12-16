package Wordle;

import java.sql.*;


public class DbFunctions {
    public Connection connectToDb(String dbname, String user, String password) {
        Connection con = null;
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbname, user, password);
            if (con != null){
                System.out.println("Connected to PostgreSQL database");
            }
            else {
                System.out.println("Failed to connect to PostgreSQL database");
            }
//            assert con != null;
//            System.out.println("Connection closed!");

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return con;
    }

    public void dropConstraints(Connection con) {
        Statement statement;
        try {
            statement = con.createStatement();
            statement.executeUpdate("ALTER TABLE games DROP CONSTRAINT fk_player_id;");
            statement.executeUpdate("ALTER TABLE games DROP CONSTRAINT fk_word_id;");
            statement.executeUpdate("ALTER TABLE scores DROP CONSTRAINT fk_player_id;");
            statement.executeUpdate("ALTER TABLE scores DROP CONSTRAINT fk_game_id;");
            System.out.println("Table constraints dropped");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void dropTables(Connection con) {
        Statement statement;
        try {
            statement = con.createStatement();
            statement.executeUpdate("DROP TABLE IF EXISTS players, words, scores, games;");
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
                    CREATE TABLE players (
                        player_id   SERIAL PRIMARY KEY,
                        player_name VARCHAR(3) NOT NULL UNIQUE,
                        play_count  INT        NOT NULL DEFAULT 0,
                            CONSTRAINT ch_play_count CHECK (play_count >= 0),
                        total_wins  INT        NOT NULL DEFAULT 0,
                            CONSTRAINT ch_total_wins CHECK (total_wins >= 0)
                    );
                    """);
            System.out.println("Table players created");
            statement.executeUpdate("""
                    CREATE TABLE words (
                        word_id SERIAL PRIMARY KEY,
                        word    CHAR(5) NOT NULL UNIQUE
                    );
                    """);
            System.out.println("Table words created");
            statement.executeUpdate("""
                    CREATE TABLE scores (
                        score_id  SERIAL PRIMARY KEY,
                        game_id   INT NOT NULL,
                        player_id INT NOT NULL,
                        score     INT     NOT NULL DEFAULT 0,
                            CONSTRAINT ch_valid_score CHECK (score BETWEEN 0 AND 10000)
                    );
                    """);
            System.out.println("Table scores created");
            statement.executeUpdate("""
                    CREATE TABLE games (
                        game_id   SERIAL PRIMARY KEY,
                        player_id INT       NOT NULL,
                        word_id   INT       NOT NULL,
                        date      DATE          NOT NULL
                    );
                    """);
            statement.executeUpdate("""
                    ALTER TABLE scores
                        ADD
                            CONSTRAINT fk_game_id FOREIGN KEY (game_id) REFERENCES games (game_id),
                        ADD
                            CONSTRAINT fk_player_id FOREIGN KEY (player_id) REFERENCES players (player_id);
                    """);
            statement.executeUpdate("""
                    ALTER TABLE games
                        ADD
                            CONSTRAINT fk_player_id FOREIGN KEY (player_id) REFERENCES players (player_id),
                        ADD
                            CONSTRAINT fk_word_id FOREIGN KEY (word_id) REFERENCES words (word_id);
                    """);
            System.out.println("Table games created");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

//    public void readPlayerName(Connection con, String playerName) {
//        Statement statement;
//        ResultSet rs = null;
//        try {
//            String querry = String.format("SELECT * " +
//                    "FROM players " +
//                    "WHERE player_name = %s", playerName);
//            statement = con.createStatement();
//            rs = statement.executeQuery(querry);
//            while (rs.next()) {
//                rs.getString(2);
//            }
//        }
//        catch(Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }

//

    public void addPlayerRow(Connection con, String playerName, int playCount, int totalWins) {
        String query = "INSERT INTO players (player_name) VALUES (?)";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, playerName.toUpperCase());
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Row added successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error adding player: " + e.getMessage());
        }
    }
}