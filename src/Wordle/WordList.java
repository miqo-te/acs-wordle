package Wordle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class WordList {
    private final Set<String> words;
    private final Random random;

    public WordList(Set<String> words) {
        this.words = new HashSet<>(words);
        this.random = new Random();
    }

    public void loadWordsFromDatabase(Connection con) {
        words.clear(); // Clear the list to avoid duplicates on reloading
        String query = "SELECT word FROM words";
        try (Statement statement = con.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                words.add(rs.getString("word"));
            }
            System.out.println("Words loaded successfully");
        } catch (Exception e) {
            System.out.println("Error loading words: " + e.getMessage());
        }
    }

    public boolean containsWord(String word) {
        return words.contains(word.toUpperCase());
    }

    public Set<String> getWords() {
        return words;
    }

    public String getRandomWord(){
        List<String> wordsList = new ArrayList<>(words);
        return wordsList.get(random.nextInt(wordsList.size()));
    }
}
