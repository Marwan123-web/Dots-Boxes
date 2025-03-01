package steps;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import app.Game;
import context.ScenarioContext;
import errors.ErrorMessage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import utils.Constants;


import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class dotsBoxesSteps {
    public Game game;
    public int columns;
    public int rows;
    public int noOfPlayers;
    public String[] playerNames;
    public ErrorMessage errorMessage = new ErrorMessage();
    private final ScenarioContext scenarioContext = new ScenarioContext();

    public dotsBoxesSteps() {
        this.game = new Game(System.in);
    }

    private void injectTestInput(String simulatedInput) {
        if (simulatedInput == null) {
            simulatedInput = "";
        }
        ByteArrayInputStream testInput = new ByteArrayInputStream(simulatedInput.getBytes(StandardCharsets.UTF_8));
        this.game.setInputStream(testInput);
    }

    private void injectTestInput(int simulatedInput) {
        // Convert int to String and add a newline (simulates user pressing Enter)
        String inputAsString = simulatedInput + "\n";

        // Convert String to InputStream
        ByteArrayInputStream testInput = new ByteArrayInputStream(inputAsString.getBytes(StandardCharsets.UTF_8));
        // Inject into game
        this.game.setInputStream(testInput);
    }

    // Overloaded method to handle List<String>
    private void injectTestInput(List<String> simulatedInputs) {
        String inputAsString = simulatedInputs.stream()
                .map(input -> (input == null) ? "" : input)
                .collect(Collectors.joining("\n")) + "\n";

        injectTestInput(inputAsString);
    }


    private void injectTestInput(String direction, int row, int col) {
        this.game.setArrayInputStream(direction);
        this.game.setArrayInputStream(String.valueOf(row));
        this.game.setArrayInputStream(String.valueOf(col));
    }


    @Given("I enter board size {string}")
    public void i_enter_board_size(String boardSize) {
        String[] sizes = boardSize.split("x");
        columns = Integer.parseInt(sizes[0].trim());
        rows = Integer.parseInt(sizes[1].trim());
        this.injectTestInput(boardSize);
        String actualMessage = game.setBoardSize();
        scenarioContext.set("actualMessage", actualMessage);
    }

    @Given("I enter number of players {string}")
    public void i_enter_number_of_players(String numberOfPlayers) {
        try {
            this.noOfPlayers = Integer.parseInt(numberOfPlayers);
            this.injectTestInput(numberOfPlayers);
            String actualMessage = game.setPlayersNumber();
            scenarioContext.set("actualMessage", actualMessage);
        } catch (NumberFormatException e) {
            String error = errorMessage.genericErrorMessage(Constants.INVALID_INTEGER_MSG);
            scenarioContext.set("actualMessage", error);
        }
    }

    @Given("I enter player names:")
    public void i_enter_player_names(io.cucumber.datatable.DataTable dataTable) {
        List<String> playerNamesList = dataTable.asList(String.class);
        this.playerNames = new String[playerNamesList.size()];
        List<String> testInputList = new ArrayList<>();

        for (int i = 0; i < playerNamesList.size(); i++) {
            String name = playerNamesList.get(i);
            this.playerNames[i] = name;
            testInputList.add(name);
        }

        this.injectTestInput(testInputList);
        String actualMessage = game.setPlayesNames();
        scenarioContext.set("actualMessage", actualMessage);
    }

    @Then("the game should initialize successfully")
    public void the_game_should_initialize_successfully() {
        game.InitGame(columns, rows, playerNames);
    }

    @Then("verify that user got error message of invalid board size")
    public void verifyThatUserGotErrorMessage() {
        String expectedMessage = errorMessage.genericErrorMessage(Constants.INVALID_BOARD_SIZE_MSG);
        String actualMessage = scenarioContext.get("actualMessage");
        assertEquals(expectedMessage, actualMessage);
        scenarioContext.reset("actualMessage");
    }

    @Then("verify that user got error message of invalid player numbers")
    public void verifyThatUserGotErrorMessageOfInvalidPlayerNumbers() {
        String expectedMessage = errorMessage.genericErrorMessage(Constants.getInvalidPlayerCountMessage(), false);
        String secondExpectedMessage = errorMessage.genericErrorMessage(Constants.INVALID_INTEGER_MSG, false);
        String actualMessage = scenarioContext.get("actualMessage");
//        assertEquals(expectedMessage, actualMessage);
        assertTrue(actualMessage.equals(expectedMessage) || actualMessage.equals(secondExpectedMessage));
        scenarioContext.reset("actualMessage");
    }

    @Then("verify that user got error message of invalid player name")
    public void verifyThatUserGotErrorMessageOfInvalidPlayerName() {
        String expectedMessage = errorMessage.genericErrorMessage(Constants.INVALID_PLAYER_NAME_MSG, false);
        String actualMessage = scenarioContext.get("actualMessage");

        // Assert the expected and actual messages match
        assertEquals(expectedMessage, actualMessage);
        scenarioContext.reset("actualMessage");
    }

    @And("Play game and finish it win")
    public void playGameAndFinishItWin() {

        this.injectTestInput("h", 0, 0);
        game.start();

        this.injectTestInput("h", 0, 1);
        game.start();

        this.injectTestInput("h", 0, 2);
        game.start();

        this.injectTestInput("h", 1, 0);
        game.start();

        this.injectTestInput("h", 1, 1);
        game.start();

        this.injectTestInput("h", 1, 2);
        game.start();

        this.injectTestInput("h", 2, 0);
        game.start();

        this.injectTestInput("h", 2, 1);
        game.start();

        this.injectTestInput("h", 2, 2);
        game.start();

        this.injectTestInput("v", 0, 0);
        game.start();

        this.injectTestInput("v", 0, 1);
        game.start();

        this.injectTestInput("v", 0, 2);
        game.start();

        this.injectTestInput("v", 1, 0);
        game.start();

        this.injectTestInput("v", 1, 1);
        game.start();

        this.injectTestInput("v", 1, 2);
        game.start();

        this.injectTestInput("v", 0, 3);
        game.start();

        this.injectTestInput("v", 1, 3);
        String actualMessage = game.start();

        String expectedMessage = "wins with";

        assertTrue(actualMessage.contains(expectedMessage),
                String.format("Expected message to contain: \"%s\" and got: \"%s\"", expectedMessage, actualMessage));
    }

    @And("Play game and finish it draw")
    public void playGameAndFinishItDraw() {
        this.injectTestInput("h", 0, 0);
        game.start();

        this.injectTestInput("h", 0, 1);
        game.start();

        this.injectTestInput("h", 0, 2);
        game.start();

        this.injectTestInput("h", 1, 0);
        game.start();

        this.injectTestInput("h", 1, 1);
        game.start();

        this.injectTestInput("h", 1, 2);
        game.start();

        this.injectTestInput("h", 2, 0);
        game.start();

        this.injectTestInput("h", 2, 1);
        game.start();

        this.injectTestInput("h", 2, 2);
        game.start();

        this.injectTestInput("v", 0, 0);
        game.start();

        this.injectTestInput("v", 0, 1);
        game.start();

        this.injectTestInput("v", 0, 2);
        game.start();

        this.injectTestInput("v", 0, 3);
        game.start();

        this.injectTestInput("v", 1, 0);
        game.start();

        this.injectTestInput("v", 1, 1);
        game.start();

        this.injectTestInput("v", 1, 2);
        game.start();

        this.injectTestInput("v", 1, 3);
        String actualMessage = game.start();

        String expectedMessage = "It's a Draw!";
        assertTrue(actualMessage.contains(expectedMessage),
                String.format("Expected message to contain: \"%s\" and got: \"%s\"", expectedMessage, actualMessage));
    }

}
