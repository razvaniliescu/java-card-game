package cards;

import fileio.CardInput;

import java.util.ArrayList;
import static constants.Constants.*;

/**
 * Card subclass for the hero type
 */
public class Hero extends Card {
    public Hero(final CardInput hero, final int playerIdx) {
        super(hero, playerIdx);
        setHealth(INITIAL_HERO_HEALTH);
    }

    /**
     * Method for hero's ability to be overridden in specific hero subclass
     * @param row selected row
     */
    public void ability(final ArrayList<Minion> row) {

    }
}
