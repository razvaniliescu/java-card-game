package cards;

import fileio.CardInput;

/**
 * Minion subclass for all the cards that must be attacked first
 */
public class Tank extends Minion {
    public Tank(final CardInput card, final int playerIdx) {
        super(card, playerIdx);
        setFrontRow(true);
    }
}
