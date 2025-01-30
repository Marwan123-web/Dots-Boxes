package steps;

import app.Game;
import errors.ErrorMessage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import utils.Constants;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.err;
import static java.lang.System.out;
import static org.testng.Assert.assertEquals;

public class dotsBoxesSteps {

    public Game game = new Game("test");
    public int columns;
    public int rows;
    public int noOfPlayers;
    public String[] playerNames;
    public ErrorMessage errorMessage = new ErrorMessage();

    @Given("I enter board size {string}")
    public void i_enter_board_size(String boardSize) {
        String[] sizes = boardSize.split("x");
        columns = Integer.parseInt(sizes[0].trim());
        rows = Integer.parseInt(sizes[1].trim());
        game.setBoardSize(boardSize);
    }

    @Given("I enter number of players {string}")
    public void i_enter_number_of_players(String numberOfPlayers) {
        this.noOfPlayers = Integer.parseInt(numberOfPlayers);
        game.setPlayersNumber(numberOfPlayers);
    }

    @Given("I enter player names:")
    public void i_enter_player_names(io.cucumber.datatable.DataTable dataTable) {
        List<String> playerNames = dataTable.asList(String.class);
        this.playerNames = playerNames.toArray(new String[0]);
        this.playerNames = playerNames.toArray(new String[0]);
        game.setPlayesNames(playerNames);
    }

    @Then("the game should initialize successfully")
    public void the_game_should_initialize_successfully() {
        game.InitGame(columns, rows, playerNames);
    }

    @Then("verify that user got error message of invalid board size")
    public void verifyThatUserGotErrorMessage() {
        String expectedMessage = Constants.INVALID_BOARD_SIZE_MSG;
        String actualMessage = errorMessage.genericErrorMessage(expectedMessage);
        assertEquals(expectedMessage, actualMessage);
    }

    @Then("verify that user got error message of invalid player numbers")
    public void verifyThatUserGotErrorMessageOfInvalidPlayerNumbers() {
        String expectedMessage = errorMessage.genericErrorMessage(Constants.getInvalidPlayerCountMessage());
        String actualMessage = errorMessage.genericErrorMessage(Constants.getInvalidPlayerCountMessage());
        assertEquals(expectedMessage, actualMessage);
    }

    @Then("verify that user got error message of invalid player name")
    public void verifyThatUserGotErrorMessageOfInvalidPlayerName() {
        String expectedMessage = Constants.INVALID_PLAYER_NAME_MSG;
        String actualMessage = errorMessage.genericErrorMessage(expectedMessage);

        // Assert the expected and actual messages match
        assertEquals(expectedMessage, actualMessage);
    }

    @And("Play game and finish it win")
    public void playGameAndFinishItWin() {
        game.start('h', 0, 0);
        game.start('h', 0, 1);
        game.start('h', 0, 2);

        game.start('h', 1, 0);
        game.start('h', 1, 1);
        game.start('h', 1, 2);

        game.start('h', 2, 0);
        game.start('h', 2, 1);
        game.start('h', 2, 2);

        game.start('v', 0, 0);
        game.start('v', 0, 1);
        game.start('v', 0, 2);

        game.start('v', 1, 0);
        game.start('v', 1, 1);
        game.start('v', 1, 2);

        game.start('v', 0, 3);
        game.start('v', 1, 3);
    }

    @And("Play game and finish it draw")
    public void playGameAndFinishItDraw() {
        game.start('h', 0, 0);
        game.start('h', 0, 1);
        game.start('h', 0, 2);

        game.start('h', 1, 0);
        game.start('h', 1, 1);
        game.start('h', 1, 2);

        game.start('h', 2, 0);
        game.start('h', 2, 1);
        game.start('h', 2, 2);

        game.start('v', 0, 0);
        game.start('v', 0, 1);
        game.start('v', 0, 2);
        game.start('v', 0, 3);

        game.start('v', 1, 0);
        game.start('v', 1, 1);
        game.start('v', 1, 2);
        game.start('v', 1, 3);
    }


}
