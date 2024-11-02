package Cards;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;

import java.util.ArrayList;

public class Hero extends Card {
    public Hero(CardInput hero) {
        super(hero.getMana(), 30, hero.getAttackDamage(), hero.getDescription(), hero.getColors(), hero.getName());
    }

    public void printHeroJSON(ObjectNode objectNode, ObjectMapper objectMapper) {
        ObjectNode heroNode = objectMapper.createObjectNode();
        heroNode.put("mana", this.getMana());
        heroNode.put("description", this.getDescription());
        ArrayNode colors = objectMapper.createArrayNode();
        for (String color : this.getColors()) {
            colors.add(color);
        }
        heroNode.set("colors", colors);
        heroNode.put("name", this.getName());
        heroNode.put("health", this.getHealth());
        objectNode.set("output", heroNode);
    }
}
