package cards;

import fileio.CardInput;

/**
 * Minion subclass for the special minion type
 */
public class SpecialMinion extends Minion {
    public SpecialMinion(final CardInput card, final int playerIdx) {
        super(card, playerIdx);
    }

    /**
     * Method for the minion's ability to be overridden in specific minion subclass
     * @param target selected minion
     */
    public void ability(final Minion target) {

    }
}
