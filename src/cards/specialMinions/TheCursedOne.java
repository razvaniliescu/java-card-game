package cards.specialMinions;

import cards.Minion;
import cards.SpecialMinion;
import fileio.CardInput;

/**
 * Contains minion specific constructor and ability for The Cursed One
 */
public final class TheCursedOne extends SpecialMinion {
    public TheCursedOne(final CardInput card, final int playerIdx) {
        super(card, playerIdx);
        setFrontRow(false);
    }

    /**
     * Swaps the health and attack damage of a minion
     * @param target selected minion
     */
    public void ability(final Minion target) {
        int aux = target.getHealth();
        target.setHealth(target.getAttackDamage());
        target.setAttackDamage(aux);
    }
}
