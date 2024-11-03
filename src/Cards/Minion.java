package Cards;

import fileio.CardInput;

public class Minion extends Card {
    private boolean frontRow;

    public boolean isFrontRow() {
        return frontRow;
    }

    public void setFrontRow(boolean frontRow) {
        this.frontRow = frontRow;
    }

    public Minion(CardInput card, int playerIdx) {
        super(card, playerIdx);
        setFrontRow(false);
    }

    public void attack(Card target) {
        target.setHealth(target.getHealth() - this.getAttackDamage());
        setAttackedThisTurn(true);
    }
}
