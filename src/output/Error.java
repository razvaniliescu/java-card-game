package output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.Coordinates;

/**
 * Contains methods for printing errors in JSON format
 */
public class Error extends Output {
    public Error(final ArrayNode output, final ObjectMapper objectMapper,
                 final ObjectNode objectNode) {
        super(output, objectMapper, objectNode);
    }

    /**
     * Prints error if placing a card is invalid
     */
    public void placeCardError(final int error, final int handIdx) {
        objectNode.put("command", "placeCard");
        objectNode.put("handIdx", handIdx);
        switch (error) {
            case 1: objectNode.put("error",
                    "Not enough mana to place card on table.");
                break;
            case 2: objectNode.put("error",
                    "Cannot place card on table since row is full.");
                break;
            default: break;
        }
        output.add(objectNode);
    }

    /**
     * Prints error if using a card attack is invalid
     */
    public void cardUsesAttackError(final int error, final Coordinates attackerCoords,
                                    final Coordinates attackedCoords) {
        objectNode.put("command", "cardUsesAttack");
        ObjectNode coordNodeAttacker = objectMapper.createObjectNode();
        coordNodeAttacker.put("x", attackerCoords.getX());
        coordNodeAttacker.put("y", attackerCoords.getY());
        objectNode.set("cardAttacker", coordNodeAttacker);
        ObjectNode coordNodeAttacked = objectMapper.createObjectNode();
        coordNodeAttacked.put("x", attackedCoords.getX());
        coordNodeAttacked.put("y", attackedCoords.getY());
        objectNode.set("cardAttacked", coordNodeAttacked);
        final int attackedCardIsFriendly = 1;
        final int cardAlreadyAttacked = 2;
        final int cardIsFrozen = 3;
        final int attackedCardIsNotTank = 4;
        switch (error) {
            case attackedCardIsFriendly: objectNode.put("error",
                    "Attacked card does not belong to the enemy.");
                break;
            case cardAlreadyAttacked: objectNode.put("error",
                    "Attacker card has already attacked this turn.");
                break;
            case cardIsFrozen: objectNode.put("error",
                    "Attacker card is frozen.");
                break;
            case attackedCardIsNotTank: objectNode.put("error",
                    "Attacked card is not of type 'Tank'.");
                break;
            default: break;
        }
        output.add(objectNode);
    }

    /**
     * Prints error if using a card ability is invalid
     */
    public void cardUsesAbilityError(final int error, final Coordinates attackerCoords,
                                     final Coordinates attackedCoords) {
        objectNode.put("command", "cardUsesAbility");
        ObjectNode coordNodeAttacker = objectMapper.createObjectNode();
        coordNodeAttacker.put("x", attackerCoords.getX());
        coordNodeAttacker.put("y", attackerCoords.getY());
        objectNode.set("cardAttacker", coordNodeAttacker);
        ObjectNode coordNodeAttacked = objectMapper.createObjectNode();
        coordNodeAttacked.put("x", attackedCoords.getX());
        coordNodeAttacked.put("y", attackedCoords.getY());
        objectNode.set("cardAttacked", coordNodeAttacked);
        final int cardIsFrozen = 1;
        final int cardAlreadyAttacked = 2;
        final int attackedCardIsNotFriendly = 3;
        final int attackedCardIsFriendly = 4;
        final int attackedCardIsNotTank = 5;
        switch (error) {
            case cardIsFrozen: objectNode.put("error",
                    "Attacker card is frozen.");
                break;
            case cardAlreadyAttacked: objectNode.put("error",
                    "Attacker card has already attacked this turn.");
                break;
            case attackedCardIsNotFriendly: objectNode.put("error",
                    "Attacked card does not belong to the current player.");
                break;
            case attackedCardIsFriendly: objectNode.put("error",
                    "Attacked card does not belong to the enemy.");
                break;
            case attackedCardIsNotTank: objectNode.put("error",
                    "Attacked card is not of type 'Tank'.");
                break;
            default: break;
        }
        output.add(objectNode);
    }

    /**
     * Prints error if attacking a hero is invalid
     */
    public void useAttackHeroError(final int error, final Coordinates attackerCoords) {
        objectNode.put("command", "useAttackHero");
        ObjectNode coordNodeAttacker = objectMapper.createObjectNode();
        coordNodeAttacker.put("x", attackerCoords.getX());
        coordNodeAttacker.put("y", attackerCoords.getY());
        objectNode.set("cardAttacker", coordNodeAttacker);
        final int cardIsFrozen = 1;
        final int cardAlreadyAttacked = 2;
        final int mustAttackTankFirst = 3;
        switch (error) {
            case cardIsFrozen: objectNode.put("error",
                    "Attacker card is frozen.");
                break;
            case cardAlreadyAttacked: objectNode.put("error",
                    "Attacker card has already attacked this turn.");
                break;
            case mustAttackTankFirst: objectNode.put("error",
                    "Attacked card is not of type 'Tank'.");
                break;
            default: break;
        }
        output.add(objectNode);
    }

    /**
     * Prints error if using the hero's ability is invalid
     */
    public void useHeroAbilityError(final int error, final int row) {
        objectNode.put("command", "useHeroAbility");
        objectNode.put("affectedRow", row);
        final int notEnoughMana = 1;
        final int heroAlreadyUsedAbility = 2;
        final int selectedRowIsFriendly = 3;
        final int selectedRowIsNotFriendly = 4;
        switch (error) {
            case notEnoughMana: objectNode.put("error",
                    "Not enough mana to use hero's ability.");
                break;
            case heroAlreadyUsedAbility: objectNode.put("error",
                    "Hero has already attacked this turn.");
                break;
            case selectedRowIsFriendly: objectNode.put("error",
                    "Selected row does not belong to the enemy.");
                break;
            case selectedRowIsNotFriendly: objectNode.put("error",
                    "Selected row does not belong to the current player.");
                break;
            default: break;
        }
        output.add(objectNode);
    }
}
