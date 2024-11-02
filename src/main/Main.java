package main;

import Cards.Card;
import Cards.Hero;
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
            Game game = new Game(g.getStartGame().getStartingPlayer());

            playerOne.setHero(new Hero(g.getStartGame().getPlayerOneHero()));
            playerTwo.setHero(new Hero(g.getStartGame().getPlayerTwoHero()));

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
                        int error = 0;
                        if (game.getCurrentPlayerTurn() == 1) {
                            error = playerOne.placeCard(a.getHandIdx(), game);
                        } else {
                            error = playerTwo.placeCard(a.getHandIdx(), game);
                        }
                        if (error == 1) {
                            objectNode.put("command", a.getCommand());
                            objectNode.put("handIdx", a.getHandIdx());
                            objectNode.put("output", "Not enough mana to place card on table.");
                            output.add(objectNode);
                        } else if (error == 2) {
                            objectNode.put("command", a.getCommand());
                            objectNode.put("handIdx", a.getHandIdx());
                            objectNode.put("output", "Cannot place card on table since row is full.");
                            output.add(objectNode);
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
                        for (ArrayList<Card> row: game.getBoard()) {
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
//                    case "getCardAtPosition":
//                        objectNode.put("command", a.getCommand());
//                        objectNode.put("x", a.getX());
//                        objectNode.put("y", a.getY());
//                        if (game.getBoard().get(a.getX()).size() < a.getY()) {
//                            objectNode.put("output", "No card available at that position");
//                        } else {
//                            game.getBoard().get(a.getX()).get(a.getY()).printCardJSON(objectNode, objectMapper);
//                        }
//                        break;
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
                        for (ArrayList<Card> row: game.getBoard()) {
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
