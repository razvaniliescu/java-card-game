package cards.specialMinions;

import cards.Minion;
import cards.SpecialMinion;
import fileio.CardInput;

/**
 * Contains minion specific constructor and ability for Disciple
 */
public final class Disciple extends SpecialMinion {
    public Disciple(final CardInput card, final int playerIdx) {
        super(card, playerIdx);
        setFrontRow(false);
    }

    /**
     * Increases the health of a friendly minion by 2
     * @param target selected minion
     */
    public void ability(final Minion target) {
        target.setHealth(target.getHealth() + 2);
    }
}
