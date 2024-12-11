package Wordle;

import java.sql.*;


public class DbFunctions {
    public void connectToDb(String dbname, String user, String password) {
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
            assert con != null;
            Statement statement = con.createStatement();
//            statement.executeUpdate("""
//                    CREATE TABLE players (
//                    \tplayer_id BIGINT PRIMARY KEY,
//                    \tusername VARCHAR(3) NOT NULL,
//                    \tplay_count INT NOT NULL CHECK (play_count BETWEEN 0 AND 500)
//                    );
//                    """);
//            System.out.println("Table players created");
//            statement.executeUpdate("""
//                    CREATE TABLE words (
//                    word_id BIGINT PRIMARY KEY,
//                    \tword CHAR(5) NOT NULL,
//                    \tcategory CHAR(255) NOT NULL
//                    """);
//            statement.executeUpdate("""
//                    CREATE TABLE scores (
//                    \tscore_id INT PRIMARY KEY,
//                    \tscore INT NOT NULL CHECK (score BETWEEN 0 AND 10000),
//                    \tguess_count SMALLINT NOT NULL CHECK (guess_count BETWEEN 0 AND 6)
//                    );
//                    """);
//            statement.executeUpdate("""
//                    CREATE TABLE session (
//                    \tsession_id INT PRIMARY KEY,
//                    \tuser_id INT NOT NULL,
//                    \tword CHAR(255) NOT NULL,
//                    \tscore_id INT NOT NULL,
//                    \tdate DATE NOT NULL,
//                    \tFOREIGN KEY (user_id) REFERENCES users(user_id),
//                    \tFOREIGN KEY (word) REFERENCES words(word),
//                    \tFOREIGN KEY (score_id) REFERENCES scores(score_id)
//                    );
//                    """);
            statement.close(); con.close();
            System.out.println("Connection closed!");

        } catch (SQLException | ClassNotFoundException exc) {
            exc.printStackTrace();
        }
    }
}