package game;

import cards.*;
import cards.heroes.EmpressThorina;
import cards.heroes.GeneralKocioraw;
import cards.heroes.KingMudface;
import cards.heroes.LordRoyce;
import cards.specialMinions.Disciple;
import fileio.CardInput;
import fileio.Coordinates;

import java.util.ArrayList;

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
        final int rows = 4;
        for (int i = 0; i < rows; i++) {
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
        final int secondRowIndex = 1;
        final int thirdRowIndex = 2;
        final int fourthRowIndex = 3;
        final int maxMana = 10;
        if (currentPlayerTurn == 1) {
            for (Minion card : board.get(thirdRowIndex)) {
                card.setFrozen(false);
                card.setAttackedThisTurn(false);
            }
            for (Minion card : board.get(fourthRowIndex)) {
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
            for (Minion card : board.get(secondRowIndex)) {
                card.setFrozen(false);
                card.setAttackedThisTurn(false);
            }
            playerTwo.getHero().setAttackedThisTurn(false);
            currentPlayerTurn = 1;
        }
        if (turnsThisRound == 1) {
            turnsThisRound = 0;
            if (manaPerRound < maxMana) {
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
            for (Minion card : board.get(1)) {
                if (card instanceof Tank) {
                    return true;
                }
            }
        } else if (playerIdx == 2) {
            for (Minion card : board.get(2)) {
                if (card instanceof Tank) {
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
        final int attackedCardIsFriendly = 1;
        final int cardAlreadyAttacked = 2;
        final int cardIsFrozen = 3;
        final int attackedCardIsNotTank = 4;
        if (attacker.getPlayerIdx() == attacked.getPlayerIdx()) {
            return attackedCardIsFriendly;
        }
        if (attacker.isAttackedThisTurn()) {
            return cardAlreadyAttacked;
        }
        if (attacked.isFrozen()) {
            return cardIsFrozen;
        }
        if (checkForTanks(attacker.getPlayerIdx()) && !(attacked instanceof Tank)) {
            return attackedCardIsNotTank;
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
        final int cardIsFrozen = 1;
        final int cardAlreadyAttacked = 2;
        final int attackedCardIsNotFriendly = 3;
        final int attackedCardIsFriendly = 4;
        final int attackedCardIsNotTank = 5;
        if (attacker.isFrozen()) {
            return cardIsFrozen;
        }
        if (attacker.isAttackedThisTurn()) {
            return cardAlreadyAttacked;
        }
        if (attacker instanceof Disciple) {
            if (attacker.getPlayerIdx() != attacked.getPlayerIdx()) {
                return attackedCardIsNotFriendly;
            }
        } else {
            if (attacker.getPlayerIdx() == attacked.getPlayerIdx()) {
                return attackedCardIsFriendly;
            }
            if (checkForTanks(attacker.getPlayerIdx()) && !(attacked instanceof Tank)) {
                return attackedCardIsNotTank;
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
        final int cardIsFrozen = 1;
        final int cardAlreadyAttacked = 2;
        final int mustAttackTankFirst = 3;
        if (attacker.isFrozen()) {
            return cardIsFrozen;
        }
        if (attacker.isAttackedThisTurn()) {
            return cardAlreadyAttacked;
        }
        if (checkForTanks(attacker.getPlayerIdx())) {
            return mustAttackTankFirst;
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

        final int firstRowIndex = 0;
        final int secondRowIndex = 1;
        final int thirdRowIndex = 2;
        final int fourthRowIndex = 3;

        final int notEnoughMana = 1;
        final int heroAlreadyUsedAbility = 2;
        final int selectedRowIsFriendly = 3;
        final int selectedRowIsNotFriendly = 4;
        if (player.getMana() < hero.getMana()) {
            return notEnoughMana;
        }
        if (hero.isAttackedThisTurn()) {
            return heroAlreadyUsedAbility;
        }
        if ((hero instanceof LordRoyce) || (hero instanceof EmpressThorina)) {
            if (currentPlayerTurn == 1) {
                if (row == thirdRowIndex || row == fourthRowIndex) {
                    return selectedRowIsFriendly;
                }
            } else if (currentPlayerTurn == 2) {
                if (row == firstRowIndex || row == secondRowIndex) {
                    return selectedRowIsFriendly;
                }
            }
        } else if ((hero instanceof KingMudface) || (hero instanceof GeneralKocioraw)) {
            if (currentPlayerTurn == 1) {
                if (row == firstRowIndex || row == secondRowIndex) {
                    return selectedRowIsNotFriendly;
                }
            } else if (currentPlayerTurn == 2) {
                if (row == thirdRowIndex || row == fourthRowIndex) {
                    return selectedRowIsNotFriendly;
                }
            }
        }
        hero.ability(board.get(row));
        player.setMana(player.getMana() - hero.getMana());
        hero.setAttackedThisTurn(true);
        return 0;
    }
}
