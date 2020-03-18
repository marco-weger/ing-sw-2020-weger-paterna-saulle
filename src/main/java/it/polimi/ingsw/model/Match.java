package it.polimi.ingsw.model;

import java.util.Observable;

public class Match extends Observable implements Cloneable {

    private int id;
    //board
    //players
    private boolean IsEnded;
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

   // public int inizializeMatch ();

   // public void EndMatch();

   // public void EndTurn();

   // public void setNextPlayer();
}


