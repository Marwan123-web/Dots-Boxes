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
    public void GetGameInfo() {
        Scanner scanner = new Scanner(System.in);
        int columns = 0;
        int rows = 0;

        // Validate and get board size
        while (true) {
            out.println("Enter Board Size (Allowed sizes: 3x2, 5x4, 8x6, 11x9): ");
            String boardSize = scanner.nextLine();
            String[] sizes = boardSize.split("x");

            if (sizes.length == 2) {
                try {
                    int currentColumns = Integer.parseInt(sizes[0].trim());
                    int currentRows = Integer.parseInt(sizes[1].trim());

                    // Check if the board size is valid or not
                    boolean isValid = validPairs.stream()
                            .anyMatch(pair -> pair[0] == currentColumns && pair[1] == currentRows);

                    if (isValid) {
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

        // Validate and get number of players
        int numberOfPlayers = 0;
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


        // Get Player's Names
        String[] playerNames = new String[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            while (true) {
                out.println("Enter Player " + (i + 1) + " Name: ");
                String playerName = scanner.nextLine().trim();

                // Check if the player name is not empty and contains at least one non-numeric character
                if (playerName.isEmpty() || !playerName.matches("^[0-9]+$")) {
                    playerNames[i] = playerName.isEmpty() ? "Player " + (i + 1) : playerName;
                    break; // Valid name, exit the loop
                } else {
                    err.println("Invalid name. Player name cannot be a number only.");
                }
            }
        }


        // Initialize game with validated inputs
        InitGame(columns, rows, playerNames);


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
        Scanner scanner = new Scanner(System.in);
        boolean gameRunning = true;

        while (gameRunning) {
            board.drawBoard();
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
            } else {
                err.println("Invalid move. Try again.");
            }

            if (isGameOver()) {
                gameRunning = false;
                board.drawBoard();
                announceWinner();
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

    private void announceWinner() {
        out.println("Game Over!");

        // Assume the first player is the winner initially
        Player winner = players[0];

        // Loop through all players to find the one with the highest score
        for (int i = 1; i < players.length; i++) {
            if (players[i].getScore() > winner.getScore()) {
                winner = players[i];
            }
        }

        // Check if there's a Draw
        boolean isDraw = false;
        for (Player player : players) {
            if (player != winner && player.getScore() == winner.getScore()) {
                isDraw = true;
                break;
            }
        }

        // Announce the result
        if (isDraw) {
            out.println("It's a Draw! Players with " + winner.getScore() + " points are:");
            for (Player player : players) {
                if (player.getScore() == winner.getScore()) {
                    out.println("- " + player.getName());
                }
            }
        } else {
            out.println(winner.getName() + " wins with " + winner.getScore() + " points!");
        }
    }

}
