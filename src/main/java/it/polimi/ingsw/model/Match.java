package it.polimi.ingsw.model;

import it.polimi.ingsw.Observable;

import java.util.ArrayList;
import java.util.List;

public class Match extends Observable implements Cloneable {

    private int id;
    private Board board;
    private ArrayList<Player> players;
    private boolean ended;
    private Player currentPlayer;
    private Status status;

    public Match(int id, Board board, ArrayList<Player> players,boolean ended, Player currentPlayer, Status status){
        this.id=id;
        this.board=board;
        this.players=players;
        this.ended=ended;
        this.currentPlayer=currentPlayer;
        this.status=status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
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

    /**
     * A method to set next player
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
     * @return It checks if current player doesn't have move, and update workers status
     */
    public boolean checkCurrentPlayerLose() {
        currentPlayer.setCurrentWorker(1);
        if (currentPlayer.getCard().checkMove(players, board).size() == 0) {
            currentPlayer.setCurrentWorker(2);
            if (currentPlayer.getCard().checkMove(players, board).size() == 0) {
                currentPlayer.setCurrentWorker(0);
                return true;
            } else {
                currentPlayer.setCurrentWorker(0);
                return false;
            }
        } else {
            currentPlayer.setCurrentWorker(0);
            return false;
        }
    }


    /**
     * @return It verifies if the current player win for other players defeat
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

    @Override
    public void notifyObservers(Object obj) {
        super.notifyObservers(this.clone());
    }

    @Override
    public Match clone(){
        try {
            Match m = (Match)super.clone();
            m.players = new ArrayList<>();
            for(Player p : this.players)
                m.players.add(p.clone());
            m.board = this.board.clone();
            m.currentPlayer = this.currentPlayer.clone();
            return m;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}

