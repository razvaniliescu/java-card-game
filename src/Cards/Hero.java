package Cards;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;

import java.util.ArrayList;

public class Hero extends Card {
    public Hero(CardInput hero, int playerIdx) {
        super(hero, playerIdx);
        setHealth(30);
    }

    public void ability(ArrayList<Minion> row) {}
}
