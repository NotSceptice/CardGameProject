//Arman
import java.util.ArrayList;

public class HealthCard extends Card implements HealsHealth {

    private int healingAmount;

    public HealthCard() {

        // Healing settings
        int minHealing = 4;
        int maxHealing = 7;

        // Points gained from playing this card
        int minPoints = 1;
        int maxPoints = 3;

        int pointValue = Rand.randomInt(minPoints, maxPoints + 1);

        super(pointValue);

        healingAmount = Rand.randomInt(minHealing, maxHealing + 1);
    }

    @Override
    public void play(Player currentPlayer, ArrayList<Player> allPlayers) {

        currentPlayer.addPoints(super.getPointValue());

        System.out.println(currentPlayer.getName() + " played " + this);

        heal(currentPlayer);

        System.out.println(currentPlayer.getName() +
                " now has " +
                currentPlayer.getNumPoints() +
                " points.");
    }

    @Override
    public void heal(Player playerToHeal) {

        playerToHeal.healPoints(healingAmount);

        System.out.println(playerToHeal.getName() +
                " healed " +
                healingAmount +
                " points!");
    }

    @Override
    public String toString() {
        return "Health Card { point value: " +
                super.getPointValue() +
                ", healing: " +
                healingAmount +
                "}";
    }
}