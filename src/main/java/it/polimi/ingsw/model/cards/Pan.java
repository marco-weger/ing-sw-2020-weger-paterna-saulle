package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Status;
import it.polimi.ingsw.network.VirtualView;

public class Pan extends Card {

    public Pan(CardName name, boolean active, boolean opponent, boolean question, Status status, VirtualView vw) {
        super(name, active, opponent, question, status ,vw);
    }

    /**
     * It adds a win condition: if the worker move down 2 the level he has automatically won
     * @param from cell
     * @param to cell
     * @return true if this player win the match
     */
    @Override
    public boolean checkWin(Cell from, Cell to) {
            if(from == null || to == null)
                return false;
            else if (from.getLevel() == 2 && to.getLevel() == 0)
                return true;
            else
                return super.checkWin(from, to);

    }
}


