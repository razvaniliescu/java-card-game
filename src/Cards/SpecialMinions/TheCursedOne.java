package Cards.SpecialMinions;

import Cards.Minion;
import Cards.SpecialMinion;
import fileio.CardInput;

public class TheCursedOne extends SpecialMinion {
    public TheCursedOne(CardInput card, int playerIdx) {
        super(card, playerIdx);
        setFrontRow(false);
    }

    public void ability(Minion target) {
        int aux = target.getHealth();
        target.setHealth(target.getAttackDamage());
        target.setAttackDamage(aux);
    }
}
