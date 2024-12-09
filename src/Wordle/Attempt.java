package Wordle;

import java.util.List;

public class Attempt {
    protected String guess;
    private final StringBuilder guessFeedback = new StringBuilder();
    private List<CharacterFeedback> feedback;

    public Attempt(String guess, String secretWord) {
        setGuess(guess);
    }

    public String generateFeedback(String guess, String secretWord) {
        guessFeedback.setLength(0); // Reset at the start of the method
        for (int i = 0; i < 5; i++) {
            char currentChar = guess.charAt(i);
            String green = "\033[32m"; // Green for correct position
            String yellow = "\033[93m"; // Yellow for present in the word
            String reset = "\033[0m"; // Reset color
            boolean isCorrectPosition = currentChar == secretWord.charAt(i);
            boolean isPresentInWord = secretWord.contains(String.valueOf(currentChar));

            if (isCorrectPosition) {
                guessFeedback.append(green).append(currentChar).append(reset);
            } else if (isPresentInWord) {
                guessFeedback.append(yellow).append(currentChar).append(reset);
            } else {
                guessFeedback.append(currentChar);
            }
        }
        return guessFeedback.toString();
    }


    public boolean isCorrect(String secretWord) {
        return guess.equals(secretWord);
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }

    public String getGuess() {
        return guess;
    }
}
