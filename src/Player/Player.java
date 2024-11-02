package Player;

import Cards.Card;
import Cards.Hero;
import Deck.DeckList;
import Deck.Deck;
import Game.Game;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.DecksInput;

import java.util.ArrayList;

public class Player {
    private int id;
    private int mana;
    private Deck hand;
    private Deck currentDeck;
    private DeckList decks;
    private Hero hero;
    private int gamesWon;
    private int gamesPlayed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Player(DecksInput decks, int id) {
        this.hand = new Deck();
        this.currentDeck = new Deck();
        this.currentDeck.setSize(decks.getNrCardsInDeck());
        this.decks = new DeckList(decks);
        this.mana = 1;
        this.id = id;
    }

    public void drawCard() {
        if (currentDeck.getSize() > 0) {
            Card card = currentDeck.removeCard();
            hand.addCard(card);
        }
    }

    public int placeCard(int HandIndex, Game game) {
        Card card = hand.getCards().get(HandIndex);
        if (mana < card.getMana()) {
            return 1;
        } else {
            int cardRow = -1;
            if (id == 1) {
                if (card.isTank()) {
                    cardRow = 2;
                } else {
                    cardRow = 3;
                }
            } else if (id == 2) {
                if (card.isTank()) {
                    cardRow = 1;
                } else {
                    cardRow = 0;
                }
            }
            if (game.getBoard().get(cardRow).size() >= 5) {
                return 1;
            }
            hand.getCards().remove(card);
            hand.setSize(hand.getSize() - 1);
            game.getBoard().get(cardRow).add(card);
            mana = mana - card.getMana();
        }
        return 0;
    }
}
