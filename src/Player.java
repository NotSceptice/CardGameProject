import java.util.ArrayList;

public class Player {
    private String name;
    private ArrayList<Card> hand;
    private int numPoints;
    private boolean isFrozen;
    private boolean isDefended; // Added to track defense shield

    public Player(String name) {
        this.name = name;
        hand = new ArrayList<Card>();
        numPoints = 5;
        isFrozen = false;
        isDefended = false; // Initialized to false
    }

    public void playRandomCardFromHand(ArrayList<Player> players) {
        // select a random card from our hand to play
        int randomCardIndex = Rand.randomInt(0, hand.size());
        Card randomCard = hand.remove(randomCardIndex);
        randomCard.play(this, players);

        // pick a random player (but not oneself) to apply any additional actions to
        boolean selectedAnotherPlayer = false;
        Player otherPlayer = null;

        while (!selectedAnotherPlayer) {
            int randomPlayerIndex = Rand.randomInt(0, players.size());
            otherPlayer = players.get(randomPlayerIndex);
            if (otherPlayer != this) {
                selectedAnotherPlayer = true;
            }
        }

        // do possible additional damage action
        if (randomCard instanceof DealsDamage) {
            DealsDamage damageCard = (DealsDamage)randomCard;
            damageCard.doDamage(this, otherPlayer);
        }

        // do possible additional freeze action
        if (randomCard instanceof AppliesFreeze) {
            AppliesFreeze freezeCard = (AppliesFreeze)randomCard;
            freezeCard.freeze(this, otherPlayer);
        }

        // Added: do possible additional defense action
        if (randomCard instanceof ReducesDamage) {
            ReducesDamage defenseCard = (ReducesDamage)randomCard;
            defenseCard.defend(this);
        }
    }

    public boolean hasCardsInHand() {
        return hand.size() > 0;
    }

    public void addCardToHand(Card card) {
        hand.add(card);
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public void freeze() {
        isFrozen = true;
    }

    public void unfreeze() {
        isFrozen = false;
    }

    // Added: Check if the player is currently defended
    public boolean isDefended() {
        return isDefended;
    }

    // Added: Activated by the DefendCard to shield the player
    public void defend() {
        isDefended = true;
    }

    // Added: Removes the shield (useful after damage is mitigated or turn ends)
    public void removeDefenseShield() {
        isDefended = false;
    }

    public Card removeRandomCard() {
        if (hand.size() == 0) {
            return null; // returning null indicates there are no cards to remove
        }

        int randomCardIndex = Rand.randomInt(0, hand.size());
        return hand.remove(randomCardIndex); // ArrayList.remove both removes AND returns a reference to the object
    }

    public String getName() {
        return name;
    }

    public void addPoints(int pointsToAdd) {
        // If adding negative points (taking damage) and player is defended, cut it in half
        if (pointsToAdd < 0 && isDefended) {
            pointsToAdd = pointsToAdd / 2;
            System.out.println(" --> " + name + "'s shield absorbed 50% of the damage!");
            isDefended = false; // Shield breaks after mitigating damage once
        }

        numPoints += pointsToAdd;
        if (numPoints < 0) {
            numPoints = 0;
        }
    }

    public void removePoints(int pointsToRemove) {
        addPoints(-pointsToRemove);
    }

    public void healPoints(int pointsToHeal) {
        numPoints += pointsToHeal;
    }

    public int getNumPoints() {
        return numPoints;
    }

    public void displayStatus() {
        System.out.println(" | ----- " + name + " ----- ");
        System.out.println(" | Points: " + numPoints);
        if (isFrozen) {
            System.out.println(" | *FROZEN*");
        }
        if (isDefended) {
            System.out.println(" | *DEFENDED (50% Damage Reduction)*"); // Added to display status
        }
        System.out.println(" | Cards in hand:");
        for (int i = 0; i < hand.size(); i++) {
            System.out.print(" | " + (i+1) + ": ");
            System.out.println(hand.get(i));
        }
        System.out.println(" | ----- ----- ----- ");
    }
}