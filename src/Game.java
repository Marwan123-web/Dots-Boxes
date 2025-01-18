import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.err;
import static java.lang.System.out;

public class Game {
    private Board board;
    private Player[] players;
    private int currentPlayer;
    // Define valid column-row pairs
    private List<int[]> validPairs = Arrays.asList(
            new int[]{3, 2},
            new int[]{5, 4},
            new int[]{8, 6},
            new int[]{11, 9}
    );
    private final Scanner scanner = new Scanner(System.in);
    private int columns;
    private int rows;
    private int numberOfPlayers;
    private String[] playerNames;

    public void initGameInfo() {
        setBoardSize();
        setPlayersNumber();
        setPlayesNames();

        InitGame(columns, rows, playerNames);
    }
    
    public void setBoardSize() {
        // Validate and get board size
        while (true) {
            out.println("Enter Board Size (Allowed sizes: 3x2, 5x4, 8x6, 11x9): ");
            String boardSize = scanner.nextLine();
            String[] sizes = boardSize.split("x");

            if (sizes.length == 2) {
                try {
                    int currentColumns = Integer.parseInt(sizes[0].trim());
                    int currentRows = Integer.parseInt(sizes[1].trim());


                    if (isValidBoardSize(currentColumns, currentRows)) {
                        columns = currentColumns;
                        rows = currentRows;
                        break; // Valid board size, exit loop
                    } else {
                        err.println("Invalid board size! Allowed sizes are: 3x2, 5x4, 8x6, 11x9.");
                    }
                } catch (NumberFormatException e) {
                    err.println("Invalid input! Please enter numbers in the format '3x2'.");
                }
            } else {
                err.println("Invalid input format! Please enter in the format '3x2, 5x4, 8x6, 11x9'.");
            }
        }

    }

    // Check if the board size is valid or not
    private boolean isValidBoardSize(int columns, int rows) {
        return validPairs.stream().anyMatch(pair -> pair[0] == columns && pair[1] == rows);
    }

    public void setPlayersNumber() {
        // Validate and get number of players
        while (true) {
            out.println("Enter Number of Players (1-4): ");
            String input = scanner.nextLine();

            try {
                numberOfPlayers = Integer.parseInt(input);

                if (numberOfPlayers >= 1 && numberOfPlayers <= 4) {
                    break; // Valid number of players, exit the loop
                } else {
                    err.println("Invalid number of players! Must be between 1 and 4.");
                }
            } catch (NumberFormatException e) {
                err.println("Invalid input. Please enter a valid integer.");
            }
        }

    }

    public void setPlayesNames() {
        // Get Player's Names
        playerNames = new String[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            while (true) {
                out.println("Enter Player " + (i + 1) + " Name: ");
                String playerName = scanner.nextLine().trim();

                if (isValidPlayerName(playerName)) {
                    playerNames[i] = playerName.isEmpty() ? "Player " + (i + 1) : playerName;
                    break; // Valid name, exit the loop
                } else {
                    err.println("Invalid name. Player name cannot be a number only.");
                }
            }
        }

    }

    // Check if the player name is not empty and contains at least one non-numeric character
    private boolean isValidPlayerName(String playerName) {
        return !playerName.isEmpty() && !playerName.matches("^[0-9]+$");
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
        start();
    }

    public void start() {
        boolean gameRunning = true;
        boolean lastMoveInvalid = false; // Track whether the last move was invalid

        while (gameRunning) {
            if (!lastMoveInvalid) {
                board.drawBoard(); // Only draw the board if the last move was valid
            }

            Player player = players[currentPlayer];
            out.println(player.getName() + "'s turn!");

            out.print("Enter line type (h for horizontal, v for vertical): ");
            char lineType = scanner.next().charAt(0);
            out.print("Enter row: ");
            int row = scanner.nextInt();
            out.print("Enter column: ");
            int col = scanner.nextInt();

            boolean validMove = false;
            if (lineType == 'h') {
                validMove = board.addHorizontalLine(row, col);
            } else if (lineType == 'v') {
                validMove = board.addVerticalLine(row, col);
            }

            if (validMove) {
                int completedBoxes = board.checkBoxes(player);
                player.addScore(completedBoxes);

                if (completedBoxes == 0) {
                    currentPlayer = (currentPlayer + 1) % players.length;
                }
                lastMoveInvalid = false;
            } else {
                err.println("Invalid move. Try again.");
                lastMoveInvalid = true; // Mark the move as invalid

            }

            if (isGameOver()) {
                gameRunning = false;
                board.drawBoard();
                player.announceWinner(players);
            }
        }
        scanner.close();
    }

    private boolean isGameOver() {
        for (char[] row : board.getBoxes()) {
            for (char box : row) {
                if (box == ' ') return false;
            }
        }
        return true;
    }


}
