package cards;

import fileio.CardInput;

import java.util.ArrayList;

/**
 * Card subclass for the hero type
 */
public class Hero extends Card {
    public Hero(final CardInput hero, final int playerIdx) {
        super(hero, playerIdx);
        final int initialHealth = 30;
        setHealth(initialHealth);
    }

    /**
     * Method for hero's ability to be overridden in specific hero subclass
     * @param row selected row
     */
    public void ability(final ArrayList<Minion> row) {

    }
}
