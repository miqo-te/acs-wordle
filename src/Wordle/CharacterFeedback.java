package Wordle;

public class CharacterFeedback {
    private char character;
    private boolean isCorrectPosition;
    private boolean isPresentInWord;

    public void setCharacter(char character) {
        this.character = character;
    }

    public char getCharacter() {
        return character;
    }

    public void setIsCorrectPosition(boolean isCorrectPosition) {
        this.isCorrectPosition = isCorrectPosition;
    }

    public boolean getCorrectPosition() {
        return isCorrectPosition;
    }

    public void setIsPresentInWord(boolean isPresentInWord) {
        this.isPresentInWord = isPresentInWord;
    }

    public boolean getPresentInWord() {
        return isPresentInWord;
    }
}
