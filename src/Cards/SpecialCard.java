package Cards;

import java.util.ArrayList;

public class SpecialCard extends Card {
    public SpecialCard(int mana, int health, int attackDamage, String description, ArrayList<String> colors, String name) {
        super(mana, health, attackDamage, description, colors, name);
    }

    public void ability() {}
}
