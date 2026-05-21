// Eknoor Gill

import java.util.ArrayList;

public class SwapPointsCard extends Card {

    public SwapPointsCard() {
        super(-2);
    }

    @Override
    public void play(Player currentPlayer, ArrayList<Player> allPlayers) {
        currentPlayer.addPoints(super.getPointValue());

        System.out.println(currentPlayer.getName() + " played " + this);
        System.out.println(currentPlayer.getName() + " now has " + currentPlayer.getNumPoints() + " points.");

        if (allPlayers.size() < 2) {
            System.out.println("Error: No other players for the SwapPointsCard to swap with.");
            return;
        }

        boolean selectedAnotherPlayer = false;
        Player otherPlayer = null;

        while (!selectedAnotherPlayer) {
            int randomPlayerIndex = Rand.randomInt(0, allPlayers.size());
            otherPlayer = allPlayers.get(randomPlayerIndex);
            if (otherPlayer != currentPlayer) {
                selectedAnotherPlayer = true;
            }
        }

        int currentPlayerPoints = currentPlayer.getNumPoints();
        int otherPlayerPoints = otherPlayer.getNumPoints();

        currentPlayer.removePoints(currentPlayerPoints);
        currentPlayer.addPoints(otherPlayerPoints);

        otherPlayer.removePoints(otherPlayerPoints);
        otherPlayer.addPoints(currentPlayerPoints);

        System.out.println(currentPlayer.getName() + " swapped points with " + otherPlayer.getName() + "!");
        System.out.println(currentPlayer.getName() + " now has " + currentPlayer.getNumPoints() + " points.");
        System.out.println(otherPlayer.getName() + " now has " + otherPlayer.getNumPoints() + " points.");
        System.out.println();
    }

    @Override
    public String toString() {
        return "Swap Points Card { cost: 2 points }";
    }
}