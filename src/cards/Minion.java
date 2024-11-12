package cards;

import fileio.CardInput;

/**
 * Parent class for all the cards that can be placed on the board
 */
public class Minion extends Card {
    protected boolean frontRow;
    protected boolean frozen;

    /**
     * Checks if the card is frozen
     */
    public boolean isFrozen() {
        return frozen;
    }

    /**
     * Sets whether the card is frozen or not
     */
    public void setFrozen(final boolean frozen) {
        this.frozen = frozen;
    }

    /**
     * Checks if the minion must be placed in the front or back row
     */
    public boolean isFrontRow() {
        return frontRow;
    }

    /**
     * Sets how the minion should be placed
     */
    public void setFrontRow(final boolean frontRow) {
        this.frontRow = frontRow;
    }

    public Minion(final CardInput card, final int playerIdx) {
        super(card, playerIdx);
        this.frozen = false;
        this.frontRow = false;
    }

    /**
     * Attacks the selected target
     */
    public void attack(final Card target) {
        target.setHealth(target.getHealth() - attackDamage);
        setAttackedThisTurn(true);
    }
}
