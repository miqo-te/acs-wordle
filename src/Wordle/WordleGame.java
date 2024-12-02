package Wordle;

import java.util.Set;

public class WordleGame {
    protected final byte MAX_ATTEMPTS = 6;
    private String secretWord;
    private boolean isGameOver;
    private byte currentAttempt;
    private Set<String> attempts;
    private Player player;
    private int gameScore;

    public WordleGame(String secretWord) {
        setSecretWord(secretWord);
    }

    public byte getMAX_ATTEMPTS() {
        return MAX_ATTEMPTS;
    }

    public String getSecretWord() {
        return secretWord;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public byte getCurrentAttempt() {
        return currentAttempt;
    }

    public void setCurrentAttempt(byte currentAttempt) {
        this.currentAttempt = currentAttempt;
    }

    public Set<String> getAttempts() {
        return attempts;
    }

    public void setAttempts(Set<String> attempts) {
        this.attempts = attempts;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getGameScore() {
        return gameScore;
    }

    public void setGameScore(int gameScore) {
        this.gameScore = gameScore;
    }

    public void setSecretWord(String secretWord) {
        this.secretWord = secretWord;
    }
}
