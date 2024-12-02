package Wordle;

import java.util.List;

public class Attempt {
    protected String guess;
    private List<CharacterFeedback> feedback;

    public Attempt(String guess) {
        setGuess(guess);
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }

    public String getGuess() {
        return guess;
    }

    public boolean isCorrect(String guess) {
        boolean isValid = false;
        if (guess.length() == 5) {
            isValid = true;
        }
        return isValid;
        //deeez nuts
    }

}
