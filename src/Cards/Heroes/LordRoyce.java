package Cards.Heroes;

import Cards.Hero;
import Cards.Minion;
import fileio.CardInput;

import java.util.ArrayList;

public class LordRoyce extends Hero {
    public LordRoyce(CardInput hero, int playerIdx) {
        super(hero, playerIdx);
    }

    public void ability(ArrayList<Minion> row) {
        for (Minion minion : row) {
            minion.setFrozen(true);
        }
    }
}
