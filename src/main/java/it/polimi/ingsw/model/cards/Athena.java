package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class Athena extends Card {

    public Athena()
    {
        super(CardName.ATHENA,false,false,true,Status.CHOSEN);
    }
    /**
     * Restituisce al giocatore un arraylist con le mosse proibite
     */
    @Override
    public List<Cell> activeBlock(List<Player> p, Board b, Worker w,  Status current) {
        if(p == null || b == null ) return new ArrayList<>(0);
        List<Cell> ret = new ArrayList<>();
        if(current != Status.QUESTION_M)  return ret;
        else{
            for (Cell c : b.getField())
                if (Math.abs(c.getRow() - w.getRow()) <= 1 && Math.abs(c.getColumn() - w.getColumn()) <= 1 && c.getLevel() < 4 && c.getLevel() == w.getLevel(b) + 1 && !c.isOccupied(p))
                    ret.add(c);
            return ret;
        }
    }

    /**
     * Resetta il parametro isActive di Atena, se all'inizio del turno il giocatore
     * ha il parametro a 1 (finito il round, finito il potere)
     */
    @Override
    public void inizializeTurn() {
        if(super.isActive()  && super.getStatus() == Status.START)
            super.setActive(false);
    }
}


