package Game;

import Cards.*;
import fileio.Coordinates;
import Player.Player;

import java.util.ArrayList;

public class Game {
    private int round;
    int manaPerRound;
    private int currentPlayerTurn;
    private int turnsThisRound;
    private ArrayList<ArrayList<Minion>> board;
    private Player playerOne;
    private Player playerTwo;

    public Player getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(Player playerOne) {
        this.playerOne = playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(Player playerTwo) {
        this.playerTwo = playerTwo;
    }

    public int getManaPerRound() {
        return manaPerRound;
    }

    public void setManaPerRound(int manaPerRound) {
        this.manaPerRound = manaPerRound;
    }

    public int getTurnsThisRound() {
        return turnsThisRound;
    }

    public void setTurnsThisRound(int turnsThisRound) {
        this.turnsThisRound = turnsThisRound;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getCurrentPlayerTurn() {
        return currentPlayerTurn;
    }

    public void setCurrentPlayerTurn(int currentPlayerTurn) {
        this.currentPlayerTurn = currentPlayerTurn;
    }

    public ArrayList<ArrayList<Minion>> getBoard() {
        return board;
    }

    public void setBoard(ArrayList<ArrayList<Minion>> board) {
        this.board = board;
    }

    public Game(int startingPlayerTurn, Player playerOne, Player playerTwo) {
        manaPerRound = 1;
        round = 1;
        turnsThisRound = 0;
        currentPlayerTurn = startingPlayerTurn;
        board = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            board.add(new ArrayList<>());
        }
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public boolean switchTurn() {
        if (currentPlayerTurn == 1) {
            for (Minion card : board.get(2)) {
                card.setFrozen(false);
                card.setAttackedThisTurn(false);
            }
            for (Minion card : board.get(3)) {
                card.setFrozen(false);
                card.setAttackedThisTurn(false);
            }
            currentPlayerTurn = 2;
        } else if (currentPlayerTurn == 2) {
            for (Minion card : board.get(0)) {
                card.setFrozen(false);
                card.setAttackedThisTurn(false);
            }
            for (Minion card : board.get(1)) {
                card.setFrozen(false);
                card.setAttackedThisTurn(false);
            }
            currentPlayerTurn = 1;
        }
        if (turnsThisRound == 1) {
            round++;
            turnsThisRound = 0;
            if (manaPerRound < 10) {
                manaPerRound++;
            }
            return true;
        } else {
            turnsThisRound++;
            return false;
        }
    }

    public boolean checkForTanks(int playerIdx) {
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

    public int useAttack(Coordinates cardAttacker, Coordinates cardAttacked) {
        if (getBoard().get(cardAttacker.getX()).size() <= cardAttacker.getY()) {
            return -1;
        }
        Minion attacker = getBoard().get(cardAttacker.getX()).get(cardAttacker.getY());
        if (getBoard().get(cardAttacked.getX()).size() <= cardAttacked.getY()) {
            return -1;
        }
        Minion attacked = getBoard().get(cardAttacked.getX()).get(cardAttacked.getY());
        if (attacker.getPlayerIdx() == attacked.getPlayerIdx()) {
            return 1;
        }
        if (attacker.hasAttackedThisTurn()) {
            return 2;
        }
        if (attacked.isFrozen()) {
            return 3;
        }
        if (checkForTanks(attacker.getPlayerIdx()) && !(attacked instanceof Tank)) {
            return 4;
        }
        attacker.attack(attacked);
        if (attacked.getHealth() <= 0) {
            getBoard().get(cardAttacked.getX()).remove(attacked);
        }
        return 0;
    }

    public int useAbility(Coordinates cardAttacker, Coordinates cardAttacked) {
        if (getBoard().get(cardAttacker.getX()).size() <= cardAttacker.getY()) {
            return -1;
        }
        SpecialMinion attacker = (SpecialMinion) getBoard().get(cardAttacker.getX()).get(cardAttacker.getY());
        if (getBoard().get(cardAttacked.getX()).size() <= cardAttacked.getY()) {
            return -1;
        }
        Minion attacked = getBoard().get(cardAttacked.getX()).get(cardAttacked.getY());
        if (attacker.isFrozen()) {
            return 1;
        }
        if (attacker.hasAttackedThisTurn()) {
            return 2;
        }
        if (attacker instanceof Disciple) {
            if (attacker.getPlayerIdx() != attacked.getPlayerIdx()) {
                return 3;
            }
        } else {
            if (attacker.getPlayerIdx() == attacked.getPlayerIdx()) {
                return 4;
            }
            if (checkForTanks(attacker.getPlayerIdx()) && !(attacked instanceof Tank)) {
                return 5;
            }
        }
        attacker.ability(attacked);
        attacker.setAttackedThisTurn(true);
        if (attacked.getHealth() <= 0) {
            getBoard().get(cardAttacked.getX()).remove(attacked);
        }
        return 0;
    }

    public int attackHero(Coordinates cardAttacker) {
        if (getBoard().get(cardAttacker.getX()).size() <= cardAttacker.getY()) {
            return -1;
        }
        Minion attacker = getBoard().get(cardAttacker.getX()).get(cardAttacker.getY());
        Hero hero = null;
        if (currentPlayerTurn == 1) {
            hero = playerTwo.getHero();
        } else if (currentPlayerTurn == 2) {
            hero = playerOne.getHero();
        }
        if (attacker.isFrozen()) {
            return 1;
        }
        if (attacker.hasAttackedThisTurn()) {
            return 2;
        }
        if (checkForTanks(attacker.getPlayerIdx())) {
            int x = 0;
            if (currentPlayerTurn == 1) {
                x = 1;
            } else if (currentPlayerTurn == 2) {
                x = 2;
            }
            for (Minion card : getBoard().get(x)) {
                if (card instanceof Tank) {
                    return 3;
                }
            }
        }
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
}
