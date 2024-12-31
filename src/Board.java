import java.util.Arrays;

public class Board {
    private int rows;
    private int cols;
    private char[][] horizontalLines;
    private char[][] verticalLines;
    private char[][] boxes;

    public Board(int cols, int rows) {
        this.rows = rows;
        this.cols = cols;
        horizontalLines = new char[rows + 1][cols];
        verticalLines = new char[rows][cols + 1];
        boxes = new char[rows][cols];

        for (char[] row : horizontalLines) Arrays.fill(row, ' ');
        for (char[] row : verticalLines) Arrays.fill(row, ' ');
        for (char[] row : boxes) Arrays.fill(row, ' ');
    }

    public void drawBoard() {
        for (int i = 0; i < rows; i++) {
            // Draw horizontal lines
            for (int j = 0; j < cols; j++) {
                System.out.print("o");
                System.out.print(horizontalLines[i][j] == '-' ? "---" : "   ");
            }
            System.out.println("o");

            // Draw vertical lines and boxes
            for (int j = 0; j < cols; j++) {
                System.out.print(verticalLines[i][j] == '|' ? "|" : " ");
                System.out.print(" " + (boxes[i][j] == ' ' ? " " : boxes[i][j]) + " ");
            }
            System.out.println(verticalLines[i][cols] == '|' ? "|" : " ");
        }

        // Draw the last row of horizontal lines
        for (int j = 0; j < cols; j++) {
            System.out.print("o");
            System.out.print(horizontalLines[rows][j] == '-' ? "---" : "   ");
        }
        System.out.println("o");
    }

    public boolean addHorizontalLine(int row, int col) {
        if (horizontalLines[row][col] == ' ') {
            horizontalLines[row][col] = '-';
            return true;
        }
        return false;
    }

    public boolean addVerticalLine(int row, int col) {
        if (verticalLines[row][col] == ' ') {
            verticalLines[row][col] = '|';
            return true;
        }
        return false;
    }

    public int checkBoxes(Player player) {
        int completedBoxes = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (boxes[i][j] == ' ' &&
                        horizontalLines[i][j] == '-' &&
                        horizontalLines[i + 1][j] == '-' &&
                        verticalLines[i][j] == '|' &&
                        verticalLines[i][j + 1] == '|') {
                    boxes[i][j] = player.getName().charAt(0); // Mark the box as completed with first player char name
                    completedBoxes++;
                }
            }
        }
        return completedBoxes;
    }

    public char[][] getBoxes() {
        return boxes;
    }
}
