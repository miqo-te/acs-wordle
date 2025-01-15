package Wordle;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class WordleGame {
    private boolean gameWon;
    private int gameId;
    private Grid grid;
    protected final byte MAX_ATTEMPTS = 6;
    protected String secretWord;
    private boolean isGameOver;
    private byte currentAttempt;
    private int attempts;
    private Player player;
    private int gameScore;
    List<String> attemptsList = new ArrayList<>();

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public WordleGame() {
        setPlayer(player);
        setSecretWord(secretWord);
        setCurrentAttempt((byte) 0);
        setAttempts(0);
        setGameScore(0);
        setIsGameOver(false);
        setGameId(gameId);
    }

    public void setGameWon(boolean gameWon) {
        this.gameWon = gameWon;
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
        File words = new File("words.txt");
        Scanner keyboard = new Scanner(System.in);
        DbFunctions db = new DbFunctions();
        Connection con = db.connectToDb();
        WordList wordList = new WordList(new HashSet<>());
//        db.dropConstraints(con);
//        db.dropTables(con);
//        db.createTable(con);
//        try {
//            db.addWordsToDb(con, words);
//            System.out.println("Words added to the database");
//        } catch (Exception e) {
//            System.out.println(" Failed to load the words file");
//        }
        wordList.loadWordsFromDatabase(con);
        String playerName = " ";
        Player player = new Player(playerName);
        while (true) {
            System.out.println("Welcome to Wordle!");
            System.out.println("1. Start Game");
            System.out.println("2. Load Game");
            System.out.println("3. Player management");
            System.out.println("4. Leaderboard");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = keyboard.nextInt();
            keyboard.nextLine(); // Consume newline

            if (choice == 1) {
                boolean startGameMenu = false;
                while (!startGameMenu) {
                    System.out.println("1. Create a new player");
                    System.out.println("2. Select an existing player");
                    System.out.println("3. Back");
                    System.out.print("Choose an option: ");
                    int startMenu = keyboard.nextInt();
                    keyboard.nextLine();
                    switch (startMenu) {
                        case 1:
                            System.out.print("Write your name (3 characters long): ");
                            playerName = keyboard.nextLine();
                            System.out.print("Write your email address: ");
                            String email = keyboard.nextLine().strip();
                            while (!player.isValidName(playerName) || player.playerExists(con, playerName)) {
                                System.out.println("Please enter a valid name: ");
                                playerName = keyboard.nextLine();
                            }
                            player.addPlayer(con, playerName, email);
                            String guess = "";
                            grid = new Grid(6, 5);
                            secretWord = wordList.getRandomWord();
                            System.out.println("Secret word for testing: " + secretWord); // Printing the secret word for testing purposes!!
                            grid.print();
                            long startTime = System.currentTimeMillis();
                            Attempt attempt = new Attempt(guess, secretWord);
                            GameState gameState = new GameState(gameId);
                            gameWon = attempt.isGameWon();
                            while (!attempt.isGameOver()) {
                                guess = keyboard.nextLine().toUpperCase();
                                if (guess.equalsIgnoreCase("SAVE")) {
                                    endGame(db.getPlayerIdByName(con, playerName), db.getWordIdByWord(con, secretWord), con, db);
                                    // Call the saveGame method and pass the list of attempts so far
                                    gameState.saveGame(con, attemptsList, db.getGameId(con), db.getWordIdByWord(con, secretWord));
                                    endSession();
                                    break;
                                } else if (attempt.submitGuess(guess, secretWord, currentAttempt) && wordList.containsWord(guess)) {
                                    // If the guess is valid, process it
                                    attempt.generateFeedback(guess, secretWord);
                                    fillGrid(guess);
                                    grid.print();
                                    attemptsList.add(guess); // Add the guess to the list of attempts for saving
                                    currentAttempt++;
                                }
                            }
                            fillGrid(guess);
                            long endTime = System.currentTimeMillis();
                            gameWon =  attempt.isGameWon();
                            endGame(attempts, startTime, endTime, db.getPlayerIdByName(con, playerName), db.getWordIdByWord(con, secretWord), con, db, gameWon);
                            attempts = 0;
                            currentAttempt = (byte) 0;
                            startGameMenu = true;

                            break;
                        case 2:
                            System.out.println("Here are all the players:");
                            player.getPlayers(con);
                            System.out.println("Choose an existing player: ");
                            String name = keyboard.nextLine();
                            guess = "";
                            grid = new Grid(6, 5);
                            secretWord = wordList.getRandomWord();
                            System.out.println("Secret word for testing: " + secretWord); // Printing the secret word for testing purposes!!
                            grid.print();
                            attempt = new Attempt(guess, secretWord);
                            startTime = System.currentTimeMillis();
                            gameState = new GameState(gameId);
                            while (!attempt.isGameOver()) {
                                guess = keyboard.nextLine().toUpperCase();
                                if (guess.equalsIgnoreCase("SAVE")) {
                                    endGame(db.getPlayerIdByName(con, name), db.getWordIdByWord(con, secretWord), con, db);
                                    // Call the saveGame method and pass the list of attempts so far
                                    gameState.saveGame(con, attemptsList, db.getGameId(con), db.getWordIdByWord(con, secretWord));
                                    endSession();
                                    break;
                                } else if (attempt.submitGuess(guess, secretWord, currentAttempt) && wordList.containsWord(guess)) {
                                    // If the guess is valid, process it
                                    attempt.generateFeedback(guess, secretWord);
                                    fillGrid(guess);
                                    grid.print();
                                    attemptsList.add(guess); // Add the guess to the list of attempts for saving
                                    currentAttempt++;
                                }
                            }
                            fillGrid(guess);
                            endTime = System.currentTimeMillis();
                            gameWon =  attempt.isGameWon();
                            endGame(attempts, startTime, endTime, db.getPlayerIdByName(con, name), db.getWordIdByWord(con, secretWord), con, db, gameWon);
                            attempts = 0;
                            currentAttempt = (byte) 0;
                            startGameMenu = true;
                            break;
                        case 3:
                            startGameMenu = true;
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }


                }
            } else if (choice == 2) {
                long startTime = System.currentTimeMillis();
                grid = new Grid(6, 5);
                int savedWordId = db.getWordIdFromGameState(con);
                String savedSecretWord = db.getWord(con, savedWordId);
                Attempt savedAttempt = new Attempt(savedSecretWord, secretWord);
                if (savedSecretWord == null) {
                    throw new IllegalStateException("Error: Secret word is null! Check database retrieval logic.");
                }

                secretWord = savedSecretWord;
                GameState gameState = new GameState(gameId);
                StringBuilder loadedWord = new StringBuilder();
                System.out.println("Saved secret word: " + savedSecretWord);
                for (int attemptNumber = 1; attemptNumber <= db.getNumberOfAttempts(con); attemptNumber++) {
                    // Load word from GameState table
                    String word = String.valueOf(gameState.loadWord(con, attemptNumber, savedWordId));

                    if (word != null) {
                        loadedWord.append(word);
                    } else {
                        System.out.println("No data found for attempt " + attemptNumber + ", skipping...");
                        continue;
                    }

                    // Generate feedback and fill grid
                    fillGrid(loadedWord.toString());
                    loadedWord.setLength(0);
                }
                grid.print();
                String guess = " ";
                int savedAttemptNumber = db.getNumberOfAttempts(con);
                while (!savedAttempt.isGameOver()) {
                    guess = keyboard.nextLine().toUpperCase();
                    if (guess.equalsIgnoreCase("SAVE")) {
                        // Call the saveGame method and pass the list of attempts so far
                        gameState.saveGame(con, attemptsList, db.getGameId(con), db.getWordIdByWord(con, secretWord));
                        endSession();
                        break;
                    } else if (savedAttempt.submitGuess(guess, secretWord, savedAttemptNumber) && wordList.containsWord(guess)) {
                        // If the guess is valid, process it
                        savedAttempt.generateFeedback(guess, secretWord);
                        fillGrid(guess);
                        grid.print();
                        attemptsList.add(guess); // Add the guess to the list of attempts for saving
                        savedAttemptNumber++;
                    }
                }
                long endTime = System.currentTimeMillis();
                gameWon =  savedAttempt.isGameWon();
                endGame(attempts, startTime, endTime, db.getPlayerIdByName(con, "SAV"), db.getWordIdByWord(con, secretWord), con, db, gameWon);
                fillGrid(guess);
                attempts = 0;
            } else if (choice == 3) {
                boolean back = false;
                while (!back) {
                    System.out.println("1. Create a new player");
                    System.out.println("2. Select an existing player");
                    System.out.println("3. Back");
                    System.out.print("Choose an option: ");
                    int playerMenu = keyboard.nextInt();
                    keyboard.nextLine();
                    switch (playerMenu) {
                        case 1:
                            System.out.println("Write your name (3 characters long) and email: ");
                            playerName = keyboard.nextLine();
                            String email = keyboard.nextLine();
                            while (!player.isValidName(playerName) || player.playerExists(con, playerName)) {
                                System.out.println("Please enter a valid name: ");
                                playerName = keyboard.nextLine();
                            }
                            player.addPlayer(con, playerName, email);
                            break;
                        case 2:
                            System.out.println("Here are all the players:");
                            player.getPlayers(con);
                            System.out.println("Choose an existing player: ");
                            String playerChoice = keyboard.nextLine();
                            player.showPlayerInfo(con, playerChoice);
                            break;
                        case 3:
                            back = true;
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                }

            } else if (choice == 4) {
                boolean back = false;
                while (!back) {
                    System.out.println("1. Show the leaderboard");
                    System.out.println("2. Show the leaderboard for a specific player");
                    System.out.println("3. Back");
                    System.out.print("Choose an option: ");
                    int leaderBoard = keyboard.nextInt();
                    keyboard.nextLine();
                    switch (leaderBoard) {
                        case 1:
                            db.printLeaderboard(con);
                            break;
                        case 2:
                            System.out.println("Here are all the players:");
                            player.getPlayers(con);
                            System.out.println("Choose an existing player: ");
                            String playerChoiceLeaderBoard = keyboard.nextLine();
                            db.printLeaderboard(con, playerChoiceLeaderBoard);
                            break;
                        case 3:
                            back = true;
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                }
            } else if (choice == 5) {
                db.closeConnection(con);
                System.out.println("Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice, please try again.");
            }
        }
    }

    public void endSession() {
        System.out.println("Game session ended");
        System.exit(0);
    }

    public void fillGrid(String guess) {
        Attempt attempt = new Attempt(guess, secretWord);
        String[] feedback = attempt.generateFeedback(guess, secretWord); // Generate feedback for the guess
        grid.fillRow(attempts, feedback); // Fill the current row with feedback
        attempts++; // Increment the attempt counter
        //grid.print(); // Print the updated grid
    }

    public void endGame(int attemptsUsed, long startTime, long endTime, int playerId, int wordId, Connection con, DbFunctions dbFunctions, boolean gameOver) {
        // Calculate time taken
        long timeTakenInSeconds = (endTime - startTime) / 1000;

        // Calculate the score
        int score = calculateScore(attemptsUsed, timeTakenInSeconds, gameWon);
        System.out.println("Final Score: " + score);

        // Save the score to the database
        dbFunctions.addGamesToDb(con, playerId, wordId, score);
    }

    public void endGame(int playerId, int wordId, Connection con, DbFunctions dbFunctions) {
        dbFunctions.addGamesToDb(con, playerId, wordId, 0);
    }

    private int calculateScore(int attemptsUsed, long timeTakenInSeconds, boolean gameWon) {
        if (!gameWon) {
            return 0;
        }
        final int BASE_SCORE = 1000;
        final int ATTEMPT_PENALTY = 100;
        final int TIME_THRESHOLD = 300; // 5 minutes

        int attemptPenalty = Math.max(0, ATTEMPT_PENALTY * (attemptsUsed - 1));
        int baseScoreAfterAttempts = Math.max(0, BASE_SCORE - attemptPenalty);

        double timeBonus = Math.max(0.5, 1.0 - ((double) timeTakenInSeconds / TIME_THRESHOLD));
        return (int) (baseScoreAfterAttempts * timeBonus);
    }
}
