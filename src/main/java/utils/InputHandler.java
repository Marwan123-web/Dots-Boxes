package utils;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class InputHandler {
    private Scanner scanner;
    private boolean isSystemIn;
    private Queue<String> inputQueue;

    // Constructor to allow dependency injection
    public InputHandler(InputStream inputStream) {
        this.scanner = new Scanner(inputStream);
        this.isSystemIn = (inputStream == System.in);
        this.inputQueue = new LinkedList<>();
    }

    public boolean hasNextLine() {
        return !inputQueue.isEmpty() || isSystemIn || scanner.hasNextLine();
    }

    public void closeScanner() {
        scanner.close(); // Close scanner to prevent resource leaks
    }

    public void addTestInput(String input) {

        inputQueue.add(input);
    }

    // Unified method using a switch case to handle different input types
    public String getUserInput(String type) {
        if (!inputQueue.isEmpty()) {
            return inputQueue.poll();
        }

        switch (type.toLowerCase()) {
            case "string":  // Read a full line
                return scanner.nextLine();

            case "word":  // Read a single word (next)
                return scanner.next();

            case "int":  // Read an integer safely
                int intValue = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                return String.valueOf(intValue);

            default:
                throw new IllegalArgumentException("Invalid input type: " + type);
        }
    }
}
