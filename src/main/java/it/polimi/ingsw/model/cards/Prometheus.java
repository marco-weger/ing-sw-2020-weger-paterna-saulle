package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.VirtualView;

import java.util.ArrayList;

public class Prometheus extends Card {

    public Prometheus(CardName name, boolean active, boolean opponent, boolean question, Status status, VirtualView vw) {
        super(name, active, opponent, question, status ,vw);
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
                //case BUILT:
                case QUESTION_B:
                    return Status.QUESTION_M;
                case MOVED:
                    super.setActive(false);
                    return Status.QUESTION_B;
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
    public ArrayList<Cell> checkMove(ArrayList<Player> p, Board b) {
        if (p == null || b == null) return new ArrayList<>(0);
        Worker actived = null;
        for (Player player : p)
            if (player.getCard().getName().compareTo(this.getName()) == 0)
                actived = player.getCurrentWorker();
        if (actived == null) return new ArrayList<>();
        ArrayList<Cell> available;
        available= new ArrayList<>();

        if(super.isActive()){
            for (Cell c : b.getField()) {
                if (Math.abs(c.getRow() - actived.getRow()) <= 1 && Math.abs(c.getColumn() - actived.getColumn()) <= 1 && c.getLevel() < 4 && c.getLevel() <= actived.getLevel(b) && !c.isOccupied(p))
                    available.add(c);
            }
            // here i check for opponent's turn ability
            for (Player player : p) {
                if (player.getCard().isOpponent() && player.getCard().isActive())
                    available.removeAll(player.getCard().activeBlock(p, b, actived, Status.QUESTION_M));
            }
        }
        else {
            available = super.checkMove(p, b);
        }
        return available;

    }

    /**
     * If you want to build before moving it checks if the new build could block your next move (you could lose the match) and then remove that possibility
     * @param p list of player
     * @param b board
     * @return list of available cells
     */
    @Override
    public ArrayList<Cell> checkBuild(ArrayList<Player> p, Board b) {

        ArrayList<Cell> available = super.checkBuild(p, b);

        ArrayList<Cell> tmp = super.checkMove(p,b);
        if(isActive() && tmp.size() == 1){
            available.remove(tmp.get(0));
        }

        return available;
    }

    /**
     * It uses the checkMove to checks if there is some build possibilities before building
     * @param p list of player
     * @param b board
     * @return true if you could activate ability in this turn
     */
    @Override
    public boolean activable(ArrayList<Player> p, Board b) {
        Player current = null;
        for (Player player : p) {
            if (player.getCard().getName().compareTo(this.getName()) == 0)
                current = player;
        }

        if(current == null){
            System.err.println("Player Not Found --> Prometheus activable");
            return true;
        }

        current.getCard().setActive(true);

        if(current.getCard().checkMove(p,b).size() < 2) {
            current.getCard().setActive(false);
            return false;
        }
        current.getCard().setActive(false);
        return true;
    }
}
