package it.polimi.ingsw.cards;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;

import java.util.List;

public class Hepaestus extends Card {

    public Hepaestus(CardName name) {
        super(name);
    }

    @Override
    public List<Cell> checkBuild(Worker w, Board b) {
        return super.checkBuild(w, b);
    }
}

