package Wordle;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WordleGame {
    private final String[][] grid;
    private Scanner scanner;
    Scanner keyboard = new Scanner(System.in);
    protected final byte MAX_ATTEMPTS = 6;
    protected String secretWord;
    private boolean isGameOver;
    private byte currentAttempt;
    private int attempts;
    private Player player;
    private int gameScore;

    public WordleGame(Player player, String secretWord) {
        setPlayer(player);
        setSecretWord(secretWord);
        setCurrentAttempt((byte) 0);
        setAttempts(0);
        setGameScore(0);
        setIsGameOver(false);
        this.grid = new String[MAX_ATTEMPTS][secretWord.length()];
        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            for (int j = 0; j < secretWord.length(); j++) {
                grid[i][j] = " "; // Initialize as empty strings
            }
        }
    }

    public byte getMAX_ATTEMPTS() {
        return MAX_ATTEMPTS;
    }

    public String getSecretWord() {
        return secretWord;
    }

    public boolean getIsGameOver() {
        return isGameOver;
    }

    public void setIsGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public byte getCurrentAttempt() {
        return currentAttempt;
    }

    public void setCurrentAttempt(byte currentAttempt) {
        this.currentAttempt = currentAttempt;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
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

    public void startGame() {
        setIsGameOver(false);
        setCurrentAttempt((byte) 0);
        setGameScore(0);
        printGrid();
    }

    public boolean submitGuess(String guess) {
        if (isGameOver) {
            System.out.println("Game over");
            return false;
        }

        if (guess.length() != 5 || guess.contains(" ")) {
            System.out.println("Invalid guess. Write a 5 letter word without spaces.");
            return false;
        }

        if (guess.equals(secretWord)) {
            System.out.println("Congratulations! You've guessed the word correctly.");
            isGameOver = true;

        } else if (attempts >= MAX_ATTEMPTS - 1) {
            System.out.println("Out of attempts! The secret word was: " + secretWord);
            isGameOver = true;
        }
      return true;
    }

//    public List<CharacterFeedback> getFeedback(String guess) {
//        List<CharacterFeedback> feedback = new ArrayList<CharacterFeedback>();
//        for (int i = 0; i < 5; i++) {
//            char guessedChar = guess.charAt(i);
//            boolean isCorrectPosition = guessedChar == secretWord.charAt(i);
//            boolean isPresentInWord = secretWord.contains(Character.toString(guessedChar));
//            feedback.add(new CharacterFeedback(guessedChar, isCorrectPosition, isPresentInWord));
//        }
//      return feedback;
//    }

    public void endSession() {
        setIsGameOver(true);
        System.out.println("Game session ended");
    }

    public void printGrid() {
        System.out.println("┌───┬───┬───┬───┬───┐");
        for (int i = 0; i < grid.length; i++) {
            System.out.print("│");
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] != null) {
                    // Ensure each cell has exactly 3 printable spaces
                    String cell = grid[i][j].replaceAll("\033\\[[;\\d]*m", ""); // Remove ANSI codes for length
                    int padding = 3 - cell.length(); // Calculate padding
                    System.out.print(" ".repeat(padding / 2) + grid[i][j] + " ".repeat((padding + 1) / 2) + "│");
                } else {
                    System.out.print("   │"); // Empty space for unused cells
                }
            }
            System.out.println();
            if (i < grid.length - 1) {
                System.out.println("├───┼───┼───┼───┼───┤");
            }
        }
        System.out.println("└───┴───┴───┴───┴───┘");
    }


    public void fillGrid(String guess) {
        if (submitGuess(guess)) {
            Attempt attempt = new Attempt(guess, secretWord);
            String[] feedback = attempt.generateFeedback(guess, secretWord); // Get color-coded feedback

            // Populate the grid row for the current attempt
            for (int i = 0; i < feedback.length; i++) {
                grid[attempts][i] = feedback[i]; // Directly assign each feedback element
            }

            attempts++; // Increment attempts
            System.out.print("\033[H\033[2J"); // Clear console
            System.out.flush();
            printGrid(); // Print the updated grid
        }
    }
}
