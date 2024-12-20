package deck;

import fileio.CardInput;
import fileio.DecksInput;

import java.util.ArrayList;

public final class DeckList {
    private int nrCardsInDeck;
    private int nrDecks;
    private ArrayList<Deck> decks;

    public int getNrCardsInDeck() {
        return nrCardsInDeck;
    }

    public void setNrCardsInDeck(final int nrCardsInDeck) {
        this.nrCardsInDeck = nrCardsInDeck;
    }

    public int getNrDecks() {
        return nrDecks;
    }

    public void setNrDecks(final int nrDecks) {
        this.nrDecks = nrDecks;
    }

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    public void setDecks(final ArrayList<Deck> decks) {
        this.decks = decks;
    }

    public DeckList(final DecksInput decksInput, final int playerIdx) {
        this.nrCardsInDeck = decksInput.getNrCardsInDeck();
        this.nrDecks = decksInput.getNrDecks();
        this.decks = new ArrayList<Deck>();
        for (ArrayList<CardInput> deck : decksInput.getDecks()) {
            Deck newDeck = new Deck(deck, playerIdx);
            this.decks.add(newDeck);
        }
    }
}
