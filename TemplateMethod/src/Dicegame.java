import java.util.Random;

public class Dicegame extends Game {
    private int[] scores; // Array to hold players' scores
    private int targetScore; // Score needed to win
    private int currentPlayer; // Current player index
    private static final Random random = new Random(); // Random number generator

    // Constructor to set the target score for winning
    public Dicegame(int targetScore) {
        this.targetScore = targetScore;
    }

    @Override
    public void initializeGame(int numberOfPlayers) {
        scores = new int[numberOfPlayers]; // Initialize scores
        currentPlayer = 0; // Start with the first player
        System.out.println("Game Initialized: " + numberOfPlayers + " players.");
    }

    @Override
    public boolean endOfGame() {
        // Check if any player has reached the target score
        for (int score : scores) {
            if (score >= targetScore) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void playSingleTurn(int player) {
        // Each player rolls a die (1 to 6)
        int roll = random.nextInt(6) + 1;
        scores[player] += roll; // Add roll to the player's score
        System.out.println("Player " + (player + 1) + " rolled a " + roll + ". Total score: " + scores[player]);
    }

    @Override
    public void displayWinner() {
        // Find and display the winner
        int winnerIndex = -1;
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] >= targetScore) {
                winnerIndex = i;
                break;
            }
        }
        if (winnerIndex != -1) {
            System.out.println("Player " + (winnerIndex + 1) + " wins with a score of " + scores[winnerIndex] + "!");
        } else {
            System.out.println("No winner yet.");
        }
    }
}
