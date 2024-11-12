package cards;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Parent class for all types of cards
 */
public class Card {
    protected int mana;
    protected int health;
    protected int attackDamage;
    protected String description;
    protected ArrayList<String> colors;
    protected String name;
    protected int playerIdx;
    protected boolean attackedThisTurn;

    /**
     * Checks if card has acted this turn
     */
    public boolean isAttackedThisTurn() {
        return attackedThisTurn;
    }

    /**
     * Sets whether the card has acted this turn or not
     */
    public void setAttackedThisTurn(final boolean hasAttackedThisTurn) {
        this.attackedThisTurn = hasAttackedThisTurn;
    }

    /**
     * Gets the card's owner
     */
    public int getPlayerIdx() {
        return playerIdx;
    }

    /**
     * Sets the card's owner
     */
    public void setPlayerIdx(final int playerIdx) {
        this.playerIdx = playerIdx;
    }

    /**
     * Gets the card's mana cost
     */
    public int getMana() {
        return mana;
    }

    /**
     * Sets the card's mana cost
     */
    public void setMana(final int mana) {
        this.mana = mana;
    }

    /**
     * Gets the card's health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Sets the card's health
     */
    public void setHealth(final int health) {
        this.health = health;
    }

    /**
     * Gets the card's attack damage
     */
    public int getAttackDamage() {
        return attackDamage;
    }

    /**
     * Sets the card's attack damage
     */
    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    /**
     * Gets the card's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the card's description
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Gets the card's colors
     */
    public ArrayList<String> getColors() {
        return colors;
    }

    /**
     * Sets the card's colors
     */
    public void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }


    /**
     * Gets the card's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the card's name
     */
    public void setName(final String name) {
        this.name = name;
    }

    public Card(final CardInput card, final int playerIdx) {
        this.mana = card.getMana();
        this.health = card.getHealth();
        this.attackDamage = card.getAttackDamage();
        this.description = card.getDescription();
        this.colors = card.getColors();
        this.name = card.getName();
        this.playerIdx = playerIdx;
    }

    /**
     * Prints card info in JSON format
     * @param objectMapper object mapper
     * @param objectNode current object node
     */
    public ObjectNode printCardJSON(final ObjectNode objectNode, final ObjectMapper objectMapper) {
        ObjectNode cardNode = objectMapper.createObjectNode();
        cardNode.put("mana", mana);
        cardNode.put("description", description);
        if (!(Objects.equals(this.name, "Lord Royce")
                || Objects.equals(this.name, "Empress Thorina")
                || Objects.equals(this.name, "King Mudface")
                || Objects.equals(this.name, "General Kocioraw"))) {
            cardNode.put("attackDamage", attackDamage);
        }
        ArrayNode colorNode = objectMapper.createArrayNode();
        for (String color : colors) {
            colorNode.add(color);
        }
        cardNode.set("colors", colorNode);
        cardNode.put("name", name);
        cardNode.put("health", health);
        objectNode.set("output", cardNode);
        return cardNode;
    }
}
