package Cards.Heroes;

import Cards.Hero;
import Cards.Minion;
import fileio.CardInput;

import java.util.ArrayList;

public class KingMudface extends Hero {
    public KingMudface(CardInput hero, int playerIdx) {
        super(hero, playerIdx);
    }

    public void ability(ArrayList<Minion> row) {
        for (Minion minion : row) {
            minion.setHealth(minion.getHealth() + 1);
        }
    }
}
