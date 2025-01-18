import static java.lang.System.out;

public class Player {
    private final String name;
    private int score;

    public Player(String name) {
        this.name = name;
        this.score = 0;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        this.score += points;
    }

    public void announceWinner(Player[] players) {
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
