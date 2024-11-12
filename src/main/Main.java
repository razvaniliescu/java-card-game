package main;

import deck.Deck;
import game.Game;
import game.Player;
import output.Debug;
import output.Error;
import checker.Checker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        ArrayNode output = objectMapper.createArrayNode();

        DecksInput playerOneDecks = inputData.getPlayerOneDecks();
        DecksInput playerTwoDecks = inputData.getPlayerTwoDecks();

        Player playerOne = new Player(playerOneDecks, 1);
        Player playerTwo = new Player(playerTwoDecks, 2);
        ArrayList<GameInput> gameInput = inputData.getGames();

        for (GameInput g : gameInput) {
            ArrayList<ActionsInput> actionsInput = g.getActions();
            Game game = new Game(g.getStartGame().getStartingPlayer(), playerOne, playerTwo);

            game.initHero(g.getStartGame().getPlayerOneHero(), playerOne);
            game.initHero(g.getStartGame().getPlayerTwoHero(), playerTwo);

            playerOne.setMana(1);
            playerTwo.setMana(1);

            playerOne.getHand().getCards().clear();
            playerTwo.getHand().getCards().clear();

            int playerOneDeckIndex = g.getStartGame().getPlayerOneDeckIdx();
            int playerTwoDeckIndex = g.getStartGame().getPlayerTwoDeckIdx();

            playerOne.setCurrentDeck(new Deck(playerOneDecks.getDecks().get(playerOneDeckIndex), 1));
            playerTwo.setCurrentDeck(new Deck(playerTwoDecks.getDecks().get(playerTwoDeckIndex), 2));

            Random random = new Random(g.getStartGame().getShuffleSeed());
            shuffle(playerOne.getCurrentDeck().getCards(), random);
            random = new Random(g.getStartGame().getShuffleSeed());
            shuffle(playerTwo.getCurrentDeck().getCards(), random);

            playerOne.drawCard();
            playerTwo.drawCard();

            for (ActionsInput a : actionsInput) {
                ObjectNode objectNode = objectMapper.createObjectNode();
                Error errorOutput = new Error(output, objectMapper, objectNode);
                Debug debugOutput = new Debug(output, objectMapper, objectNode, game);
                int error;
                Coordinates cardAttacker;
                Coordinates cardAttacked;
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
                            errorOutput.placeCardError(error, a.getHandIdx());
                        }
                        break;
                    case "cardUsesAttack":
                        cardAttacker = a.getCardAttacker();
                        cardAttacked = a.getCardAttacked();
                        error = game.useAttack(cardAttacker, cardAttacked);
                        if (error != 0) {
                            errorOutput.cardUsesAttackError(error, cardAttacker, cardAttacked);
                        }
                        break;
                    case "cardUsesAbility":
                        cardAttacker = a.getCardAttacker();
                        cardAttacked = a.getCardAttacked();
                        error = game.useAbility(cardAttacker, cardAttacked);
                        if (error != 0) {
                            errorOutput.cardUsesAbilityError(error, cardAttacker, cardAttacked);
                        }
                        break;
                    case "useAttackHero":
                        cardAttacker = a.getCardAttacker();
                        error = game.attackHero(cardAttacker);
                        if (error != 0) {
                            errorOutput.useAttackHeroError(error, cardAttacker);
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
                    case "useHeroAbility":
                        error = game.useHeroAbility(a.getAffectedRow());
                        if (error != 0) {
                            errorOutput.useHeroAbilityError(error, a.getAffectedRow());
                        }
                        break;
                    case "getPlayerDeck":
                        debugOutput.getPlayerDeck(a.getPlayerIdx());
                        break;
                    case "getCardsInHand":
                        debugOutput.getCardsInHand(a.getPlayerIdx());
                        break;
                    case "getCardsOnTable":
                        debugOutput.getCardsOnTable();
                        break;
                    case "getPlayerHero":
                        debugOutput.getPlayerHero(a.getPlayerIdx());
                        break;
                    case "getCardAtPosition":
                        debugOutput.getCardAtPosition(a.getX(), a.getY());
                        break;
                    case "getPlayerTurn":
                        debugOutput.getPlayerTurn();
                        break;
                    case "getPlayerMana":
                        debugOutput.getPlayerMana(a.getPlayerIdx());
                        break;
                    case "getFrozenCardsOnTable":
                        debugOutput.getFrozenCardsOnTable();
                        break;
                    case "getTotalGamesPlayed":
                        debugOutput.getTotalGamesPlayed();
                        break;
                    case "getPlayerOneWins":
                        debugOutput.getPlayerOneWins();
                        break;
                    case "getPlayerTwoWins":
                        debugOutput.getPlayerTwoWins();
                        break;
                    default:
                        break;
                }
            }
        }
        objectWriter.writeValue(new File(filePath2), output);
    }
}
