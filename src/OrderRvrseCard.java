import java.util.ArrayList;
import java.util.Collections;

public class OrderRvrseCard extends Card implements ReverseOrder{

    public OrderRvrseCard(){
        int maxPoint = 0;
        int minPoint = 0;
        int pointValue = Rand.randomInt(minPoint, maxPoint + 1);
        super(pointValue);
    }

    @Override
    public void play(Player currentPlayer, ArrayList<Player> allPlayers) {
        currentPlayer.addPoints(super.getPointValue());


        System.out.println(currentPlayer.getName() + " played " + this);
        System.out.println(currentPlayer.getName() + " now has " + currentPlayer.getNumPoints() + " points.");
    }

    @Override
    public void reverseOrder(ArrayList<Player> allPlayers){
        Collections.reverse(allPlayers);

        System.out.println("The player order has been reversed!");
        System.out.println("New Order: ");

        for (Player player : allPlayers) {
            System.out.println(player.getName() + " ");
        }
        System.out.println();
    }

    @Override
    public String toString() {
        return "Order Reverse Card { point value: " + super.getPointValue() + "}";
    }
}
