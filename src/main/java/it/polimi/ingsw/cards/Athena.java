package it.polimi.ingsw.cards;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Status;
import it.polimi.ingsw.model.Worker;

import java.util.List;

public class Athena extends Card {
    public Athena(CardName name) {
        super(name);
    }

    @Override
    public List<Cell> getBlocked(Worker w, Board b, Status status) {
        return super.getBlocked(w, b, status);
    }

}
