// Eknoor Gill

import java.util.ArrayList;

public class DefendCard extends Card implements ReducesDamage {

    public DefendCard() {
        super(0);
    }

    @Override
    public void play(Player currentPlayer, ArrayList<Player> allPlayers) {
        System.out.println(currentPlayer.getName() + " played " + this);
        defend(currentPlayer);
    }

    @Override
    public void defend(Player currentPlayer) {
        currentPlayer.defend();
        System.out.println(currentPlayer.getName() + " is now defended! Incoming damage will be reduced by 50%.");
        System.out.println();
    }

    @Override
    public String toString() {
        return "Defend Card { reduces incoming damage by 50% }";
    }
}