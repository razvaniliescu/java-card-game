package game;

import cards.*;
import cards.heroes.EmpressThorina;
import cards.heroes.GeneralKocioraw;
import cards.heroes.KingMudface;
import cards.heroes.LordRoyce;
import fileio.CardInput;
import fileio.Coordinates;
import static constants.Constants.*;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Handles game elements
 */
public class Game {
    private int manaPerRound;
    private int currentPlayerTurn;
    private int turnsThisRound;
    private final ArrayList<ArrayList<Minion>> board;
    private final Player playerOne;
    private final Player playerTwo;
    /**
     * Gets the board
     */
    public ArrayList<ArrayList<Minion>> getBoard() {
        return board;
    }

    /**
     * Gets the current mana increase per round
     */
    public int getManaPerRound() {
        return manaPerRound;
    }

    /**
     * Gets the index of the current player
     */
    public int getCurrentPlayerTurn() {
        return currentPlayerTurn;
    }

    /**
     * Gets the number of turns this round
     */
    public int getTurnsThisRound() {
        return turnsThisRound;
    }

    /**
     * Gets player one
     */
    public Player getPlayerOne() {
        return playerOne;
    }

    /**
     * Gets player two
     */
    public Player getPlayerTwo() {
        return playerTwo;
    }

    public Game(final int startingPlayerTurn, final Player playerOne, final Player playerTwo) {
        manaPerRound = 1;
        turnsThisRound = 0;
        currentPlayerTurn = startingPlayerTurn;
        board = new ArrayList<>();
        for (int i = 0; i < BOARD_ROWS; i++) {
            board.add(new ArrayList<>());
        }
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    /**
     * Initializes a player's hero depending on its name
     */
    public void initHero(final CardInput hero, final Player player) {
        Hero playerHero = switch (hero.getName()) {
            case "Lord Royce" -> new LordRoyce(hero, player.getPlayerIdx());
            case "Empress Thorina" -> new EmpressThorina(hero, player.getPlayerIdx());
            case "King Mudface" -> new KingMudface(hero, player.getPlayerIdx());
            case "General Kocioraw" -> new GeneralKocioraw(hero, player.getPlayerIdx());
            default -> null;
        };
        player.setHero(playerHero);
    }

    /**
     * Advances the game to the next turn
     */
    public boolean switchTurn() {
        if (currentPlayerTurn == 1) {
            for (Minion card : board.get(THIRD_ROW_INDEX)) {
                card.setFrozen(false);
                card.setAttackedThisTurn(false);
            }
            for (Minion card : board.get(FOURTH_ROW_INDEX)) {
                card.setFrozen(false);
                card.setAttackedThisTurn(false);
            }
            playerOne.getHero().setAttackedThisTurn(false);
            currentPlayerTurn = 2;
        } else if (currentPlayerTurn == 2) {
            for (Minion card : board.getFirst()) {
                card.setFrozen(false);
                card.setAttackedThisTurn(false);
            }
            for (Minion card : board.get(SECOND_ROW_INDEX)) {
                card.setFrozen(false);
                card.setAttackedThisTurn(false);
            }
            playerTwo.getHero().setAttackedThisTurn(false);
            currentPlayerTurn = 1;
        }
        if (turnsThisRound == 1) {
            turnsThisRound = 0;
            if (manaPerRound < MAX_MANA_PER_ROUND) {
                manaPerRound++;
            }
            return true;
        } else {
            turnsThisRound++;
            return false;
        }
    }

    /**
     * Checks if the enemy's side of the board has any tanks
     */
    public boolean checkForTanks(final int playerIdx) {
        if (playerIdx == 1) {
            for (Minion card : board.get(SECOND_ROW_INDEX)) {
                if (Objects.equals(card.getName(), "Goliath")
                        || Objects.equals(card.getName(), "Warden")) {
                    return true;
                }
            }
        } else if (playerIdx == 2) {
            for (Minion card : board.get(THIRD_ROW_INDEX)) {
                if (Objects.equals(card.getName(), "Goliath")
                        || Objects.equals(card.getName(), "Warden")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Game logic for the attack action
     * @param cardAttacker coordinates of the attacker
     * @param cardAttacked coordinates of the attacked
     */
    public int useAttack(final Coordinates cardAttacker, final Coordinates cardAttacked) {
        Minion attacker = board.get(cardAttacker.getX()).get(cardAttacker.getY());
        Minion attacked = board.get(cardAttacked.getX()).get(cardAttacked.getY());
        if (attacked.isFrozen()) {
            return CARD_IS_FROZEN;
        }
        if (attacker.getPlayerIdx() == attacked.getPlayerIdx()) {
            return ATTACKED_CARD_IS_FRIENDLY;
        }
        if (attacker.isAttackedThisTurn()) {
            return CARD_ALREADY_ATTACKED;
        }
        if (checkForTanks(attacker.getPlayerIdx())
                && !(Objects.equals(attacked.getName(), "Goliath")
                || Objects.equals(attacked.getName(), "Warden"))) {
            return ATTACKED_CARD_IS_NOT_TANK;
        }
        attacker.attack(attacked);
        if (attacked.getHealth() <= 0) {
            board.get(cardAttacked.getX()).remove(attacked);
        }
        return 0;
    }

    /**
     * Game logic for the minion ability action
     * @param cardAttacker coordinates of the ability user
     * @param cardAttacked coordinates of the affected minion
     */
    public int useAbility(final Coordinates cardAttacker, final Coordinates cardAttacked) {
        SpecialMinion attacker = (SpecialMinion) board.get(cardAttacker.getX()).get(cardAttacker.getY());
        Minion attacked = board.get(cardAttacked.getX()).get(cardAttacked.getY());
        if (attacker.isFrozen()) {
            return CARD_IS_FROZEN;
        }
        if (attacker.isAttackedThisTurn()) {
            return CARD_ALREADY_ATTACKED;
        }
        if (Objects.equals(attacker.getName(), "Disciple")) {
            if (attacker.getPlayerIdx() != attacked.getPlayerIdx()) {
                return ATTACKED_CARD_IS_NOT_FRIENDLY;
            }
        } else {
            if (attacker.getPlayerIdx() == attacked.getPlayerIdx()) {
                return ATTACKED_CARD_IS_FRIENDLY;
            }
            if (checkForTanks(attacker.getPlayerIdx())
                    && !(Objects.equals(attacked.getName(), "Goliath")
                    || Objects.equals(attacked.getName(), "Warden"))) {
                return ATTACKED_CARD_IS_NOT_TANK;
            }
        }
        attacker.ability(attacked);
        attacker.setAttackedThisTurn(true);
        if (attacked.getHealth() <= 0) {
            board.get(cardAttacked.getX()).remove(attacked);
        }
        return 0;
    }

    /**
     * Game logic for the attack hero action
     * @param cardAttacker coordinates of the attacker
     */
    public int attackHero(final Coordinates cardAttacker) {
        Minion attacker = board.get(cardAttacker.getX()).get(cardAttacker.getY());
        Hero hero = switch (currentPlayerTurn) {
            case 1 -> playerTwo.getHero();
            case 2 -> playerOne.getHero();
            default -> null;
        };
        if (attacker.isFrozen()) {
            return CARD_IS_FROZEN;
        }
        if (attacker.isAttackedThisTurn()) {
            return CARD_ALREADY_ATTACKED;
        }
        if (checkForTanks(attacker.getPlayerIdx())) {
            return ATTACKED_CARD_IS_NOT_TANK;
        }
        assert hero != null;
        attacker.attack(hero);
        if (hero.getHealth() <= 0) {
            if (currentPlayerTurn == 1) {
                playerOne.win();
                playerTwo.lose();
            } else if (currentPlayerTurn == 2) {
                playerOne.lose();
                playerTwo.win();
            }
        }
        return 0;
    }

    /**
     * Game logic for the hero ability action
     * @param row selected row
     */
    public int useHeroAbility(final int row) {
        Player player = switch (currentPlayerTurn) {
            case 1 -> playerOne;
            case 2 -> playerTwo;
            default -> null;
        };
        assert player != null;
        Hero hero = player.getHero();
        if (player.getMana() < hero.getMana()) {
            return NOT_ENOUGH_MANA;
        }
        if (hero.isAttackedThisTurn()) {
            return HERO_ALREADY_USED_ABILITY;
        }
        if ((Objects.equals(hero.getName(), "Lord Royce"))
                || (Objects.equals(hero.getName(), "Empress Thorina"))) {
            if (currentPlayerTurn == 1) {
                if (row == THIRD_ROW_INDEX || row == FOURTH_ROW_INDEX) {
                    return SELECTED_ROW_IS_FRIENDLY;
                }
            } else if (currentPlayerTurn == 2) {
                if (row == FIRST_ROW_INDEX || row == SECOND_ROW_INDEX) {
                    return SELECTED_ROW_IS_FRIENDLY;
                }
            }
        } else if ((Objects.equals(hero.getName(), "King Mudface"))
                || (Objects.equals(hero.getName(), "General Kocioraw"))) {
            if (currentPlayerTurn == 1) {
                if (row == FIRST_ROW_INDEX || row == SECOND_ROW_INDEX) {
                    return SELECTED_ROW_IS_NOT_FRIENDLY;
                }
            } else if (currentPlayerTurn == 2) {
                if (row == THIRD_ROW_INDEX || row == FOURTH_ROW_INDEX) {
                    return SELECTED_ROW_IS_NOT_FRIENDLY;
                }
            }
        }
        hero.ability(board.get(row));
        player.setMana(player.getMana() - hero.getMana());
        hero.setAttackedThisTurn(true);
        return 0;
    }
}
