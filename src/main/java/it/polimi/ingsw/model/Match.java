package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Match extends Observable implements Cloneable {

    private int id;
    private Board board;
    private static List<Player> players;
    private boolean IsEnded;
    private Player currentPlayer;
    private Status status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEnded() {
        return IsEnded;
    }

    public void setEnded(boolean ended) {
        IsEnded = ended;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public static List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    // public void inizializeMatch (int n);

    // public void EndMatch();

    // public void EndTurn();

    /**
     * a method to set next player
     */
    public void setNextPlayer() {
        int i = players.indexOf(currentPlayer);
        if (i < players.size()-1) {
            currentPlayer = players.get(i + 1);
        } else {
            currentPlayer = players.get(0);
        }
    }

    /**
     *@return check if currentplayer doesn't have move, and update workers status
     */
    public boolean checkCurrentPlayerLose() {
        List<Cell> empty = new ArrayList<>();
        currentPlayer.setCurrentWorker(1);
        if (currentPlayer.getCard().checkMove(players, board).equals(empty)) {
            currentPlayer.getWorker1().setActive(false);
            currentPlayer.setCurrentWorker(2);
            if (currentPlayer.getCard().checkMove(players, board).equals(empty)) {
                currentPlayer.getWorker2().setActive(false);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    /**
     * @return Verify if the current player win for other players defeat
     */
    public boolean checkCurrentPlayerWin() {
        int deads = 0;
        int k = (players.size() - 1);
        for (Player player : players) {
            if (currentPlayer != player) {
                if (!player.getWorker1().isActive() && !player.getWorker2().isActive()) {
                    deads++;
                }
            }
        }
        return (deads == k);
    }
}

