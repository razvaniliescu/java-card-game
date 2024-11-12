package output;

import cards.Card;
import cards.Minion;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import game.Game;

import java.util.ArrayList;

/**
 * Contains methods for printing debug commands in JSON format
 */
public class Debug extends Output {
    private final Game game;

    public Debug(final ArrayNode output, final ObjectMapper objectMapper,
                 final ObjectNode objectNode, final Game game) {
        super(output, objectMapper, objectNode);
        this.game = game;
    }

    /**
     * Prints the selected player's deck in JSON format
     * @param playerIdx selected player
     */
    public void getPlayerDeck(final int playerIdx) {
        objectNode.put("command", "getPlayerDeck");
        objectNode.put("playerIdx", playerIdx);
        if (playerIdx == 1) {
            game.getPlayerOne().getCurrentDeck().printDeckJSON(objectMapper, objectNode);
        } else if (playerIdx == 2) {
            game.getPlayerTwo().getCurrentDeck().printDeckJSON(objectMapper, objectNode);
        }
        output.add(objectNode);
    }

    /**
     * Prints the selected player's hand in JSON format
     * @param playerIdx selected player
     */
    public void getCardsInHand(final int playerIdx) {
        objectNode.put("command", "getCardsInHand");
        objectNode.put("playerIdx", playerIdx);
        if (playerIdx == 1) {
            game.getPlayerOne().getHand().printDeckJSON(objectMapper, objectNode);
        } else if (playerIdx == 2) {
            game.getPlayerTwo().getHand().printDeckJSON(objectMapper, objectNode);
        }
        output.add(objectNode);
    }

    /**
     * Prints the cards on the table in JSON format
     */
    public void getCardsOnTable() {
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

    /**
     * Prints the selected player's hero in JSON format
     * @param playerIdx selected player
     */
    public void getPlayerHero(final int playerIdx) {
        objectNode.put("command", "getPlayerHero");
        objectNode.put("playerIdx", playerIdx);
        if (playerIdx == 1) {
            game.getPlayerOne().getHero().printCardJSON(objectNode, objectMapper);
        } else if (playerIdx == 2) {
            game.getPlayerTwo().getHero().printCardJSON(objectNode, objectMapper);
        }
        output.add(objectNode);
    }

    /**
     * Prints the selected card in JSON format
     * @param x card's row
     * @param y card's column
     */
    public void getCardAtPosition(final int x, final int y) {
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

    /**
     * Prints the current player's turn in JSON format
     */
    public void getPlayerTurn() {
        objectNode.put("command", "getPlayerTurn");
        objectNode.put("output", game.getCurrentPlayerTurn());
        output.add(objectNode);
    }

    /**
     * Prints the selected player's mana in JSON format
     * @param playerIdx selected player
     */
    public void getPlayerMana(final int playerIdx) {
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

    /**
     * Prints the frozen cards on the table in JSON format
     */
    public void getFrozenCardsOnTable() {
        objectNode.put("command", "getFrozenCardsOnTable");
        ArrayNode frozenTable = objectMapper.createArrayNode();
        for (ArrayList<Minion> row: game.getBoard()) {
            for (Minion minion: row) {
                if (minion.isFrozen()) {
                    ObjectNode cardNode = objectMapper.createObjectNode();
                    frozenTable.add(minion.printCardJSON(cardNode, objectMapper));
                }
            }
        }
        objectNode.set("output", frozenTable);
        output.add(objectNode);
    }

    /**
     * Prints the number of games played in JSON format
     */
    public void getTotalGamesPlayed() {
        objectNode.put("command", "getTotalGamesPlayed");
        objectNode.put("output", game.getPlayerOne().getGamesPlayed());
        output.add(objectNode);
    }

    /**
     * Prints the number of games won by player one in JSON format
     */
    public void getPlayerOneWins() {
        objectNode.put("command", "getPlayerOneWins");
        objectNode.put("output", game.getPlayerOne().getGamesWon());
        output.add(objectNode);
    }

    /**
     * Prints the number of games won by player two in JSON format
     */
    public void getPlayerTwoWins() {
        objectNode.put("command", "getPlayerTwoWins");
        objectNode.put("output", game.getPlayerTwo().getGamesWon());
        output.add(objectNode);
    }
}
