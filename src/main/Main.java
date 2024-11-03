package main;

import Cards.Card;
import Cards.Hero;
import Cards.Minion;
import Deck.Deck;
import Game.Game;
import Player.Player;
import checker.Checker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import checker.CheckerConstants;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static java.util.Collections.shuffle;

/**
 * The entry point to this homework. It runs the checker that tests your implementation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Input inputData = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + filePath1),
                Input.class);

        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        File outputFile = new File(filePath2);
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        ArrayNode output = objectMapper.createArrayNode();

        Player playerOne = new Player(inputData.getPlayerOneDecks(), 1);
        Player playerTwo = new Player(inputData.getPlayerTwoDecks(), 2);
        ArrayList<GameInput> gameInput = inputData.getGames();

        for (GameInput g : gameInput) {
            ArrayList<ActionsInput> actionsInput = g.getActions();
            Game game = new Game(g.getStartGame().getStartingPlayer(), playerOne, playerTwo);

            playerOne.setHero(new Hero(g.getStartGame().getPlayerOneHero(), 1));
            playerTwo.setHero(new Hero(g.getStartGame().getPlayerTwoHero(), 2));

            int playerOneDeckIndex = g.getStartGame().getPlayerOneDeckIdx();
            int playerTwoDeckIndex = g.getStartGame().getPlayerTwoDeckIdx();

            playerOne.setCurrentDeck(new Deck(playerOne.getDecks(), playerOneDeckIndex));
            playerTwo.setCurrentDeck(new Deck(playerTwo.getDecks(), playerTwoDeckIndex));

            Random random = new Random(g.getStartGame().getShuffleSeed());
            shuffle(playerOne.getCurrentDeck().getCards(), random);
            random = new Random(g.getStartGame().getShuffleSeed());
            shuffle(playerTwo.getCurrentDeck().getCards(), random);

            playerOne.drawCard();
            playerTwo.drawCard();

            for (ActionsInput a : actionsInput) {
                ObjectNode objectNode = objectMapper.createObjectNode();
                int error = 0;
                Coordinates cardAttacker = null;
                Coordinates cardAttacked = null;
                switch (a.getCommand()) {
                    case "endPlayerTurn":
                        boolean endOfRound = game.switchTurn();
                        if (endOfRound) {
                            playerOne.drawCard();
                            playerTwo.drawCard();
                            playerOne.setMana(playerOne.getMana() + game.getManaPerRound());
                            playerTwo.setMana(playerTwo.getMana() + game.getManaPerRound());
                        }
                        break;
                    case "placeCard":
                        if (game.getCurrentPlayerTurn() == 1) {
                            error = playerOne.placeCard(a.getHandIdx(), game);
                        } else {
                            error = playerTwo.placeCard(a.getHandIdx(), game);
                        }
                        if (error != 0) {
                            objectNode.put("command", a.getCommand());
                            objectNode.put("handIdx", a.getHandIdx());
                            switch (error) {
                                case 1: objectNode.put("error", "Not enough mana to place card on table.");
                                break;
                                case 2: objectNode.put("error", "Cannot place card on table since row is full.");
                                break;
                            }
                            output.add(objectNode);
                        }
                        break;
                    case "cardUsesAttack":
                        cardAttacker = a.getCardAttacker();
                        cardAttacked = a.getCardAttacked();
                        error = game.useAttack(cardAttacker, cardAttacked);
                        if (error != 0) {
                            objectNode.put("command", a.getCommand());
                            ObjectNode coordNodeAttacker = objectMapper.createObjectNode();
                            coordNodeAttacker.put("x", cardAttacker.getX());
                            coordNodeAttacker.put("y", cardAttacker.getY());
                            objectNode.set("cardAttacker", coordNodeAttacker);
                            ObjectNode coordNodeAttacked = objectMapper.createObjectNode();
                            coordNodeAttacked.put("x", cardAttacked.getX());
                            coordNodeAttacked.put("y", cardAttacked.getY());
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
                        break;
                    case "cardUsesAbility":
                        cardAttacker = a.getCardAttacker();
                        cardAttacked = a.getCardAttacked();
                        error = game.useAbility(cardAttacker, cardAttacked);
                        if (error != 0) {
                            objectNode.put("command", a.getCommand());
                            ObjectNode coordNodeAttacker = objectMapper.createObjectNode();
                            coordNodeAttacker.put("x", cardAttacker.getX());
                            coordNodeAttacker.put("y", cardAttacker.getY());
                            objectNode.set("cardAttacker", coordNodeAttacker);
                            ObjectNode coordNodeAttacked = objectMapper.createObjectNode();
                            coordNodeAttacked.put("x", cardAttacked.getX());
                            coordNodeAttacked.put("y", cardAttacked.getY());
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
                        break;
                    case "useAttackHero":
                        cardAttacker = a.getCardAttacker();
                        error = game.attackHero(cardAttacker);
                        if (error != 0) {
                            objectNode.put("command", a.getCommand());
                            ObjectNode coordNodeAttacker = objectMapper.createObjectNode();
                            coordNodeAttacker.put("x", cardAttacker.getX());
                            coordNodeAttacker.put("y", cardAttacker.getY());
                            objectNode.set("cardAttacker", coordNodeAttacker);
                            switch (error) {
                                case 1: objectNode.put("error", "Attacker card is frozen.");
                                    output.add(objectNode);
                                    break;
                                case 2: objectNode.put("error", "Attacker card has already attacked this turn.");
                                    output.add(objectNode);
                                    break;
                                case 3: objectNode.put("error", "Attacked card is not of type 'Tank'.");
                                    output.add(objectNode);
                                    break;
                            }
                        } else {
                            if (playerOne.getHero().getHealth() <= 0) {
                                objectNode.put("gameEnded", "Player two killed the enemy hero.");
                                output.add(objectNode);
                            } else if (playerTwo.getHero().getHealth() <= 0) {
                                objectNode.put("gameEnded", "Player one killed the enemy hero.");
                                output.add(objectNode);
                            }
                        }
                        break;
                    case "getPlayerDeck":
                        objectNode.put("command", a.getCommand());
                        objectNode.put("playerIdx", a.getPlayerIdx());
                        if (a.getPlayerIdx() == 1) {
                            playerOne.getCurrentDeck().printDeckJSON(objectMapper, objectNode);
                        } else if (a.getPlayerIdx() == 2) {
                            playerTwo.getCurrentDeck().printDeckJSON(objectMapper, objectNode);
                        }
                        output.add(objectNode);
                        break;
                    case "getCardsInHand":
                        objectNode.put("command", a.getCommand());
                        objectNode.put("playerIdx", a.getPlayerIdx());
                        if (a.getPlayerIdx() == 1) {
                            playerOne.getHand().printDeckJSON(objectMapper, objectNode);
                        } else if (a.getPlayerIdx() == 2) {
                            playerTwo.getHand().printDeckJSON(objectMapper, objectNode);
                        }
                        output.add(objectNode);
                        break;
                    case "getCardsOnTable":
                        objectNode.put("command", a.getCommand());
                        ArrayNode tableRow = objectMapper.createArrayNode();
                        for (ArrayList<Minion> row: game.getBoard()) {
                            ArrayNode tableCol = objectMapper.createArrayNode();
                            for (Card card: row) {
                                    ObjectNode cardNode = objectMapper.createObjectNode();
                                    tableCol.add(card.printCardJSON(cardNode, objectMapper));
                            }
                            tableRow.add(tableCol);
                        }
                        objectNode.set("output", tableRow);
                        output.add(objectNode);
                        break;
                    case "getPlayerHero":
                        objectNode.put("command", a.getCommand());
                        objectNode.put("playerIdx", a.getPlayerIdx());
                        if (a.getPlayerIdx() == 1) {
                            playerOne.getHero().printCardJSON(objectNode, objectMapper);
                        } else if (a.getPlayerIdx() == 2) {
                            playerTwo.getHero().printCardJSON(objectNode, objectMapper);
                        }
                        output.add(objectNode);
                        break;
                    case "getCardAtPosition":
                        objectNode.put("command", a.getCommand());
                        objectNode.put("x", a.getX());
                        objectNode.put("y", a.getY());
                        if (game.getBoard().get(a.getX()).isEmpty() || game.getBoard().get(a.getX()).size() < a.getY()) {
                            objectNode.put("output", "No card available at that position.");
                            output.add(objectNode);
                        } else {
                            game.getBoard().get(a.getX()).get(a.getY()).printCardJSON(objectNode, objectMapper);
                            output.add(objectNode);
                        }
                        break;
                    case "getPlayerTurn":
                        objectNode.put("command", a.getCommand());
                        objectNode.put("output", game.getCurrentPlayerTurn());
                        output.add(objectNode);
                        break;
                    case "getPlayerMana":
                        objectNode.put("command", a.getCommand());
                        objectNode.put("playerIdx", a.getPlayerIdx());
                        if (a.getPlayerIdx() == 1) {
                            objectNode.put("output", playerOne.getMana());
                            output.add(objectNode);
                        } else if (a.getPlayerIdx() == 2) {
                            objectNode.put("output", playerTwo.getMana());
                            output.add(objectNode);
                        }
                        break;
                    case "getFrozenCardsOnTable":
                        objectNode.put("command", a.getCommand());
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
                        break;
                    case "getTotalGamesPlayed":
                        objectNode.put("command", a.getCommand());
                        objectNode.put("output", playerOne.getGamesPlayed());
                        output.add(objectNode);
                        break;
                    case "getPlayerOneWins":
                        objectNode.put("command", a.getCommand());
                        objectNode.put("output", playerOne.getGamesWon());
                        output.add(objectNode);
                        break;
                    case "getPlayerTwoWins":
                        objectNode.put("command", a.getCommand());
                        objectNode.put("output", playerTwo.getGamesWon());
                        output.add(objectNode);
                        break;
                }
            }
        }
        objectWriter.writeValue(new File(filePath2), output);
        System.out.println();
    }
}
