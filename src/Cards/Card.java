package Cards;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;

import java.util.ArrayList;
import java.util.Objects;

public class Card {
    private int mana;
    private int health;
    private int attackDamage;
    private String description;
    private ArrayList<String> colors;
    private String name;
    private boolean tank;
    private boolean frozen;

    public boolean isTank() {
        return tank;
    }

    public void setTank(boolean tank) {
        this.tank = tank;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public void setColors(ArrayList<String> colors) {
        this.colors = colors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public Card(CardInput card) {
        this.mana = card.getMana();
        this.health = card.getHealth();
        this.attackDamage = card.getAttackDamage();
        this.description = card.getDescription();
        this.colors = card.getColors();
        this.name = card.getName();
        this.frozen = false;
        this.tank = Objects.equals(name, "Goliath") || Objects.equals(name, "Warden") || Objects.equals(name, "The Ripper") || Objects.equals(name, "Miraj");
    }

    public ObjectNode printCardJSON(ObjectNode objectNode, ObjectMapper objectMapper) {
        ObjectNode cardNode = objectMapper.createObjectNode();
        cardNode.put("mana", this.getMana());
        cardNode.put("description", this.getDescription());
        if (!(this instanceof Hero)) {
            cardNode.put("attackDamage", this.getAttackDamage());
        }
        ArrayNode colors = objectMapper.createArrayNode();
        for (String color : this.getColors()) {
            colors.add(color);
        }
        cardNode.set("colors", colors);
        cardNode.put("name", this.getName());
        cardNode.put("health", this.getHealth());
        objectNode.set("output", cardNode);
        return cardNode;
    }
}
