package Deck;

import Cards.*;
import Cards.SpecialMinions.Disciple;
import Cards.SpecialMinions.Miraj;
import Cards.SpecialMinions.TheCursedOne;
import Cards.SpecialMinions.TheRipper;
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
            Minion newCard = null;
            switch (card.getName()) {
                case "Sentinel":
                case "Berserker":
                    newCard = new Minion(card, playerIdx);
                break;
                case "Goliath":
                case "Warden":
                    newCard = new Tank(card, playerIdx);
                break;
                case "Miraj": newCard = new Miraj(card, playerIdx);
                break;
                case "The Cursed One": newCard = new TheCursedOne(card, playerIdx);
                break;
                case "Disciple": newCard = new Disciple(card, playerIdx);
                break;
                case "The Ripper": newCard = new TheRipper(card, playerIdx);
                break;
            };
            this.cards.add(newCard);
        }
        this.size = this.cards.size();
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
