import java.util.Random;
import java.util.Scanner;

public class SnakeAndLadder {
    private static final int BOARD_SIZE = 100;
    private static final int[] snakes = {16, 47, 49, 56, 62, 64, 87, 93, 95, 98};
    private static final int[] snakeEnds = {6, 26, 11, 53, 19, 59, 24, 73, 75, 79};
    private static final int[] ladders = {1, 38, 3, 14, 8, 31, 28, 84, 51, 91};
    private static final int[] ladderEnds = {38, 14, 24, 39, 31, 84, 84, 98, 91, 100};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        System.out.print("Enter number of players: ");
        int playerCount = scanner.nextInt();
        int[] playerPositions = new int[playerCount];
        String[] playerNames = new String[playerCount];
        boolean[] hasStarted = new boolean[playerCount];

        // Getting player names
        for (int i = 0; i < playerCount; i++) {
            System.out.print("Enter name for player " + (i + 1) + ": ");
            playerNames[i] = scanner.next();
        }

        boolean gameWon = false;
        int currentPlayer = 0;

        while (!gameWon) {
            // Current player's turn
            System.out.println("\nCurrent Player: " + playerNames[currentPlayer]);
            System.out.println("Player Positions: ");
            for (int i = 0; i < playerCount; i++) {
                System.out.println(playerNames[i] + ": " + playerPositions[i]);
            }

            System.out.println(playerNames[currentPlayer] + ", press Enter to roll the dice or type 'f' to forfeit...");
            String input = scanner.next();

            if (input.equalsIgnoreCase("f")) {
                System.out.println(playerNames[currentPlayer] + " has forfeited the game!");
                break; // End the game if the player forfeits
            }

            int diceRoll = random.nextInt(6) + 1; // Dice roll between 1 and 6
            System.out.println(playerNames[currentPlayer] + " rolled a " + diceRoll);

            // Only start moving if the player has rolled a 6
            if (diceRoll == 6) {
                hasStarted[currentPlayer] = true;
            }

            if (hasStarted[currentPlayer]) {
                playerPositions[currentPlayer] += diceRoll;

                // Check for overshoot
                if (playerPositions[currentPlayer] > 100) {
                    playerPositions[currentPlayer] -= diceRoll; // Move back if overshoot
                    System.out.println(playerNames[currentPlayer] + " can't move, stay at " + playerPositions[currentPlayer]);
                } else {
                    // Check for snakes or ladders
                    if (isSnake(playerPositions[currentPlayer])) {
                        System.out.println(playerNames[currentPlayer] + " landed on a snake! " +
                                "Moving down from " + playerPositions[currentPlayer] + " to " + snakeEnds[getSnakeIndex(playerPositions[currentPlayer])]);
                        playerPositions[currentPlayer] = snakeEnds[getSnakeIndex(playerPositions[currentPlayer])];
                    } else if (isLadder(playerPositions[currentPlayer])) {
                        System.out.println(playerNames[currentPlayer] + " climbed a ladder! " +
                                "Moving up from " + playerPositions[currentPlayer] + " to " + ladderEnds[getLadderIndex(playerPositions[currentPlayer])]);
                        playerPositions[currentPlayer] = ladderEnds[getLadderIndex(playerPositions[currentPlayer])];
                    }
                }
            } else {
                System.out.println(playerNames[currentPlayer] + " needs to roll a 6 to start.");
            }

            displayBoard(playerPositions, playerNames);

            // Check for win
            if (playerPositions[currentPlayer] == 100) {
                System.out.println(playerNames[currentPlayer] + " wins the game!");
                gameWon = true;
            }

            // Move to the next player
            currentPlayer = (currentPlayer + 1) % playerCount;
        }

        scanner.close();
    }

    private static int getSnakeIndex(int position) {
        for (int i = 0; i < snakes.length; i++) {
            if (snakes[i] == position) {
                return i;
            }
        }
        return -1; // Not found
    }

    private static int getLadderIndex(int position) {
        for (int i = 0; i < ladders.length; i++) {
            if (ladders[i] == position) {
                return i;
            }
        }
        return -1; // Not found
    }

    private static boolean isSnake(int position) {
        for (int snake : snakes) {
            if (snake == position) {
                return true;
            }
        }
        return false;
    }

    private static boolean isLadder(int position) {
        for (int ladder : ladders) {
            if (ladder == position) {
                return true;
            }
        }
        return false;
    }

    private static void displayBoard(int[] playerPositions, String[] playerNames) {
        System.out.println("\nCurrent Board:");
        System.out.println("╔═══════════════════════════════════════════╗");

        for (int i = 10; i >= 1; i--) {
            System.out.print("║ ");
            for (int j = 1; j <= 10; j++) {
                int cell = (i - 1) * 10 + j;
                boolean isPlayer = false;
                for (int k = 0; k < playerPositions.length; k++) {
                    if (cell == playerPositions[k]) {
                        System.out.print(" " + playerNames[k].charAt(0) + "  ║ "); // Player initial
                        isPlayer = true;
                        break;
                    }
                }
                if (!isPlayer) {
                    if (isSnake(cell)) {
                        System.out.print(" S  ║ "); // Snake position
                    } else if (isLadder(cell)) {
                        System.out.print(" L  ║ "); // Ladder position
                    } else {
                        System.out.printf("%-3d ║ ", cell);
                    }
                }
            }
            System.out.println();
            System.out.println("╠═══════════════════════════════════════════╣");
        }
        System.out.println("╚═══════════════════════════════════════════╝");
        System.out.println();
    }
}
