package main;

import Cards.Card;
import Cards.Hero;
import Deck.Decks;
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

        Player playerOne = new Player(inputData.getPlayerOneDecks());
        Player playerTwo = new Player(inputData.getPlayerTwoDecks());
        ArrayList<GameInput> gameInput = inputData.getGames();

        for (GameInput g : gameInput) {
            ArrayList<ActionsInput> actionsInput = g.getActions();

            playerOne.setHero(new Hero(g.getStartGame().getPlayerOneHero()));
            playerTwo.setHero(new Hero(g.getStartGame().getPlayerTwoHero()));

            playerOne.setCurrentDeck(playerOne.getDecks().getDecks().get(g.getStartGame().getPlayerOneDeckIdx()));
            playerTwo.setCurrentDeck(playerTwo.getDecks().getDecks().get(g.getStartGame().getPlayerTwoDeckIdx()));

            int currentPlayer = g.getStartGame().getStartingPlayer();

            Random random = new Random(g.getStartGame().getShuffleSeed());
            shuffle(playerOne.getCurrentDeck(), random);
            random = new Random(g.getStartGame().getShuffleSeed());
            shuffle(playerTwo.getCurrentDeck(), random);

            playerOne.drawCard();
            playerTwo.drawCard();

            for (ActionsInput a : actionsInput) {
                ObjectNode objectNode = objectMapper.createObjectNode();
                objectNode.put("command", a.getCommand());
                switch (a.getCommand()) {
                    case "getPlayerDeck":
                        objectNode.put("playerIdx", a.getPlayerIdx());
                        if (a.getPlayerIdx() == 1) {
                            playerOne.printCurrentJSON(objectMapper, objectNode, playerOne.getCurrentDeck());
                        } else if (a.getPlayerIdx() == 2) {
                            playerTwo.printCurrentJSON(objectMapper, objectNode, playerTwo.getCurrentDeck());
                        }
                        break;
                    case "getCardsInHand":
                        objectNode.put("playerIdx", a.getPlayerIdx());
                        if (a.getPlayerIdx() == 1) {
                            playerOne.printCurrentJSON(objectMapper, objectNode, playerOne.getHand());
                        } else if (a.getPlayerIdx() == 2) {
                            playerTwo.printCurrentJSON(objectMapper, objectNode, playerTwo.getHand());
                        }
                        break;
                    case "getPlayerHero":
                        objectNode.put("playerIdx", a.getPlayerIdx());
                        if (a.getPlayerIdx() == 1) {
                            playerOne.getHero().printHeroJSON(objectNode, objectMapper);
                        } else if (a.getPlayerIdx() == 2) {
                            playerTwo.getHero().printHeroJSON(objectNode, objectMapper);
                        }
                        break;
                    case "getPlayerTurn":
                        objectNode.put("output", currentPlayer);
                        break;
                    case "endTurn":
                        break;
                    case "placeCard":
                        break;
                }
                output.add(objectNode);
            }
        }
        objectWriter.writeValue(new File(filePath2), output);
    }
}
