package Cards;

import fileio.CardInput;

public class Miraj extends SpecialMinion {
    public Miraj(CardInput card, int playerIdx) {
        super(card, playerIdx);
        setFrontRow(true);
    }

    public void ability(Minion target) {
        int aux = target.getHealth();
        target.setHealth(this.getHealth());
        this.setHealth(aux);
    }
}
