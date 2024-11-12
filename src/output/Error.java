package output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.Coordinates;

import static constants.Constants.*;

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
        if (error == NOT_ENOUGH_MANA) {
            objectNode.put("error", "Not enough mana to place card on table.");
        } else if (error == TOO_MANY_MINIONS_ON_ROW) {
            objectNode.put("error", "Cannot place card on table since row is full.");
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
        if (error == CARD_IS_FROZEN) {
            objectNode.put("error", "Attacker card is frozen.");
        } else if (error == CARD_ALREADY_ATTACKED) {
            objectNode.put("error", "Attacker card has already attacked this turn.");
        } else if (error == ATTACKED_CARD_IS_FRIENDLY) {
            objectNode.put("error", "Attacked card does not belong to the enemy.");
        } else if (error == ATTACKED_CARD_IS_NOT_TANK) {
            objectNode.put("error", "Attacked card is not of type 'Tank'.");
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
        if (error == CARD_IS_FROZEN) {
            objectNode.put("error", "Attacker card is frozen.");
        } else if (error == CARD_ALREADY_ATTACKED) {
            objectNode.put("error", "Attacker card has already attacked this turn.");
        } else if (error == ATTACKED_CARD_IS_FRIENDLY) {
            objectNode.put("error", "Attacked card does not belong to the enemy.");
        } else if (error == ATTACKED_CARD_IS_NOT_FRIENDLY) {
            objectNode.put("error", "Attacked card does not belong to the current player.");
        } else if (error == ATTACKED_CARD_IS_NOT_TANK) {
            objectNode.put("error", "Attacked card is not of type 'Tank'.");
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
        if (error == CARD_IS_FROZEN) {
            objectNode.put("error", "Attacker card is frozen.");
        } else if (error == CARD_ALREADY_ATTACKED) {
            objectNode.put("error", "Attacker card has already attacked this turn.");
        } else if (error == ATTACKED_CARD_IS_NOT_TANK) {
            objectNode.put("error", "Attacked card is not of type 'Tank'.");
        }
        output.add(objectNode);
    }

    /**
     * Prints error if using the hero's ability is invalid
     */
    public void useHeroAbilityError(final int error, final int row) {
        objectNode.put("command", "useHeroAbility");
        objectNode.put("affectedRow", row);
        if (error == NOT_ENOUGH_MANA) {
            objectNode.put("error", "Not enough mana to use hero's ability.");
        } else if (error == HERO_ALREADY_USED_ABILITY) {
            objectNode.put("error", "Hero has already attacked this turn.");
        } else if (error == SELECTED_ROW_IS_FRIENDLY) {
            objectNode.put("error", "Selected row does not belong to the enemy.");
        } else if (error == SELECTED_ROW_IS_NOT_FRIENDLY) {
            objectNode.put("error", "Selected row does not belong to the current player.");
        }
        output.add(objectNode);
    }
}
