package Cards;

import fileio.CardInput;
import fileio.Coordinates;
import Game.Game;

public class SpecialMinion extends Minion {
    public SpecialMinion(CardInput card, int playerIdx) {
        super(card, playerIdx);
    }

    public void ability(Minion target) {}
}
