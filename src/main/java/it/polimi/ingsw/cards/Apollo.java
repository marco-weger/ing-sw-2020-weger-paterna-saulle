package it.polimi.ingsw.cards;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;

import java.util.List;

public class Apollo extends Card {

    public Apollo() {
        super(CardName.APOLLO);
    }

    @Override
    public List<Cell> checkMove(Worker w, Board b)
    {
        List<Cell> available = super.checkMove(w,b);

        // TODO: altri check
        available.add(new Cell(0,0,0));

        return available;
    }

}
