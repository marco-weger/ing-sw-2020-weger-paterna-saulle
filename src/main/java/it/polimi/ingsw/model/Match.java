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

   // public void inizializeMatch (int n);

   // public void EndMatch();

   // public void EndTurn();

   // public void setNextPlayer();
}


