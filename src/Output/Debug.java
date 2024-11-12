package Output;

import Cards.Card;
import Cards.Minion;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import Game.Game;
import fileio.Coordinates;

import java.util.ArrayList;

public class Debug extends Output {

    public Debug(ArrayNode output, ObjectMapper objectMapper) {
        super(output, objectMapper);
    }

    public void getPlayerDeck(ObjectNode objectNode, int playerIdx, Game game) {
        objectNode.put("command", "getPlayerDeck");
        objectNode.put("playerIdx", playerIdx);
        if (playerIdx == 1) {
            game.getPlayerOne().getCurrentDeck().printDeckJSON(objectMapper, objectNode);
        } else if (playerIdx == 2) {
            game.getPlayerTwo().getCurrentDeck().printDeckJSON(objectMapper, objectNode);
        }
        output.add(objectNode);
    }

    public void getCardsInHand(ObjectNode objectNode, int playerIdx, Game game) {
        objectNode.put("command", "getCardsInHand");
        objectNode.put("playerIdx", playerIdx);
        if (playerIdx == 1) {
            game.getPlayerOne().getHand().printDeckJSON(objectMapper, objectNode);
        } else if (playerIdx == 2) {
            game.getPlayerTwo().getHand().printDeckJSON(objectMapper, objectNode);
        }
        output.add(objectNode);
    }

    public void getCardsOnTable(ObjectNode objectNode, int playerIdx, Game game) {
        objectNode.put("command", "getCardsOnTable");
        ArrayNode tableNode = objectMapper.createArrayNode();
        for (ArrayList<Minion> row: game.getBoard()) {
            ArrayNode tableCol = objectMapper.createArrayNode();
            for (Card card: row) {
                ObjectNode cardNode = objectMapper.createObjectNode();
                tableCol.add(card.printCardJSON(cardNode, objectMapper));
            }
            tableNode.add(tableCol);
        }
        objectNode.set("output", tableNode);
        output.add(objectNode);
    }

    public void getPlayerHero(ObjectNode objectNode, int playerIdx, Game game) {
        objectNode.put("command", "getPlayerHero");
        objectNode.put("playerIdx", playerIdx);
        if (playerIdx == 1) {
            game.getPlayerOne().getHero().printCardJSON(objectNode, objectMapper);
        } else if (playerIdx == 2) {
            game.getPlayerTwo().getHero().printCardJSON(objectNode, objectMapper);
        }
        output.add(objectNode);
    }

    public void getCardAtPosition(ObjectNode objectNode, int x, int y, Game game) {
        objectNode.put("command", "getCardAtPosition");
        objectNode.put("x", x);
        objectNode.put("y", y);
        if (game.getBoard().get(x).isEmpty() || game.getBoard().get(x).size() < y) {
            objectNode.put("output", "No card available at that position.");
            output.add(objectNode);
        } else {
            game.getBoard().get(x).get(y).printCardJSON(objectNode, objectMapper);
            output.add(objectNode);
        }
    }

    public void getPlayerTurn(ObjectNode objectNode, Game game) {
        objectNode.put("command", "getPlayerTurn");
        objectNode.put("output", game.getCurrentPlayerTurn());
        output.add(objectNode);
    }

    public void getPlayerMana(ObjectNode objectNode, int playerIdx, Game game) {
        objectNode.put("command", "getPlayerMana");
        objectNode.put("playerIdx", playerIdx);
        if (playerIdx == 1) {
            objectNode.put("output", game.getPlayerOne().getMana());
            output.add(objectNode);
        } else if (playerIdx == 2) {
            objectNode.put("output", game.getPlayerTwo().getMana());
            output.add(objectNode);
        }
    }

    public void getFrozenCardsOnTable(ObjectNode objectNode, Game game) {
        objectNode.put("command", "getFrozenCardsOnTable");
        ArrayNode frozenTable = objectMapper.createArrayNode();
        for (ArrayList<Minion> row: game.getBoard()) {
            for (Card card: row) {
                if (card.isFrozen()) {
                    ObjectNode cardNode = objectMapper.createObjectNode();
                    frozenTable.add(card.printCardJSON(cardNode, objectMapper));
                }
            }
        }
        objectNode.set("output", frozenTable);
        output.add(objectNode);
    }

    public void getTotalGamesPlayed(ObjectNode objectNode, Game game) {
        objectNode.put("command", "getTotalGamesPlayed");
        objectNode.put("output", game.getPlayerOne().getGamesPlayed());
        output.add(objectNode);
    }

    public void getPlayerOneWins(ObjectNode objectNode, Game game) {
        objectNode.put("command", "getPlayerOneWins");
        objectNode.put("output", game.getPlayerOne().getGamesWon());
        output.add(objectNode);
    }

    public void getPlayerTwoWins(ObjectNode objectNode, Game game) {
        objectNode.put("command", "getPlayerTwoWins");
        objectNode.put("output", game.getPlayerTwo().getGamesWon());
        output.add(objectNode);
    }
}
