package it.polimi.ingsw.cards;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class Artemis extends Card {

    public Artemis()
    {
        super(CardName.ARTEMIS,false,false,true, Status.CHOSEN);
    }

    @Override
    public List<Cell> checkMove(List<Player> p, Board b){
        List<Cell> available = new ArrayList<>();

        // TODO: check generici
        available.add(new Cell(0,0,0));

        return available;
    }
}
