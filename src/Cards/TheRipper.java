package Cards;

import fileio.CardInput;

public class TheRipper extends SpecialMinion{
    public TheRipper(CardInput card, int playerIdx) {
        super(card, playerIdx);
        setFrontRow(true);
    }

    public void ability(Minion target) {
        target.setAttackDamage(target.getAttackDamage() - 2);
        if (target.getAttackDamage() < 0) {
            target.setAttackDamage(0);
        }
    }
}
