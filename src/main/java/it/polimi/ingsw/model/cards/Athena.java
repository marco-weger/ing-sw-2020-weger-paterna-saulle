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
     *if Athena is active, return a list of opponent's blocked cell
     *
     * @param p list of player
     * @param b board
     * @param w worker
     * @param current status
     *
     * @return list of opponent's blocked cell
     */
    @Override
    protected List<Cell> activeBlock(List<Player> p, Board b, Worker w,  Status current) {
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
     * Reset the parameter isActive of Athena, if a new round is STARTED
     * and the player with Athena Card have used the power
     * (status player == START and  isActive current player ==1 )
     *
     */
    @Override
    public void inizializeTurn() {
        if(super.isActive()  && super.getStatus() == Status.START)
            super.setActive(false);
    }
}


