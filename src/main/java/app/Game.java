package app;

import errors.ErrorMessage;
import utils.Constants;
import utils.InputHandler;
import utils.InputValidator;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.out;

public class Game {
    private InputHandler inputHandler;

    public Game(InputStream inputStream) {
        inputHandler = new InputHandler(inputStream);
    }

    public void setInputStream(InputStream inputStream) {
        this.inputHandler = new InputHandler(inputStream);
    }

    public void setArrayInputStream(String inputStream) {
        this.inputHandler.addTestInput(inputStream);
    }

    private final ErrorMessage errorMessage = new ErrorMessage();
    private Board board;
    private Player[] players;
    private int currentPlayer;
    // Define valid column-row pairs
    private int columns;
    private int rows;
    private int numberOfPlayers;
    private String[] playerNames;
    private boolean gameRunning = true;

    public void initGameInfo() {
        setBoardSize();
        setPlayersNumber();
        setPlayesNames();

        InitGame(columns, rows, playerNames);
        start();
    }


    public String setBoardSize() {
        while (inputHandler.hasNextLine()) {
            String error;
            out.println("Enter Board Size (Allowed sizes: 3x2, 5x4, 8x6, 11x9): ");
            String boardSize = inputHandler.getUserInput("string"); // Read input dynamically

            String[] sizes = boardSize.split("x");

            if (sizes.length == 2) {
                try {
                    int currentColumns = Integer.parseInt(sizes[0].trim());
                    int currentRows = Integer.parseInt(sizes[1].trim());

                    // Validate board size
                    if (InputValidator.isValidBoardSize(boardSize, Constants.VALID_BOARD_SIZES)) {
                        columns = currentColumns;
                        rows = currentRows;
                        out.println("Board size set to: " + columns + "x" + rows);
                        break; // Exit loop after successful validation
                    } else {
                        error = errorMessage.genericErrorMessage(Constants.INVALID_BOARD_SIZE_MSG);
                    }
                } catch (NumberFormatException e) {
                    error = errorMessage.genericErrorMessage(Constants.INVALID_BOARD_SIZE_MSG);
                }
            } else {
                error = errorMessage.genericErrorMessage(Constants.INVALID_BOARD_SIZE_MSG);
            }
            if (!inputHandler.hasNextLine()) {
                return error;
            }

            // Clear input for subsequent retries
//            boardSize = null;
        }
        return "";
    }


    public String setPlayersNumber() {
        while (inputHandler.hasNextLine()) {
            String error;
            out.println("Enter Number of Players (1-4): ");
            String input = inputHandler.getUserInput("string"); // Prompt for input dynamically


            try {
                numberOfPlayers = Integer.parseInt(input);
                // Validate the player count
                if (InputValidator.isValidPlayerCount(numberOfPlayers, Constants.MIN_PLAYERS, Constants.MAX_PLAYERS)) {
                    break; // Valid number of players, exit the loop
                } else {
                    error = errorMessage.genericErrorMessage(Constants.getInvalidPlayerCountMessage());
                    if (!inputHandler.hasNextLine()) {
                        return error;
                    }
                }
            } catch (NumberFormatException e) {
                error = errorMessage.genericErrorMessage(Constants.INVALID_INTEGER_MSG);
                if (!inputHandler.hasNextLine()) {
                    return error;
                }
            }
//            input = null;
        }
        return "";
    }

    public String setPlayesNames() {
        // Validate and Get Player Names
        playerNames = new String[numberOfPlayers];

        for (int i = 0; i < numberOfPlayers; i++) {
            while (inputHandler.hasNextLine()) {
                String error;
                String playerName;
                // Prompt user for player name
                out.println("Enter Player " + (i + 1) + " Name: ");
                playerName = inputHandler.getUserInput("string").trim();

                if (InputValidator.isValidPlayerName(playerName)) {
                    playerNames[i] = playerName.isEmpty() ? "Player " + (i + 1) : playerName;
                    break; // Valid name, exit the loop
                } else {
                    error = errorMessage.genericErrorMessage(Constants.INVALID_PLAYER_NAME_MSG);
                }
                if (!inputHandler.hasNextLine()) {
                    return error;
                }
            }
        }
        return "";
    }

    public void InitGame(int columns, int rows, String[] playerNames) {
        // Initialize the board with the specified size
        board = new Board(columns, rows);
        // Initialize players based on the provided names
        players = new Player[playerNames.length];
        for (int i = 0; i < playerNames.length; i++) {
            players[i] = new Player(playerNames[i]);
        }

        // Set the current player to the first player
        currentPlayer = 0;
        out.println("Game initialized successfully!");
        out.println("Board size: " + columns + "x" + rows);
        out.println("Players: " + String.join(", ", playerNames));
    }

    public String start() {
        while (gameRunning && inputHandler.hasNextLine()) {
            board.drawBoard();
            Player player = players[currentPlayer];
            out.println(player.getName() + "'s turn!");
            out.print("Enter line type (h for horizontal, v for vertical): ");
            char lineType = inputHandler.getUserInput("string").charAt(0);
            out.println("");
            out.print("Enter row: ");
            int row = Integer.parseInt(inputHandler.getUserInput("int"));
            out.println("");
            out.print("Enter column: ");
            int col = Integer.parseInt(inputHandler.getUserInput("int"));
            out.println("");


            boolean validMove = false;
            if (lineType == 'h') {
                validMove = board.addHorizontalLine(row, col);
            } else if (lineType == 'v') {
                validMove = board.addVerticalLine(row, col);
            } else {
                errorMessage.genericErrorMessage(Constants.INVALID_LINETYPE_MSG);
                validMove = false;
                sleep(100);
            }

            if (validMove) {
                int completedBoxes = board.checkBoxes(player);
                player.addScore(completedBoxes);

                if (completedBoxes == 0) {
                    currentPlayer = (currentPlayer + 1) % players.length;
                }
            } else {
                sleep(100);
            }

            if (isGameOver()) {
                gameRunning = false;
                board.drawBoard();
                return player.announceWinner(players);
            }
        }
        return "";
    }

    private boolean isGameOver() {
        for (char[] row : board.getBoxes()) {
            for (char box : row) {
                if (box == ' ') return false;
            }
        }
        return true;
    }

    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Preserve interrupt status
        }
    }
}
