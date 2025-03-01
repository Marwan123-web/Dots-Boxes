# Dots and Boxes Game 

This project is designed for both **gameplay** and **automated testing** of a game initialization system using **TestNG** and **Cucumber**. The game setup involves configuring parameters like board size, number of players, and player names. Automated tests are implemented to validate both the happy path (valid inputs) and various negative scenarios (invalid inputs).

## Features

- **Game Play:** Allows users to play the Dots and Boxes game with customizable board sizes and player configurations.
- **Automated Game Setup:** Verifies the ability to initialize the game with specific board sizes and player counts.
- **Player Input Validation:** Ensures player names are correctly handled, with validation for missing or invalid names.
- **Comprehensive Input Validation:** Automates validation of inputs like board size, number of players, and player names. Invalid data triggers appropriate error messages.

## Testing Approach

- **TestNG:** The test execution framework used to run the scenarios, ensuring proper test management and reporting.
- **Step Definitions:** Step classes define the automation logic for each scenario, providing reusable and maintainable test steps.
- **Test Runner:** A TestNG-based test runner is used to execute the Cucumber scenarios and generate detailed test reports.
- **Scenario Context Class:** Handles error messages from input validation. The `ScenarioContext` class stores and manages error messages displayed during negative scenarios, ensuring proper validation and user feedback.

## Scenarios Covered

- **Happy Path:** Validates successful game initialization with correct data (board size, players, names).
- **Negative Scenarios:** Automates tests for invalid inputs like incorrect board size, invalid player counts, and empty player names. These scenarios check if proper error messages are triggered, managed via the `ScenarioContext` class.

## Technologies Used

- **TestNG:** Test execution and reporting framework
- **Cucumber:** BDD (Behavior-Driven Development) for scenario definition
- **Step Classes:** Define automation steps for each test scenario
- **Gherkin:** Human-readable format for defining test scenarios
- **Maven:** Dependency management and build tool

## Running the Application

### Prerequisites

- **Java (JDK 8 or above)**
- **Maven**

### Running the Game

To run the Dots and Boxes game:

```bash
mvn exec:java
```

### Running Automated Tests

To execute the automated test cases:

```bash
mvn clean test
```

This command will run all the TestNG and Cucumber scenarios and generate test reports.

### Generating Test Reports

After running tests, reports can be found in the `target` directory:

```bash
/target/cucumber-reports
```

### Clean and Build the Project

```bash
mvn clean install
```

This will clean the previous builds, compile the code, run tests, and package the application.

