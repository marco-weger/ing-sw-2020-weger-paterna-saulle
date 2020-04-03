package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;
import java.util.ArrayList;

public class Prometheus extends Card {

    public Prometheus(CardName name, boolean active, boolean opponent, boolean question, Status status) {
        super(name, active, opponent, question, status);
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
     * @return list of available cells
     */
    @Override
    protected ArrayList<Cell> checkMove(ArrayList<Player> p, Board b) {
        if (p == null || b == null) return new ArrayList<>(0);
        Worker actived = null;
        for (Player player : p)
            if (player.getCard().getName().compareTo(this.getName()) == 0)
                actived = player.getCurrentWorker();
        if (actived == null) return new ArrayList<>();
        ArrayList<Cell> ret = super.checkMove(p, b);
        ArrayList<Cell> toRemove = new ArrayList<>();
        if (super.isActive()) {
            for (Cell c : ret) {
                if (c.getLevel() > b.getCell(actived.getRow(),actived.getColumn()).getLevel())
                    toRemove.add(c);
            }
        }
        ret.removeAll(toRemove);
        return ret;
    }

    /**
     * If you want to build before moving it checks if the new build could block your next move (you could lose the match) and then remove that possibility
     * @param p list of player
     * @param b board
     * @return list of available cells
     */
    @Override
    protected ArrayList<Cell> checkBuild(ArrayList<Player> p, Board b) {

        ArrayList<Cell> available = super.checkBuild(p, b);

        ArrayList<Cell> tmp = this.checkMove(p,b);
        if(isActive() && tmp.size() == 1){
            available.remove(tmp.get(0));
        }

        return available;
    }

    /**
     * It uses the checkBuild to checks if there is some build possibilities before moving
     * @param p list of player
     * @param b board
     * @return true if you could activate ability in this turn
     */
    @Override
    public boolean activable(ArrayList<Player> p, Board b) {
        boolean activable = super.activable(p,b);
        this.setActive(true);
        if(checkBuild(p,b).size() == 0)
            activable = false;
        this.setActive(false);
        return activable;
    }
}
