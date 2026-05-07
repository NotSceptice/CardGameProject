import java.util.ArrayList;

public abstract class Card {

    private int pointValue;

    public Card(int pointValue) {
        this.pointValue = pointValue;
    }

    public int getPointValue() {
        return pointValue;
    }

    public abstract void play(Player currentPlayer, ArrayList<Player> allPlayers);
}
