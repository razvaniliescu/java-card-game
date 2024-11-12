package cards.heroes;

import cards.Hero;
import cards.Minion;
import fileio.CardInput;

import java.util.ArrayList;

/**
 * Contains hero specific constructor and ability for General Kocioraw
 */
public class GeneralKocioraw extends Hero {
    public GeneralKocioraw(final CardInput hero, final int playerIdx) {
        super(hero, playerIdx);
    }

    /**
     * Increases the attack of the minions of the selected row by 1
     * @param row selected row
     */
    public void ability(final ArrayList<Minion> row) {
        for (Minion minion : row) {
            minion.setAttackDamage(minion.getAttackDamage() + 1);
        }
    }
}
