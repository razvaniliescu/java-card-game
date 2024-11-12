package cards.specialMinions;

import cards.Minion;
import cards.SpecialMinion;
import fileio.CardInput;

/**
 * Contains minion specific constructor and ability for Miraj
 */
public final class Miraj extends SpecialMinion {
    public Miraj(final CardInput card, final int playerIdx) {
        super(card, playerIdx);
        setFrontRow(true);
    }

    /**
     * Swap this minion's health with the health of another minion
     * @param target selected minion
     */
    public void ability(final Minion target) {
        int aux = target.getHealth();
        target.setHealth(this.health);
        this.health = aux;
    }
}
