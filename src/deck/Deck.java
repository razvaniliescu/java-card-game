package deck;

import cards.*;
import cards.specialMinions.Disciple;
import cards.specialMinions.Miraj;
import cards.specialMinions.TheCursedOne;
import cards.specialMinions.TheRipper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;

import java.util.ArrayList;

/**
 * Contains information about a single deck
 */
public class Deck {
    private ArrayList<Minion> cards;
    private int size;

    /**
     * Gets the current cards in the deck
     */
    public ArrayList<Minion> getCards() {
        return cards;
    }

    /**
     * Sets the current cards in the deck
     */
    public void setCards(final ArrayList<Minion> cards) {
        this.cards = cards;
    }

    /**
     * Gets the current number of cards in the deck
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets the current number of cards in the deck
     */
    public void setSize(final int size) {
        this.size = size;
    }

    public Deck() {
        cards = new ArrayList<>();
    }

    public Deck(final ArrayList<CardInput> cards, final int playerIdx) {
        this.cards = new ArrayList<>();
        for (CardInput card : cards) {
            Minion newCard = switch (card.getName()) {
                case "Sentinel", "Berserker" -> new Minion(card, playerIdx);
                case "Goliath", "Warden" -> new Tank(card, playerIdx);
                case "Miraj" -> new Miraj(card, playerIdx);
                case "The Cursed One" -> new TheCursedOne(card, playerIdx);
                case "Disciple" -> new Disciple(card, playerIdx);
                case "The Ripper" -> new TheRipper(card, playerIdx);
                default -> null;
            };
            this.cards.add(newCard);
        }
        this.size = this.cards.size();
    }

    /**
     * Removes the first card in a deck
     */
    public Minion removeCard() {
        Minion card = cards.getFirst();
        cards.remove(card);
        size--;
        return card;
    }

    /**
     * Adds a card in the deck
     */
    public void addCard(final Minion card) {
        cards.add(card);
        size++;
    }

    /**
     * Prints the all the cards in a deck in JSON format
     */
    public void printDeckJSON(final ObjectMapper objectMapper, final ObjectNode objectNode) {
        ArrayNode deck = objectMapper.createArrayNode();
        for (Card card: this.getCards()) {
            ObjectNode deckNode = objectMapper.createObjectNode();
            deck.add(card.printCardJSON(deckNode, objectMapper));
        }
        objectNode.set("output", deck);
    }
}
