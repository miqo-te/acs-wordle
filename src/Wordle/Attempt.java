package Wordle;

import java.util.List;

public class Attempt {
    private boolean isGameOver;
    protected String guess;
    private final StringBuilder[] guessFeedback = new StringBuilder[5];
    private List<CharacterFeedback> feedback;

    public Attempt(String guess, String secretWord) {
        setGuess(guess);
    }

    public boolean isCorrect (String secretWord){
        return guess.equals(secretWord);
    }

    public void setGuess (String guess){
        this.guess = guess;
    }

    public String getGuess () {
        return guess;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public boolean submitGuess(String guess, String secretWord, int attempts) {
        if (guess.length() != 5) {
            System.out.println("Invalid guess. Write a 5-letter word without spaces.");
            return false;
        }

        if (guess.equals(secretWord)) {
            System.out.println("Congratulations! You've guessed the word correctly.");
            isGameOver = true;
        } else if (attempts >= 5) {
            System.out.println("Out of attempts! The secret word was: " + secretWord);
            isGameOver = true;
        }

        return !isGameOver; // Return false if the game is over
    }


    public String[] generateFeedback(String guess, String secretWord) {
        String[] feedback = new String[5];
        boolean[] used = new boolean[5]; // To track used letters in the secret word

        String green = "\033[32m"; // Green for correct position
        String yellow = "\033[93m"; // Yellow for present in the word
        String reset = "\033[0m";   // Reset color

        // First pass: Check for correct positions (green)
        for (int i = 0; i < 5; i++) {
            char currentChar = guess.charAt(i);
            if (currentChar == secretWord.charAt(i)) {
                feedback[i] = green + currentChar + reset;
                used[i] = true; // Mark as used
            }
        }
        // Second pass: Check for presence in the word (yellow)
        for (int i = 0; i < 5; i++) {
            if (feedback[i] != null) continue; // Skip already matched letters

            char currentChar = guess.charAt(i);
            boolean matched = false;

            for (int j = 0; j < 5; j++) {
                if (!used[j] && secretWord.charAt(j) == currentChar) {
                    matched = true;
                    used[j] = true; // Mark as used
                    break;
                }
            }
            if (matched) {
                feedback[i] = yellow + currentChar + reset;
            } else {
                feedback[i] = String.valueOf(currentChar); // Uncolored letter
            }
        }
        return feedback;
    }
}
