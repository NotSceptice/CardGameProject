import java.util.ArrayList;

public class AttackCard extends Card implements DealsDamage {

    private int attackDamage;

    public AttackCard() {

        // Attack card settings
        int minAttackDamage = 5;
        int maxAttackDamage = 8;
        this.attackDamage = Rand.randomInt(minAttackDamage, maxAttackDamage + 1);

        // Points gained from playing this card
        int minPoints = 3;
        int maxPoints = 4;
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
            System.out.println("Error: No other players for the AttackCard to damage.");
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

        doDamage(currentPlayer, otherPlayer);
    }

    @Override
    public void doDamage(Player currentPlayer, Player playerToDamage) {
        playerToDamage.removePoints(attackDamage);
        System.out.println("\n" + currentPlayer.getName() + " did " + attackDamage + " damage to " + playerToDamage.getName() + ".");
        System.out.println(playerToDamage.getName() + " now has " + playerToDamage.getNumPoints() + " points.\n");
    }

    @Override
    public String toString() {
        return "Attack Card { point value: " + super.getPointValue() + ", damage: " + attackDamage + "}";
    }
}
