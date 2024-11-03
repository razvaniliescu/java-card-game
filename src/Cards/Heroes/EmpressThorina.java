package Cards.Heroes;

import Cards.Hero;
import Cards.Minion;
import fileio.CardInput;

import java.util.ArrayList;

public class EmpressThorina extends Hero {
    public EmpressThorina(CardInput hero, int playerIdx) {
        super(hero, playerIdx);
    }

    public void ability(ArrayList<Minion> row) {
        Minion maxHealthCard = row.get(0);
        for (Minion minion : row) {
            if (minion.getHealth() > maxHealthCard.getHealth()) {
                maxHealthCard = minion;
            }
        }
        row.remove(maxHealthCard);
    }
}
