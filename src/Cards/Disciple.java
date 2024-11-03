package Cards;

import fileio.CardInput;

public class Disciple extends SpecialMinion {
    public Disciple(CardInput card, int playerIdx) {
        super(card, playerIdx);
        setFrontRow(false);
    }

    public void ability(Minion target) {
        target.setHealth(target.getHealth() + 2);
    }
}
