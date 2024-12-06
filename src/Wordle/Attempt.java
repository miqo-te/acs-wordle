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
        for (int i = 0; i < 5; i++) {
            char currentChar = guess.charAt(i);
            StringBuilder characterFeedback = new StringBuilder();
            String green = "\033[32m";
            String yellow = "\033[93m";
            String reset = "\033[0m";
            boolean isCorrectPosition = currentChar == secretWord.charAt(i);
            boolean isPresentInWord = secretWord.contains(String.valueOf(currentChar));
            if (isCorrectPosition) {
                characterFeedback.append(green).append(currentChar).append(reset);
            } else if (isPresentInWord) {
                characterFeedback.append(yellow).append(currentChar).append(reset);
            } else if(currentChar >= 'A' && currentChar <= 'Z') {
                characterFeedback.append(currentChar);
            }
            guessFeedback.append(characterFeedback);
        }
        System.out.println(guessFeedback);
        guessFeedback.setLength(0);
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
