package Wordle;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
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

        while (true) {
            System.out.println("Welcome to Wordle!");
            System.out.println("1. Start Game");
            System.out.println("2. Leaderboard");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = keyboard.nextInt();
            keyboard.nextLine(); // Consume newline

            if (choice == 1) {
                String guess = " ";
//                startGame(initialWords, keyboard);
                Player player1 = new Player(0, "MKH");
                WordList wordList = new WordList(initialWords);
                WordleGame gameSession = new WordleGame(player1 ,wordList.getRandomWord());
                Attempt attempt = new Attempt("", gameSession.secretWord);
                gameSession.startGame();
                System.out.println("Secret word for testing: " + gameSession.secretWord); // Printing the secret word for testing purposes!!
                while (!gameSession.getIsGameOver()) {
                    guess = keyboard.nextLine();
                    if (gameSession.submitGuess(guess)) {
                        attempt.generateFeedback(guess.toUpperCase(), gameSession.secretWord);
                        gameSession.fillGrid(guess.toUpperCase());
                    }
                }
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

//    public static void startGame(List<String> initialWords, Scanner keyboard) {
//        WordList words = new WordList(initialWords);
//        Player player1 = new Player(0, "QWE");
//        WordleGame gameSession = new WordleGame(player1, words.getRandomWord());
//
//        String secretWord = gameSession.getSecretWord();
//        char[][] grid = new char[6][5]; // 6 rows for guesses, 5 columns for letters
//        for (char[] row : grid) {
//            Arrays.fill(row, ' '); // Fill grid with spaces initially
//        }
//
//        System.out.println("Selected Category: GENERIC - Secret word (for testing): " + secretWord);
//        printGrid(grid);
//
//        int attempts = 0;
//        boolean guessedCorrectly = false;
//
//        while (attempts < 6 && !guessedCorrectly) {
//            System.out.println("ENTER YOUR 5-LETTER GUESS:");
//            String currentGuess = keyboard.nextLine().toUpperCase();
//
//            if (currentGuess.length() == 5 && initialWords.contains(currentGuess)) {
//                // Update the grid with the current guess
//                grid[attempts] = currentGuess.toCharArray();
//                attempts++;
//
//                // Check if the guess matches the secret word
//                if (currentGuess.equals(secretWord)) {
//                    guessedCorrectly = true;
//                }
//
//                // Clear the console and reprint the grid
//                System.out.print("\033[H\033[2J");
//                System.out.flush();
//                printGrid(grid);
//            } else {
//                System.out.println("INVALID GUESS, TRY AGAIN.");
//            }
//        }
//
//        if (guessedCorrectly) {
//            System.out.println("Congratulations! You guessed the word: " + secretWord);
//        } else {
//            System.out.println("Game over! The secret word was: " + secretWord);
//        }
//    }
//
//    // Function to print the grid
//    public static void printGrid() {
//        char[][] grid = new char[6][5]; // 6 rows for guesses, 5 columns for letters
//        for (char[] row : grid) {
//            Arrays.fill(row, ' '); // Fill grid with spaces initially
//        }
//        System.out.println("┌───┬───┬───┬───┬───┐");
//        for (int i = 0; i < grid.length; i++) {
//            System.out.print("│");
//            for (int j = 0; j < grid[i].length; j++) {
//                System.out.print(" " + grid[i][j] + " │");
//            }
//            System.out.println();
//            if (i < grid.length - 1) {
//                System.out.println("├───┼───┼───┼───┼───┤");
//            }
//        }
//        System.out.println("└───┴───┴───┴───┴───┘");
//    }
}
