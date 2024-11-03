package Game;

import Cards.Hero;
import Cards.Minion;
import Deck.DeckList;
import Deck.Deck;
import fileio.DecksInput;

public class Player {
    private int playerIdx;
    private int mana;
    private Deck hand;
    private Deck currentDeck;
    private DeckList decks;
    private Hero hero;
    private int gamesWon;
    private int gamesPlayed;

    public int getPlayerIdx() {
        return playerIdx;
    }

    public void setPlayerIdx(int playerIdx) {
        this.playerIdx = playerIdx;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public DeckList getDeckList() {
        return decks;
    }

    public void setDeckList(DeckList decks) {
        this.decks = decks;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Deck getHand() {
        return hand;
    }

    public void setHand(Deck hand) {
        this.hand = hand;
    }

    public Deck getCurrentDeck() {
        return currentDeck;
    }

    public void setCurrentDeck(Deck currentDeck) {
        this.currentDeck = currentDeck;
    }

    public DeckList getDecks() {
        return decks;
    }

    public void setDecks(DeckList decks) {
        this.decks = decks;
    }

    public Player(DecksInput decks, int playerIdx) {
        this.hand = new Deck();
        this.currentDeck = new Deck();
        this.currentDeck.setSize(decks.getNrCardsInDeck());
        this.decks = new DeckList(decks, playerIdx);
        this.mana = 1;
        this.playerIdx = playerIdx;
    }

    public void drawCard() {
        if (currentDeck.getSize() > 0) {
            Minion card = currentDeck.removeCard();
            hand.addCard(card);
        }
    }

    public int placeCard(int HandIndex, Game game) {
        Minion card = hand.getCards().get(HandIndex);
        if (mana < card.getMana()) {
            return 1;
        } else {
            int cardRow = -1;
            if (playerIdx == 1) {
                if (card.isFrontRow()) {
                    cardRow = 2;
                } else {
                    cardRow = 3;
                }
            } else if (playerIdx == 2) {
                if (card.isFrontRow()) {
                    cardRow = 1;
                } else {
                    cardRow = 0;
                }
            }
            if (game.getBoard().get(cardRow).size() >= 5) {
                return 2;
            }
            hand.getCards().remove(card);
            hand.setSize(hand.getSize() - 1);
            game.getBoard().get(cardRow).add(card);
            mana = mana - card.getMana();
        }
        return 0;
    }

    public void win() {
        gamesWon++;
        gamesPlayed++;
    }

    public void lose() {
        gamesPlayed++;
    }
}
