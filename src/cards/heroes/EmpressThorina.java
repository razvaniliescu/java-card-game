package cards.heroes;

import cards.Hero;
import cards.Minion;
import fileio.CardInput;
import java.util.ArrayList;

/**
 * Contains hero specific constructor and ability for Empress Thorina
 */
public final class EmpressThorina extends Hero {
    public EmpressThorina(final CardInput hero, final int playerIdx) {
        super(hero, playerIdx);
    }

    /**
     * Destroys the minion with the highest health from the selected row
     * @param row selected row
     */
    public void ability(final ArrayList<Minion> row) {
        Minion maxHealthCard = row.getFirst();
        for (Minion minion : row) {
            if (minion.getHealth() > maxHealthCard.getHealth()) {
                maxHealthCard = minion;
            }
        }
        row.remove(maxHealthCard);
    }
}
