package cards.specialMinions;

import cards.Minion;
import cards.SpecialMinion;
import fileio.CardInput;

/**
 * Contains minion specific constructor and ability for The Ripper
 */
public final class TheRipper extends SpecialMinion {
    public TheRipper(final CardInput card, final int playerIdx) {
        super(card, playerIdx);
        setFrontRow(true);
    }

    /**
     * Reduces the attack damage of a minion by 2
     * @param target selected minion
     */
    public void ability(final Minion target) {
        target.setAttackDamage(target.getAttackDamage() - 2);
        if (target.getAttackDamage() < 0) {
            target.setAttackDamage(0);
        }
    }
}
