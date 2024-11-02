package Game;

import Cards.Card;
import Player.Player;

import java.util.ArrayList;

public class Game {
    private int round;
    int manaPerRound;
    private int currentPlayerTurn;
    private int turnsThisRound;
    private ArrayList<ArrayList<Card>> board;

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

    public ArrayList<ArrayList<Card>> getBoard() {
        return board;
    }

    public void setBoard(ArrayList<ArrayList<Card>> board) {
        this.board = board;
    }

    public Game(int startingPlayerTurn) {
        manaPerRound = 1;
        round = 1;
        turnsThisRound = 0;
        currentPlayerTurn = startingPlayerTurn;
        board = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            board.add(new ArrayList<>());
        }
    }

    public boolean switchTurn() {
        if (currentPlayerTurn == 1) {
            for (Card card : board.get(2)) {
                card.setFrozen(false);
            }
            for (Card card : board.get(3)) {
                card.setFrozen(false);
            }
            currentPlayerTurn = 2;
        } else if (currentPlayerTurn == 2) {
            for (Card card : board.get(0)) {
                card.setFrozen(false);
            }
            for (Card card : board.get(1)) {
                card.setFrozen(false);
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

    public void PlayerOneWin(Player playerOne, Player playerTwo) {
        playerOne.setGamesPlayed(playerOne.getGamesPlayed() + 1);
        playerTwo.setGamesPlayed(playerTwo.getGamesPlayed() + 1);
        playerOne.setGamesWon(playerOne.getGamesWon() + 1);
    }

    public void PlayerTwoWin(Player playerOne, Player playerTwo) {
        playerOne.setGamesPlayed(playerOne.getGamesPlayed() + 1);
        playerTwo.setGamesPlayed(playerTwo.getGamesPlayed() + 1);
        playerOne.setGamesWon(playerOne.getGamesWon() + 1);
    }
}
