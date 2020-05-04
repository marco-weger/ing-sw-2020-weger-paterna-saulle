package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.VirtualView;

import java.util.ArrayList;
import java.util.List;

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
    public List<Cell> checkMove(List<Player> p, Board b) {
        if (p == null || b == null) return new ArrayList<>(0);
        Worker actived = null;
        for (Player player : p)
            if (player.getCard().getName().compareTo(this.getName()) == 0)
                actived = player.getCurrentWorker();
        if (actived == null) return new ArrayList<>();
        List<Cell> available;
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
     * PASS (Prometheus Anti Suicide Protocol): Available
     * @param p list of player
     * @param b board
     * @return list of available cells
     */
    @Override
    public List<Cell> checkBuild(List<Player> p, Board b) {

        if (p == null || b == null) return new ArrayList<>();
        Worker actived = null;
        for (Player player : p)
            if (player.getCard().getName().compareTo(this.getName()) == 0) {
                actived = player.getCurrentWorker();
            }

        if (actived == null) return new ArrayList<>();
        List<Cell> available;
        ArrayList<Cell> availablecm;
        ArrayList<Cell> availablelow;
        ArrayList<Cell> availableeq;
        available = super.checkBuild(p, b);
        availablecm= new ArrayList<>();   //checkmove available
        availablelow = new ArrayList<>();  //checkmove available on a lower level
        availableeq = new ArrayList<>();  //checkmove available on the same level

        if(super.isActive()){
            for (Cell c : b.getField()) {
                if (Math.abs(c.getRow() - actived.getRow()) <= 1 && Math.abs(c.getColumn() - actived.getColumn()) <= 1 && c.getLevel() < 4 && c.getLevel() <= actived.getLevel(b) && !c.isOccupied(p)) {
                    availablecm.add(c);
                }
                if (Math.abs(c.getRow() - actived.getRow()) <= 1 && Math.abs(c.getColumn() - actived.getColumn()) <= 1 && c.getLevel() < 4 && c.getLevel() < actived.getLevel(b) && !c.isOccupied(p)) {
                    availablelow.add(c);
                }
                if (Math.abs(c.getRow() - actived.getRow()) <= 1 && Math.abs(c.getColumn() - actived.getColumn()) <= 1 && c.getLevel() < 4 && c.getLevel() == actived.getLevel(b) && !c.isOccupied(p)) {
                    availableeq.add(c);
                }
            }

            //PASP Prometheus Anti Suicide Protocol
            //If you have only one move, on the same level, but you have at least 2 checkbuild allowed, YOU CAN'T BUILD ON THE checkmove marked cell.
            if(availablecm.size() < 2 && availablelow.isEmpty() && available.size() > 1 &&isActive()) {
                available.remove(availableeq.get(0));
            }

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
    public boolean activable(List<Player> p, Board b) {
        if (p == null || b == null) return true;
        Worker actived = null;
        Player current = null;
        for (Player player : p)
            if (player.getCard().getName().compareTo(this.getName()) == 0) {
                actived = player.getCurrentWorker();
                current = player;
            }

        if (actived == null) return true;
        ArrayList<Cell> available;
        ArrayList<Cell> availablelow;
        List<Cell> ckbd;

        available= new ArrayList<>();
        availablelow = new ArrayList<>();

        current.getCard().setActive(true);

        if(super.isActive()){
            for (Cell c : b.getField()) {
                if (Math.abs(c.getRow() - actived.getRow()) <= 1 && Math.abs(c.getColumn() - actived.getColumn()) <= 1 && c.getLevel() < 4 && c.getLevel() <= actived.getLevel(b) && !c.isOccupied(p)) {
                    available.add(c);
                }
                if (Math.abs(c.getRow() - actived.getRow()) <= 1 && Math.abs(c.getColumn() - actived.getColumn()) <= 1 && c.getLevel() < 4 && c.getLevel() < actived.getLevel(b) && !c.isOccupied(p)) {
                    availablelow.add(c);
                }
            }
            // here i check for opponent's turn ability (DELETED FOR NOW)
        }


        ckbd = super.checkBuild(p,b);
        //se ho a disposizione un solo movimento, sullo stesso livello, non farmi usare il potere
        if(available.size() < 2 && availablelow.isEmpty() && ckbd.size() < 2) {
            current.getCard().setActive(false);
            return false;
        }
        current.getCard().setActive(false);
        return true;
    }
}
