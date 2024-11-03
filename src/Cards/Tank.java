package Cards;

import fileio.CardInput;

public class Tank extends Minion {
    public Tank(CardInput card, int playerIdx) {
        super(card, playerIdx);
        setFrontRow(true);
    }
}
