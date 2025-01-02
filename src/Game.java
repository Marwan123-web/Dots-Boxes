import java.util.Scanner;

public class Game {
    private Board board;
    private Player[] players;
    private int currentPlayer;

    public void GetGameInfo() {
        Scanner scanner = new Scanner(System.in);
        int columns = 0, rows = 0;

        // Validate and get board size
        while (true) {
            System.out.println("Enter Board Size (Allowed sizes: 3x2, 5x4, 8x6, 11x9): ");
            String boardSize = scanner.nextLine();
            String[] sizes = boardSize.split("x");

            if (sizes.length == 2) {
                try {
                    columns = Integer.parseInt(sizes[0].trim());
                    rows = Integer.parseInt(sizes[1].trim());

                    if ((columns == 3 && rows == 2) ||
                            (columns == 5 && rows == 4) ||
                            (columns == 8 && rows == 6) ||
                            (columns == 11 && rows == 9)) {
                        break; // Valid board size, exit loop
                    } else {
                        System.out.println("Invalid board size! Allowed sizes are: 3x2, 5x4, 8x6, 11x9.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input! Please enter numbers in the format '3x2'.");
                }
            } else {
                System.out.println("Invalid input format! Please enter in the format '3x2, 5x4, 8x6, 11x9'.");
            }
        }

        // Validate and get number of players
        int numberOfPlayers = 0;
        while (true) {
            System.out.println("Enter Number of Players (1-4): ");

            if (scanner.hasNextInt()) { // Check if the input is an integer
                numberOfPlayers = scanner.nextInt();
                scanner.nextLine(); // Consume the newline

                if (numberOfPlayers >= 1 && numberOfPlayers <= 4) {
                    break; // Valid number of players, exit loop
                } else {
                    System.out.println("Invalid number of players! Must be between 1 and 4.");
                }
            } else {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.nextLine(); // Clear invalid input
            }
        }

        // Get player names
        String[] playerNames = new String[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            while (true) {
                System.out.println("Enter Player " + (i + 1) + " Name: ");
                String playerName = scanner.nextLine().trim();

                // Check if the player name is not empty and contains at least one non-numeric character
                if (playerName.isEmpty() || !playerName.matches("^[0-9]+$")) {
                    playerNames[i] = playerName.isEmpty() ? "Player " + (i + 1) : playerName;
                    break; // Valid name, exit the loop
                } else {
                    System.out.println("Invalid name. Player name cannot be a number only.");
                }
            }
        }


        // Initialize game with validated inputs
        InitGame(columns, rows, playerNames);

        System.out.println("Game initialized successfully!");
        System.out.println("Board size: " + columns + "x" + rows);
        System.out.println("Players: " + String.join(", ", playerNames));
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

        System.out.println("Game initialized with a " + columns + "x" + rows + " board and " + players.length + " players.");
        start();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        boolean gameRunning = true;

        while (gameRunning) {
            board.drawBoard();
            Player player = players[currentPlayer];
            System.out.println(player.getName() + "'s turn!");

            System.out.print("Enter line type (h for horizontal, v for vertical): ");
            char lineType = scanner.next().charAt(0);
            System.out.print("Enter row: ");
            int row = scanner.nextInt();
            System.out.print("Enter column: ");
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
                System.out.println("Invalid move. Try again.");
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
        System.out.println("Game Over!");

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
            System.out.println("It's a Draw! Players with " + winner.getScore() + " points are:");
            for (Player player : players) {
                if (player.getScore() == winner.getScore()) {
                    System.out.println("- " + player.getName());
                }
            }
        } else {
            System.out.println(winner.getName() + " wins with " + winner.getScore() + " points!");
        }
    }

}
