package cards.heroes;

import cards.Hero;
import cards.Minion;
import fileio.CardInput;

import java.util.ArrayList;

/**
 * Contains hero specific constructor and ability for Lord Royce
 */
public final class LordRoyce extends Hero {
    public LordRoyce(final CardInput hero, final int playerIdx) {
        super(hero, playerIdx);
    }

    /**
     * Freezes all minions on the selected row
     * @param row selected row
     */
    public void ability(final ArrayList<Minion> row) {
        for (Minion minion : row) {
            minion.setFrozen(true);
        }
    }
}
