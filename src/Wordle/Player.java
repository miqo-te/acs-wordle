package Wordle;

import java.sql.*;

public class Player {
    private String playerName;
    DbFunctions db = new DbFunctions();

    public Player(String playerName) {
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

    public boolean isValidName(String name) {
        return name != null && name.matches("[A-Za-z]{3}");
    }

    public boolean playerExists(Connection con, String playerName) {
        String checkPlayerSQL = "SELECT COUNT(*) FROM players WHERE player_name = ?";
        try (PreparedStatement checkPlayerStmt = con.prepareStatement(checkPlayerSQL)) {
            checkPlayerStmt.setString(1, playerName.toUpperCase());
            try (ResultSet resultSet = checkPlayerStmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0; // If COUNT > 0, player exists
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking player existence: " + e.getMessage());
        }
        return false;
    }

    public void addPlayer(Connection con, String playerName, String email, int playCount, int totalWins) {
        PreparedStatement checkPlayerStmt = null;
        PreparedStatement insertPlayerStmt = null;
        ResultSet resultSet = null;

        try {
            // SQL query to check if a player with the same name exists
            String checkPlayerSQL = "SELECT player_id FROM players WHERE player_name = ?";
            checkPlayerStmt = con.prepareStatement(checkPlayerSQL);
            checkPlayerStmt.setString(1, playerName);

            resultSet = checkPlayerStmt.executeQuery();

            if (resultSet.next()) {
                // Player already exists
                System.out.println("Player with name '" + playerName.toUpperCase() + "' already exists in the database.");
            } else {
                // SQL query to insert a new player
                String insertPlayerSQL = "INSERT INTO players (player_name, email, play_count, total_wins) VALUES (?, ?, ?, ?)";
                insertPlayerStmt = con.prepareStatement(insertPlayerSQL);

                // Set parameters for the INSERT query
                insertPlayerStmt.setString(1, playerName.toUpperCase());
                insertPlayerStmt.setString(2, email);
                insertPlayerStmt.setInt(3, playCount);
                insertPlayerStmt.setInt(4, totalWins);

                // Execute the insertion
                int rowsAffected = insertPlayerStmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Player '" + playerName + "' added successfully!");
                } else {
                    System.out.println("Failed to add the player.");
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void getPlayers(Connection con) {
        Statement statement;
        String showPlayersSQL = "SELECT player_name FROM players";
        try {
            statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(showPlayersSQL);
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1));
            }
        }
        catch (Exception e) {
            System.out.println("Error showing players");
        }
    }

    public void showPlayerInfo(Connection con, String playerName) {
        String showPlayerInfoSQL = "SELECT * FROM players WHERE player_name = ?";
        try (PreparedStatement statement = con.prepareStatement(showPlayerInfoSQL)) {
            statement.setString(1, playerName.trim().toUpperCase());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    System.out.println("Player ID: " + resultSet.getInt("player_id"));
                    System.out.println("Player Name: " + resultSet.getString("player_name"));
                    System.out.println("Player Email: " + resultSet.getString("email"));
                    System.out.println("The Amount of Games Played: " + resultSet.getInt("play_count"));
                    System.out.println("The Amount of Wins: " + resultSet.getInt("total_wins"));
                }
            }
        } catch (Exception e) {
            System.out.println("Error showing player info: " + e.getMessage());
        }
    }
}
