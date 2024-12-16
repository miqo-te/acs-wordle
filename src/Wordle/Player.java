package Wordle;

import java.sql.*;

public class Player {
    private String playerName;
    DbFunctions db = new DbFunctions();

    public Player(String playerName) {
        if (!isValidName(playerName)) {
            throw new IllegalArgumentException("Player name must be exactly 3 characters long.");
        }
        setPlayerName(playerName);
    }

    public void setPlayerName(String playerName) {
        if (playerName == null || playerName.isEmpty()) {
            this.playerName = playerName;
        } else {
            this.playerName = " ";
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    private boolean isValidName(String name) {
        return name != null && name.length() == 3;
    }

//    public boolean playerExists(Connection con, String playerName) throws SQLException {
//        Statement statement;
//        ResultSet resultSet;
//        String query = String.format("SELECT COUNT(*) " +
//                "FROM Players " +
//                "WHERE player_name = %s", playerName);
//        try {
//            statement = con.createStatement();
//            statement.execute(query);
//            resultSet = statement.getResultSet();
//            if (resultSet.next()) {
//                return true;
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        return false;
//    }

//

    public boolean playerExists(Connection con, String playerName) {
        String query = "SELECT COUNT(*) FROM players WHERE player_name = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, playerName.trim().toUpperCase());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking player existence: " + e.getMessage());
        }
        return false;
    }

    public void addPlayer(Connection con) {
        if (!playerExists(con, playerName)) {
            db.addPlayerRow(con, playerName.trim(), 0, 0);
        } else {
            System.out.println("Player already exists in the database.");
        }
    }

//    public void PlayerStats getStats() {
//
//    }
}
