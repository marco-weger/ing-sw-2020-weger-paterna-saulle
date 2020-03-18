package it.polimi.ingsw.model;

import java.util.List;

public class Athena extends Card{
    public Athena(CardName name) {
        super(name);
    }

    @Override
    public List<Cell> getBlocked(Worker w, Board b, Status status) {
        return super.getBlocked(w, b, status);
    }

}
