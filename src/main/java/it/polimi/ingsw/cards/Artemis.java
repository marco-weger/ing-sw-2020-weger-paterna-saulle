package it.polimi.ingsw.cards;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;

import java.util.List;

public class Artemis extends Card {
    public Artemis(CardName name) {
        super(name);
    }

    @Override
    public List<Cell> checkMove(Worker w, Board b) {
        return super.checkMove(w, b);
    }
}
