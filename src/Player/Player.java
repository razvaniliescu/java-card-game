package Player;

import Cards.Card;
import Cards.Hero;
import Deck.Decks;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;
import fileio.DecksInput;
import fileio.StartGameInput;

import java.util.ArrayList;

public class Player {
    private ArrayList<Card> hand;
    private Decks decks;
    private Hero hero;
    private ArrayList<Card> currentDeck;
    private int gamesWon;
    private int gamesPlayed;

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

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public Decks getDecks() {
        return decks;
    }

    public void setDecks(Decks decks) {
        this.decks = decks;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public ArrayList<Card> getCurrentDeck() {
        return currentDeck;
    }

    public void setCurrentDeck(ArrayList<Card> currentDeck) {
        this.currentDeck = currentDeck;
    }

    public Player(DecksInput decks) {
        this.hand = new ArrayList<Card>();
        this.decks = new Decks(decks);
    }

    public void drawCard() {
        Card card = this.currentDeck.get(0);
        this.currentDeck.remove(0);
        this.hand.add(card);
    }

    public void printCurrentJSON(ObjectMapper objectMapper, ObjectNode objectNode, ArrayList<Card> currentDeckOrHand) {
        ArrayNode deck = objectMapper.createArrayNode();
        for (Card card: currentDeckOrHand) {
            ObjectNode deckNode = objectMapper.createObjectNode();
            deckNode.put("mana", card.getMana());
            deckNode.put("attackDamage", card.getAttackDamage());
            deckNode.put("health", card.getHealth());
            deckNode.put("description", card.getDescription());
            ArrayNode colors = objectMapper.createArrayNode();
            for (String color: card.getColors()) {
                colors.add(color);
            }
            deckNode.set("colors", colors);
            deckNode.put("name", card.getName());
            deck.add(deckNode);
        }
        objectNode.set("output", deck);
    }
}
