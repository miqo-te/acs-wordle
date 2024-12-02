package Wordle;

public class PlayerStats {
    private int totalGamesWon;
    private int totalGamesPlayed;
    private int playerScore;

    public PlayerStats(int totalGamesWon, int totalGamesPlayed, int playerScore) {
        setTotalGamesWon(totalGamesWon);
        setTotalGamesPlayed(totalGamesPlayed);
        setPlayerScore(playerScore);
    }

    public void setTotalGamesWon(int totalGamesWon) {
        this.totalGamesWon = totalGamesWon;
    }

    public int getTotalGamesWon() {
        return totalGamesWon;
    }

    public void setTotalGamesPlayed(int totalGamesPlayed) {
        this.totalGamesPlayed = totalGamesPlayed;
    }

    public int getTotalGamesPlayed() {
        return totalGamesPlayed;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void getStatsSummary() {

    }
}
