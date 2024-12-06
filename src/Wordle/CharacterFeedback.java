package Wordle;

public class CharacterFeedback {
    private String character;
    private boolean isCorrectPosition;
    private boolean isPresentInWord;

    public CharacterFeedback(char character, boolean isCorrectPosition, boolean isPresentInWord) {
        setCharacter(character);
        setIsCorrectPosition(isCorrectPosition);
        setIsPresentInWord(isPresentInWord);
    }

    public void setCharacter(char character) {
        this.character = String.valueOf(character);
    }

    public String getCharacter() {
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

    public void color() {
        if (isCorrectPosition) {
            character = "\033[32m"+character+"\033[0m";
        } else if (isPresentInWord) {
            character = "\033[93m"+character+"\033[0m";
        }
    }
}
