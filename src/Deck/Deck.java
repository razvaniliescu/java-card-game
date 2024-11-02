package Deck;

import Cards.Card;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;

import java.util.ArrayList;

public class Deck {
    private ArrayList<Card> cards;
    private int size;

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
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

    public Deck(ArrayList<CardInput> cards) {
        this.cards = new ArrayList<>();
        for (CardInput card : cards) {
            Card newCard = new Card(card);
            this.cards.add(newCard);
        }
        this.size = this.cards.size();
    }

    public Deck(DeckList decks, int index) {
        this.cards = new ArrayList<>();
        this.cards.addAll(decks.getDecks().get(index).getCards());
        this.size = decks.getDecks().get(index).getCards().size();
    }

    public Card removeCard() {
        Card card = cards.get(0);
        cards.remove(card);
        size--;
        return card;
    }

    public void addCard(Card card) {
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
