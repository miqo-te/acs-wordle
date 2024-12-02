package Wordle;

public class Player {
    private int playerId;
    private String playerName;

    public Player(int playerId, String playerName) {
        setPlayerId(playerId);
        setPlayerName(playerName);
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

//    public void PlayerStats getStats() {
//
//    }
}
