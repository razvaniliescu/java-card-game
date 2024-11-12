package Output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.Coordinates;

public class Error extends Output {
    public Error(ArrayNode output, ObjectMapper objectMapper) {
        super(output, objectMapper);
    }

    public void placeCardError(ObjectNode objectNode, int error, int handIdx) {
        objectNode.put("command", "placeCard");
        objectNode.put("handIdx", handIdx);
        switch (error) {
            case 1: objectNode.put("error", "Not enough mana to place card on table.");
                break;
            case 2: objectNode.put("error", "Cannot place card on table since row is full.");
                break;
        }
        output.add(objectNode);
    }

    public void cardUsesAttackError(ObjectNode objectNode, int error, Coordinates attackerCoords, Coordinates attackedCoords) {
        objectNode.put("command", "cardUsesAttack");
        ObjectNode coordNodeAttacker = objectMapper.createObjectNode();
        coordNodeAttacker.put("x", attackerCoords.getX());
        coordNodeAttacker.put("y", attackerCoords.getY());
        objectNode.set("cardAttacker", coordNodeAttacker);
        ObjectNode coordNodeAttacked = objectMapper.createObjectNode();
        coordNodeAttacked.put("x", attackedCoords.getX());
        coordNodeAttacked.put("y", attackedCoords.getY());
        objectNode.set("cardAttacked", coordNodeAttacked);
        switch (error) {
            case 1: objectNode.put("error", "Attacked card does not belong to the enemy.");
                break;
            case 2: objectNode.put("error", "Attacker card has already attacked this turn.");
                break;
            case 3: objectNode.put("error", "Attacker card is frozen.");
                break;
            case 4: objectNode.put("error", "Attacked card is not of type 'Tank'.");
                break;
        }
        output.add(objectNode);
    }

    public void cardUsesAbilityError(ObjectNode objectNode, int error, Coordinates attackerCoords, Coordinates attackedCoords) {
        objectNode.put("command", "cardUsesAbility");
        ObjectNode coordNodeAttacker = objectMapper.createObjectNode();
        coordNodeAttacker.put("x", attackerCoords.getX());
        coordNodeAttacker.put("y", attackerCoords.getY());
        objectNode.set("cardAttacker", coordNodeAttacker);
        ObjectNode coordNodeAttacked = objectMapper.createObjectNode();
        coordNodeAttacked.put("x", attackedCoords.getX());
        coordNodeAttacked.put("y", attackedCoords.getY());
        objectNode.set("cardAttacked", coordNodeAttacked);
        switch (error) {
            case 1: objectNode.put("error", "Attacker card is frozen.");
                break;
            case 2: objectNode.put("error", "Attacker card has already attacked this turn.");
                break;
            case 3: objectNode.put("error", "Attacked card does not belong to the current player.");
                break;
            case 4: objectNode.put("error", "Attacked card does not belong to the enemy.");
                break;
            case 5: objectNode.put("error", "Attacked card is not of type 'Tank'.");
                break;
        }
        output.add(objectNode);
    }

    public void useAttackHeroError(ObjectNode objectNode, int error, Coordinates attackerCoords) {
        objectNode.put("command", "useAttackHero");
        ObjectNode coordNodeAttacker = objectMapper.createObjectNode();
        coordNodeAttacker.put("x", attackerCoords.getX());
        coordNodeAttacker.put("y", attackerCoords.getY());
        objectNode.set("cardAttacker", coordNodeAttacker);
        switch (error) {
            case 1: objectNode.put("error", "Attacker card is frozen.");
                break;
            case 2: objectNode.put("error", "Attacker card has already attacked this turn.");
                break;
            case 3: objectNode.put("error", "Attacked card is not of type 'Tank'.");
                break;
        }
        output.add(objectNode);
    }

    public void useHeroAbilityError(ObjectNode objectNode, int error, int row) {
        objectNode.put("command", "useHeroAbility");
        objectNode.put("affectedRow", row);
        switch (error) {
            case 1: objectNode.put("error", "Not enough mana to use hero's ability.");
                break;
            case 2: objectNode.put("error", "Hero has already attacked this turn.");
                break;
            case 3: objectNode.put("error", "Selected row does not belong to the enemy.");
                break;
            case 4: objectNode.put("error", "Selected row does not belong to the current player.");
                break;
        }
        output.add(objectNode);
    }
}
