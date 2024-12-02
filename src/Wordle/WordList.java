package Wordle;

import java.util.*;

public class WordList {
    private Set<String> words;
    private Random random;

    public WordList(List<String> words) {
        this.words = new HashSet<>(words);
        this.random = new Random();
    }
    public String getRandomWord(){
        List<String> wordsList = new ArrayList<>(words);
        return wordsList.get(random.nextInt(wordsList.size()));
    }
}
