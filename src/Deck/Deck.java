package Deck;

import Cards.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;

import java.util.ArrayList;

public class Deck {
    private ArrayList<Minion> cards;
    private int size;

    public ArrayList<Minion> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Minion> cards) {
        this.cards = cards;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Deck() {
        cards = new ArrayList<>();
    }

    public Deck(ArrayList<CardInput> cards, int playerIdx) {
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

    public Deck(DeckList decks, int index) {
        this.cards = new ArrayList<>();
        this.cards.addAll(decks.getDecks().get(index).getCards());
        this.size = decks.getDecks().get(index).getCards().size();
    }

    public Minion removeCard() {
        Minion card = cards.get(0);
        cards.remove(card);
        size--;
        return card;
    }

    public void addCard(Minion card) {
        cards.add(card);
        size++;
    }

    public void printDeckJSON(ObjectMapper objectMapper, ObjectNode objectNode) {
        ArrayNode deck = objectMapper.createArrayNode();
        for (Card card: this.getCards()) {
            ObjectNode deckNode = objectMapper.createObjectNode();
            deck.add(card.printCardJSON(deckNode, objectMapper));
        }
        objectNode.set("output", deck);
    }
}
