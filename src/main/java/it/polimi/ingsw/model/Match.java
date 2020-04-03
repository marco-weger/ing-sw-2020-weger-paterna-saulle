package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Match {

    /**
     * Unique id to discriminate the match
     */
    private int id;

    /**
     * The board
     */
    private Board board;

    /**
     * List of players (2 or 3)
     */
    private ArrayList<Player> players;

    /**
     * Status of the match
     */
    private Status status;

    private boolean ended;

    public Match(int id, Board board, ArrayList<Player> players,boolean ended, Status status){
        this.id=id;
        this.board=board;
        this.players=players;
        this.ended=ended;
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

    public ArrayList<Player> getPlayers() {
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

    /**
     * Current player getter
     */
    public Player getCurrentPlayer(){
        for(Player p:players)
            if(p.isActive())
                return p;
        return null;
    }

    /**
     * A method to set next player
     */
    public void setNextPlayer() {
        if(getCurrentPlayer() != null){
            int i = players.indexOf(getCurrentPlayer());
            getCurrentPlayer().setActive(false);
            if (i < players.size()-1) {
                players.get(i + 1).setActive(true);
            } else {
                players.get(0).setActive(true);
            }
        }
        else{
            players.get(0).setActive(true);
        }
    }

    /**
     * @return It checks if current player doesn't have move, and update workers status
     */
    /*public boolean checkCurrentPlayerLose() {
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
    */

    /**
     * @return It verifies if the current player win for other players defeat
     */
    public boolean checkCurrentPlayerWin() {
        int deads = 0;
        int k = (players.size() - 1);
        for (Player player : players) {
            if (getCurrentPlayer() != player) {
                if (!player.getWorker1().isActive() && !player.getWorker2().isActive()) {
                    deads++;
                }
            }
        }
        return (deads == k);
    }
}

