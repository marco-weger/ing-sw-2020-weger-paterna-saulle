package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;
import java.util.ArrayList;
import java.util.List;

public class Prometheus extends Card {

    public Prometheus()
    {
        super(CardName.PROMETHEUS,false,false,true, Status.CHOSEN);
    }


    /**
     * if Prometheus is Active, the player follows this line
     * start-> chosen-> quesion_b-> built-> question_m-> moved-> question_b-> built-> end
     *
     * otherwise the player follows the classical line
     * start-> chosen-> question_m-> moved-> question_b-> built-> end
     *
     * @param current current state of current turn
     * @return next state
     */

    @Override
    public Status getNextStatus(Status current) {
        if (!super.isActive()) {
            return super.getNextStatus(current);
        }
        else {
            if (current == null) return null;
            switch (current) {
                case CHOSEN:
                    return Status.QUESTION_B;
                case BUILT:
                    super.setActive(false);
                    return Status.QUESTION_M;
                default:
                    return super.getNextStatus(current);
            }
        }
    }


    /**
 * if Prometheus is Active, return only the cells with level inferior to current level plus one,
 * otherwise return a simple check move;
 *
 * @param p list of players
 * @param b board
 *
 * @return list of available cells
 */

@Override
protected List<Cell> checkMove(List<Player> p, Board b) {
    if (p == null || b == null) return new ArrayList<>(0);
    Worker actived = null;
    for (Player player : p)
        if (player.getCard().getName().compareTo(this.getName()) == 0)
            actived = player.getCurrentWorker();
    if (actived == null) return new ArrayList<>();
    List<Cell> ret = super.checkMove(p, b);
    if (super.isActive()) {
        for (Cell c : ret) {
            if (c.getLevel() > b.getCell(actived.getRow(),actived.getColumn()).getLevel())
                ret.remove(c);
        }
    }
    return ret;
}

    @Override
    protected List<Cell> checkBuild(List<Player> p, Board b) {
        List<Cell> available = super.checkBuild(p, b);

        // TODO: if ACTIVE and CHECKMOVE has only 1 element i must remove the single CELL from available

        return available;
    }

    @Override
    public boolean activable(List<Player> p, Board b){
        boolean activable = super.activable(p,b);
        this.setActive(true);
        if(checkBuild(p,b).size() == 0)
            activable = false;
        this.setActive(false);
        return activable;
    }
}
