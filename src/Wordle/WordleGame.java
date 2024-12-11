package Wordle;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class WordleGame {
    List<String> initialWords = Arrays.asList(
            "APPLE", "ALERT", "BEACH", "BROAD", "CHEAP", "DAILY", "BROKE",
            "CHECK", "DANCE", "BROWN", "CHAIR", "CRISP", "EARTH", "FAITH",
            "FIELD", "FLAIR", "FRESH", "GIANT", "GRACE", "HAPPY", "HEART",
            "HOUSE", "INDEX", "JUICE", "LIGHT", "LUNCH", "MONEY", "NIGHT",
            "OCEAN", "PEACH", "PLANE", "PRIZE", "QUIET", "RIVER", "SLEEP",
            "SMILE", "STORM", "SUGAR", "TABLE", "THINK", "TRAIN", "TRUTH",
            "UNION", "VALUE", "VOICE", "WATCH", "WORLD", "WORTH", "YOUTH",
            "AGILE", "ALIVE", "BLEND", "BLOOM", "BLAST", "BRAVE", "BRICK",
            "CHILL", "CIVIC", "CLEAN", "CLOSE", "CLOUD", "COAST", "CRANE",
            "CRAFT", "DAIRY", "DELAY", "DEPTH", "DRIVE", "ELITE", "ENTER",
            "EQUAL", "EVERY", "FLUID", "FOCUS", "FORCE", "FRAME", "FRUIT",
            "GLORY", "GRIND", "GROVE", "HUMOR", "HONEY", "IDEAL", "IMAGE",
            "KNOCK", "LEMON", "LOGIC", "MAGIC", "MIGHT", "MOUNT", "MUSIC",
            "NOVEL", "POWER", "PRIME", "QUIRK", "RAISE", "RIGHT", "SHARE",
            "SHINE", "SPACE", "SOLAR", "SOUND", "STAGE", "TREND", "UNITY"
    );
    WordList wordList = new WordList(initialWords);
    private Grid grid;
    private Scanner scanner;
    Scanner keyboard = new Scanner(System.in);
    protected final byte MAX_ATTEMPTS = 6;
    protected String secretWord;
    private boolean isGameOver;
    private byte currentAttempt;
    private int attempts;
    private Player player;
    private int gameScore;

    public WordleGame() {
        setPlayer(player);
        setSecretWord(secretWord);
        setCurrentAttempt((byte) 0);
        setAttempts(0);
        setGameScore(0);
        setIsGameOver(false);
//        this.grid = new String[MAX_ATTEMPTS][secretWord.length()];
//        for (int i = 0; i < MAX_ATTEMPTS; i++) {
//            for (int j = 0; j < secretWord.length(); j++) {
//                grid[i][j] = " "; // Initialize as empty strings
//            }
//        }
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
        Scanner keyboard = new Scanner(System.in);
//        DbFunctions wordleDb = new DbFunctions();
//        wordleDb.connectToDb("wordle_db", "postgres", "Student_1234");

        while (true) {
            System.out.println("Welcome to Wordle!");
            System.out.println("1. Start Game");
            System.out.println("2. Leaderboard");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = keyboard.nextInt();
            keyboard.nextLine(); // Consume newline

            if (choice == 1) {
                String guess = "";
                grid = new Grid(6, 5);
                secretWord = wordList.getRandomWord();
                System.out.println("Secret word for testing: " + secretWord); // Printing the secret word for testing purposes!!
                grid.print();
                Attempt attempt = new Attempt(guess, secretWord);
                while (!attempt.isGameOver()) {
                    guess = keyboard.nextLine().toUpperCase();
                    if (attempt.submitGuess(guess, secretWord, currentAttempt)) {
                        attempt.generateFeedback(guess, secretWord);
                        fillGrid(guess);
                        currentAttempt++;
                    }
                }
                fillGrid(guess);
            } else if (choice == 2) {
                System.out.println("Hello World"); // Will be replaced with leaderboard eventually
            } else if (choice == 3) {
                System.out.println("Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice, please try again.");
            }
        }
    }

//    public boolean submitGuess(String guess) {
//        if (isGameOver) {
//            System.out.println("Game over");
//            return false;
//        }
//
//        if (guess.length() != 5 || guess.contains(" ")) {
//            System.out.println("Invalid guess. Write a 5 letter word without spaces.");
//            return false;
//        }
//
//        if (guess.equals(secretWord)) {
//            System.out.println("Congratulations! You've guessed the word correctly.");
//            isGameOver = true;
//
//        } else if (attempts >= MAX_ATTEMPTS - 1) {
//            System.out.println("Out of attempts! The secret word was: " + secretWord);
//            isGameOver = true;
//        }
//      return true;
//    }

    public void endSession() {
        setIsGameOver(true);
        System.out.println("Game session ended");
    }

    public void fillGrid(String guess) {
        Attempt attempt = new Attempt(guess, secretWord);
            String[] feedback = attempt.generateFeedback(guess, secretWord); // Generate feedback for the guess
            grid.fillRow(attempts, feedback); // Fill the current row with feedback
            attempts++; // Increment the attempt counter
            grid.print(); // Print the updated grid
    }

//    public void printGrid() {
//        System.out.println("┌───┬───┬───┬───┬───┐");
//        for (int i = 0; i < grid.length; i++) {
//            System.out.print("│");
//            for (int j = 0; j < grid[i].length; j++) {
//                if (grid[i][j] != null) {
//                    // Ensure each cell has exactly 3 printable spaces
//                    String cell = grid[i][j].replaceAll("\033\\[[;\\d]*m", ""); // Remove ANSI codes for length
//                    int padding = 3 - cell.length(); // Calculate padding
//                    System.out.print(" ".repeat(padding / 2) + grid[i][j] + " ".repeat((padding + 1) / 2) + "│");
//                } else {
//                    System.out.print("   │"); // Empty space for unused cells
//                }
//            }
//            System.out.println();
//            if (i < grid.length - 1) {
//                System.out.println("├───┼───┼───┼───┼───┤");
//            }
//        }
//        System.out.println("└───┴───┴───┴───┴───┘");
//    }


//    public void fillGrid(String guess) {
//        if (submitGuess(guess)) {
//            Attempt attempt = new Attempt(guess, secretWord);
//            String[] feedback = attempt.generateFeedback(guess, secretWord); // Get color-coded feedback
//
//            // Populate the grid row for the current attempt
//            for (int i = 0; i < feedback.length; i++) {
//                grid[attempts][i] = feedback[i]; // Directly assign each feedback element
//            }
//
//            attempts++; // Increment attempts
//            System.out.print("\033[H\033[2J"); // Clear console
//            System.out.flush();
//            printGrid(); // Print the updated grid
//        }
//    }


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
}
