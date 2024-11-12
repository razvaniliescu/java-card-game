package game;

import cards.Hero;
import cards.Minion;
import deck.DeckList;
import deck.Deck;
import fileio.DecksInput;

/**
 * Contains information about the player
 */
public class Player {
    private int playerIdx;
    private int mana;
    private Deck hand;
    private Deck currentDeck;
    private DeckList decks;
    private Hero hero;
    private int gamesWon;
    private int gamesPlayed;

    /**
     * Gets the player's index
     */
    public int getPlayerIdx() {
        return playerIdx;
    }

    /**
     * Sets the player's index
     */
    public void setPlayerIdx(final int playerIdx) {
        this.playerIdx = playerIdx;
    }

    /**
     * Gets the player's mana
     */
    public int getMana() {
        return mana;
    }

    /**
     * Sets the player's mana
     */
    public void setMana(final int mana) {
        this.mana = mana;
    }

    /**
     * Gets the player's number of won games
     */
    public int getGamesWon() {
        return gamesWon;
    }

    /**
     * Sets the player's number of won games
     */
    public void setGamesWon(final int gamesWon) {
        this.gamesWon = gamesWon;
    }

    /**
     * Gets the player's number of played games
     */
    public int getGamesPlayed() {
        return gamesPlayed;
    }

    /**
     * Sets the player's number of played games
     */
    public void setGamesPlayed(final int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    /**
     * Gets the player's hero
     */
    public Hero getHero() {
        return hero;
    }

    /**
     * Sets the player's hero
     */
    public void setHero(final Hero hero) {
        this.hero = hero;
    }

    /**
     * Gets the player's hand
     */
    public Deck getHand() {
        return hand;
    }

    /**
     * Sets the player's hand
     */
    public void setHand(final Deck hand) {
        this.hand = hand;
    }

    /**
     * Gets the player's current deck
     */
    public Deck getCurrentDeck() {
        return currentDeck;
    }

    /**
     * Sets the player's current deck
     */
    public void setCurrentDeck(final Deck currentDeck) {
        this.currentDeck = currentDeck;
    }

    /**
     * Gets the player's deck list
     */
    public DeckList getDecks() {
        return decks;
    }

    /**
     * Sets the player's deck list
     */
    public void setDecks(final DeckList decks) {
        this.decks = decks;
    }

    public Player(final DecksInput decks, final int playerIdx) {
        this.hand = new Deck();
        this.currentDeck = new Deck();
        this.currentDeck.setSize(decks.getNrCardsInDeck());
        this.decks = new DeckList(decks, playerIdx);
        this.mana = 1;
        this.playerIdx = playerIdx;
    }

    /**
     * Draws a card out of the player's deck
     */
    public void drawCard() {
        if (currentDeck.getSize() > 0) {
            Minion card = currentDeck.removeCard();
            hand.addCard(card);
        }
    }


    /**
     * Places a card on the board
     * @param game current game details
     * @param handIndex index of the selected card in hand
     */
    public int placeCard(final int handIndex, final Game game) {
        Minion card = hand.getCards().get(handIndex);
        final int maxMinionsOnRow = 5;

        final int firstRowIndex = 0;
        final int secondRowIndex = 1;
        final int thirdRowIndex = 2;
        final int fourthRowIndex = 3;

        final int notEnoughMana = 1;
        final int tooManyMinionsOnRow = 2;

        if (mana < card.getMana()) {
            return notEnoughMana;
        } else {
            int cardRow = -1;
            if (playerIdx == 1) {
                if (card.isFrontRow()) {
                    cardRow = thirdRowIndex;
                } else {
                    cardRow = fourthRowIndex;
                }
            } else if (playerIdx == 2) {
                if (card.isFrontRow()) {
                    cardRow = secondRowIndex;
                } else {
                    cardRow = firstRowIndex;
                }
            }
            if (game.getBoard().get(cardRow).size() >= maxMinionsOnRow) {
                return tooManyMinionsOnRow;
            }
            hand.getCards().remove(card);
            hand.setSize(hand.getSize() - 1);
            game.getBoard().get(cardRow).add(card);
            mana = mana - card.getMana();
        }
        return 0;
    }

    /**
     * Modifies the stats of the winning player
     */
    public void win() {
        gamesWon++;
        gamesPlayed++;
    }

    /**
     * Modifies the stats of the losing player
     */
    public void lose() {
        gamesPlayed++;
    }
}
