import java.util.ArrayList;

public class Game {

    // ----------- Settings ----------- //

    // Player settings
    private int startingHandSize;

    private float playerChancesOfPlayingCard; // % chance (0-1) that a player plays a card from their hand
    private float playerChancesOfDrawingFromMixedDeck; // % chance (0-1) that a player draws from the mixed deck

    // Deck settings
    private int totalNumberOfCards;
    private float pointCardChances;
    private float attackCardChances;
    private float freezeCardChances;
    private float healthCardChances;
    private float orderRvrseCardChances;
    private float defendCardChances;   // ADDED: % chance to generate defend card
    private float drawTwoCardChances;  // ADDED: % chance to generate draw two card
    // thiefCardChances are the leftovers of the other choices

    private float chancesOfDamageCardBeingInDamageDeck;

    // -------- End of Settings ------- //


    // --------- Game Objects --------- //

    private ArrayList<Player> players;
    private ArrayList<Card> mixedDeck; // contains a mix of all types of cards
    private ArrayList<DealsDamage> damageDeck; // contains only cards that implement DealsDamage

    // ------ End of Game Objects ----- //


    public Game() {
        // Set game settings
        setGameSettings();

        // Game objects
        players = new ArrayList<Player>();
        mixedDeck = new ArrayList<Card>();
        damageDeck = new ArrayList<DealsDamage>();

        // Generate the decks
        generateDecks();
    }

    public void registerPlayer(Player player) {
        players.add(player);
    }

    public void run() {
        // deal cards to each player
        int cardsAdded = 0;
        while (cardsAdded < startingHandSize) {
            for (int i = 0; i < players.size(); i++) {
                if (mixedDeck.isEmpty()) break;
                int randomCardIndex = Rand.randomInt(0, mixedDeck.size());
                Card randomCard = mixedDeck.remove(randomCardIndex);
                players.get(i).addCardToHand(randomCard);
            }
            cardsAdded += 1;
        }

        int currentPlayerIndex = -1;
        Player currentPlayer;

        // game loop -- loop as long as either deck has cards
        while (mixedDeck.size() > 0 || damageDeck.size() > 0) {

            // switch to next player
            currentPlayerIndex += 1;
            if (currentPlayerIndex >= players.size()) {
                currentPlayerIndex = 0;
            }
            currentPlayer = players.get(currentPlayerIndex);

            System.out.println("\n# cards remaining in Mixed deck: " + mixedDeck.size() + ".");
            System.out.println("# cards remaining in Damage deck: " + damageDeck.size() + ".\n");

            System.out.println("It's " + currentPlayer.getName() + "'s turn.\n");
            currentPlayer.displayStatus();
            Input.waitForUserToPressEnter("\nPress Enter to play " + currentPlayer.getName() + "'s turn.");

            // check if the player should be skipped
            if (currentPlayer.isFrozen()) {
                System.out.println(currentPlayer.getName() + " is frozen! Skipping turn.");
                currentPlayer.unfreeze();
                continue;
            }

            // generate a random value to choose a random action
            float randomValue = Rand.random();

            // 1. play a card from player's hand
            if (randomValue < playerChancesOfPlayingCard && currentPlayer.hasCardsInHand()) {
                currentPlayer.playRandomCardFromHand(players);
            }

            // 2. OR draw a card from mixed deck (but don't play it yet)
            else if (damageDeck.size() == 0 || (mixedDeck.size() > 0 && randomValue < playerChancesOfPlayingCard + playerChancesOfDrawingFromMixedDeck)) {
                Object drawnObject = drawRandomCard(mixedDeck);
                Card drawnCard = (Card)drawnObject;
                currentPlayer.addCardToHand(drawnCard);

                System.out.println(currentPlayer.getName() + " drew a " + drawnCard + " from the Mixed deck.");
            }

            // 3. OR draw a card from damage deck and use its damage effect immediately, without getting points
            else {
                Object drawnObject = drawRandomCard(damageDeck);
                DealsDamage damageCard = (DealsDamage)drawnObject;

                System.out.println(currentPlayer.getName() + " drew a " + damageCard + " from the Damage deck.");

                // pick a random player (but not oneself) to apply the damage card to
                boolean selectedAnotherPlayer = false;
                Player otherPlayer = null;

                while (!selectedAnotherPlayer) {
                    int randomPlayerIndex = Rand.randomInt(0, players.size());
                    otherPlayer = players.get(randomPlayerIndex);
                    if (otherPlayer != currentPlayer) {
                        selectedAnotherPlayer = true;
                    }
                }

                damageCard.doDamage(currentPlayer, otherPlayer);
                if (damageCard instanceof AppliesFreeze) {
                    AppliesFreeze freezeCard = (AppliesFreeze)damageCard;
                    freezeCard.freeze(currentPlayer, otherPlayer);
                }
            }

            Input.waitForUserToPressEnter("\nPress Enter to end " + currentPlayer.getName() + "'s turn.\n");
        }

        // End game: determine which Player had the most points
        declareWinner();
    }

    private Object drawRandomCard(ArrayList arrayList) {
        int randomCardIndex = Rand.randomInt(0, arrayList.size());
        Object randomCard = arrayList.remove(randomCardIndex);
        return randomCard;
    }

    // Initializes the settings fields.
    private void setGameSettings() {
        // Player settings
        startingHandSize = 3;
        playerChancesOfPlayingCard = 0.5f;
        playerChancesOfDrawingFromMixedDeck = 0.25f;
        float playerChancesOfDrawingFromDamageDeck = 1f - (playerChancesOfPlayingCard + playerChancesOfDrawingFromMixedDeck);
        if (playerChancesOfDrawingFromDamageDeck < 0f) {
            System.out.println("ERROR: Chances of different player actions are not all positive.");
        }

        // Deck settings
        totalNumberOfCards = 40; // Increased to 40 so all card types have a chance to shine!
        chancesOfDamageCardBeingInDamageDeck = 0.4f;

        // Rebalanced probability allocation (Must sum to less than or equal to 1.0f)
        pointCardChances      = 0.35f;
        attackCardChances     = 0.15f;
        freezeCardChances     = 0.10f;
        healthCardChances     = 0.10f;
        orderRvrseCardChances = 0.10f;
        defendCardChances     = 0.10f; // ADDED
        drawTwoCardChances    = 0.05f; // ADDED

        // Leftover math check for ThiefCard
        float thiefCardChances = 1f - (pointCardChances + attackCardChances + freezeCardChances + healthCardChances + orderRvrseCardChances + defendCardChances + drawTwoCardChances);
        if (thiefCardChances < 0f) {
            System.out.println("ERROR: Card chances are not all positive.");
        }
    }

    // Populates the two ArrayLists with random Cards, according to the settings.
    private void generateDecks() {
        for (int i = 0; i < totalNumberOfCards; i++) {

            float randomValue = Rand.random();

            // Track standard ranges cumulatively
            float currentRange = pointCardChances;
            if (randomValue < currentRange) {
                mixedDeck.add(new PointCard());
                continue;
            }

            currentRange += attackCardChances;
            if (randomValue < currentRange) {
                AttackCard newAttackCard = new AttackCard();
                if (Rand.random() < chancesOfDamageCardBeingInDamageDeck) {
                    damageDeck.add(newAttackCard);
                } else {
                    mixedDeck.add(newAttackCard);
                }
                continue;
            }

            currentRange += freezeCardChances;
            if (randomValue < currentRange) {
                FreezeCard newFreezeCard = new FreezeCard();
                if (Rand.random() < chancesOfDamageCardBeingInDamageDeck) {
                    damageDeck.add(newFreezeCard);
                } else {
                    mixedDeck.add(newFreezeCard);
                }
                continue;
            }

            currentRange += healthCardChances;
            if (randomValue < currentRange) {
                mixedDeck.add(new HealthCard());
                continue;
            }

            currentRange += orderRvrseCardChances;
            if (randomValue < currentRange) {
                mixedDeck.add(new OrderRvrseCard());
                continue;
            }

            // ADDED: Defend Card generation
            currentRange += defendCardChances;
            if (randomValue < currentRange) {
                mixedDeck.add(new DefendCard());
                continue;
            }

            // ADDED: Draw Two Card generation (passes reference of mixedDeck)
            currentRange += drawTwoCardChances;
            if (randomValue < currentRange) {
                mixedDeck.add(new DrawTwoCard(mixedDeck));
                continue;
            }

            // Thief card handles the rest
            mixedDeck.add(new ThiefCard());
        }
    }

    private void declareWinner() {
        int highestScore = -999; // Lower bounds safe initial point
        Player playerWithHighestScore = null;

        System.out.println("\nFinal Scoreboard:");
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            System.out.println(p.getName() + ": " + p.getNumPoints());

            if (p.getNumPoints() >= highestScore) {
                highestScore = p.getNumPoints();
                playerWithHighestScore = p;
            }
        }

        if (playerWithHighestScore != null) {
            System.out.println("Player '" + playerWithHighestScore.getName() + "' wins!");
        }
    }
}