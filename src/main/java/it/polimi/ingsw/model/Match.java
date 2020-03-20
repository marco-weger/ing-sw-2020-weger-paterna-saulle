package it.polimi.ingsw.model;

import java.util.List;
import java.util.Observable;

public class Match extends Observable implements Cloneable {

    private int id;
    private Board board;
    private List<Player> players;
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

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Board getBoard() {
        return board;
    }

// public void inizializeMatch (int n);

   // public void EndMatch();

   // public void EndTurn();

    /* a method to update the current player*/
    //TODO: Test on this method
    public void setNextPlayer(){

        int i = players.indexOf(currentPlayer);

        if (i < players.size() )
        {
            currentPlayer = players.get(i + 1);
        }
        else {
            currentPlayer = players.get(0);
        }
    }

   // public booelan checkCurrentPlayerLose();
    //TODO: Fix check on Worker1 and Worker 2
    public boolean checkCurrentPlayerWin() {
        int j;
        int deads = 0;
        int k = players.size() - 1;
        for (j = 0; j < players.size();j++ ){
        if (currentPlayer == players.get(j)) {
            j++;
        } else {
            if (players.get(j).getCurrentWorker().isActive() == false && players.get(j).getCurrentWorker().isActive() == false) {
                deads++;
            }
        }
    }
        if (deads == k) return true;
        else return false;
   };
}


