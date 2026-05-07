import java.util.ArrayList;

public class FreezeCard extends Card implements DealsDamage, AppliesFreeze {
    private int damage; // amount of points subtracted from the target

    public FreezeCard() {

        // Card settings
        int minDamage = 4;
        int maxDamage = 6;
        this.damage = Rand.randomInt(minDamage, maxDamage + 1);

        // Points gained from playing this card
        int minPoints = 1;
        int maxPoints = 2;
        int pointValue = Rand.randomInt(minPoints, maxPoints + 1);

        super(pointValue);
    }

    @Override
    public void play(Player currentPlayer, ArrayList<Player> allPlayers) {
        currentPlayer.addPoints(super.getPointValue());

        System.out.println(currentPlayer.getName() + " played " + this);
        System.out.println(currentPlayer.getName() + " now has " + currentPlayer.getNumPoints() + " points.");

        // choose a target player (and not the current player)
        if (allPlayers.size() < 2) {
            System.out.println("Error: No other players for the FreezeCard to freeze or damage.");
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

        freeze(currentPlayer, otherPlayer);
        doDamage(currentPlayer, otherPlayer);

    }

    @Override
    public void freeze(Player currentPlayer, Player playerToFreeze) {
        playerToFreeze.freeze();
        System.out.println(currentPlayer.getName() + " froze " + playerToFreeze.getName() + "!");
    }

    @Override
    public void doDamage(Player currentPlayer, Player playerToDamage) {
        playerToDamage.removePoints(damage);
        System.out.println(currentPlayer.getName() + " did " + damage + " damage to " + playerToDamage.getName() + ".");
        System.out.println(playerToDamage.getName() + " now has " + playerToDamage.getNumPoints() + " points.\n");
    }

    @Override
    public String toString() {
        return "Freeze Card { point value: " + super.getPointValue() + ", damage: " + damage + "}";
    }
}
