// Eknoor Gill

import java.util.ArrayList;

public class DrawTwoCard extends Card {

    private ArrayList<Card> mixedDeck;

    public DrawTwoCard(ArrayList<Card> mixedDeck) {

        super(0);

        this.mixedDeck = mixedDeck;
    }

    @Override
    public void play(Player currentPlayer, ArrayList<Player> allPlayers) {

        System.out.println(currentPlayer.getName() + " played " + this);

        int cardsDrawn = 0;

        while (cardsDrawn < 2 && mixedDeck.size() > 0) {

            int randomCardIndex = Rand.randomInt(0, mixedDeck.size());

            Card drawnCard = mixedDeck.remove(randomCardIndex);

            currentPlayer.addCardToHand(drawnCard);

            System.out.println(currentPlayer.getName()
                    + " drew a "
                    + drawnCard
                    + " from the Mixed deck.");

            cardsDrawn++;
        }

        if (cardsDrawn == 0) {
            System.out.println("The Mixed deck is empty.");
        }

        System.out.println();
    }

    @Override
    public String toString() {
        return "Draw Two Card { draws 2 cards }";
    }
}