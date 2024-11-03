package Cards.Heroes;

import Cards.Hero;
import Cards.Minion;
import fileio.CardInput;

import java.util.ArrayList;

public class GeneralKocioraw extends Hero {
    public GeneralKocioraw(CardInput hero, int playerIdx) {
        super(hero, playerIdx);
    }

    public void ability(ArrayList<Minion> row) {
        for (Minion minion : row) {
            minion.setAttackDamage(minion.getAttackDamage() + 1);
        }
    }
}
