package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.commons.SnapWorker;
import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.commons.serverMessages.MovedServer;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.VirtualView;

import java.util.ArrayList;

public class Artemis extends Card {
    Cell lastMoved;

    public Artemis(CardName name, boolean active, boolean opponent, boolean question, Status status, VirtualView vw) {
        super(name, active, opponent, question, status ,vw);
        lastMoved = null;
    }


    /**
     * if Artemis is Active, the player follows this line
     * start-> chosen-> question_m-> question_m-> moved-> question_b-> built-> end
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
                case MOVED:
                    //super.setActive(false);
                    return Status.QUESTION_M;
                case QUESTION_M:
                    super.setActive(false);
                    return Status.QUESTION_B;
                default:
                    return super.getNextStatus(current);
            }
        }
    }

    /**
     * It checks for second move
     * @param p list of player
     * @param b board
     * @return list of available cells
     */
    @Override
    public ArrayList<Cell> checkMove(ArrayList<Player> p, Board b) {
        if (p == null || b == null) return new ArrayList<>(0);
        Worker actived = null;
        Player current = null;
        for (Player player : p){
            if (player.getCard().getName().compareTo(this.getName()) == 0) { //get current player (matching card name)
                current=player; //references the player whit this card
                actived = player.getCurrentWorker();
            }
        }
        if (actived == null) return new ArrayList<>();
        ArrayList<Cell> available = super.checkMove(p,b);
        if(!current.getCard().isActive() && lastMoved != null)
            available.remove(lastMoved);
        return available;
    }

    /**
     * It moves the current worker
     * @param p list of player
     * @param b board
     * @param to where to move
     * @return true if moved
     */
    @Override
    public boolean move(ArrayList<Player> p, Board b, Cell to) {
        if (!(p == null || b == null || to == null)) {
            Player current = null;
            for (Player player : p)
                if (player.getCard().getName().compareTo(this.getName()) == 0)
                    current = player;
            if (current != null) {
                if (current.getCurrentWorker() != null) {
                    ArrayList<Cell> available = checkMove(p, b);
                    //the worker can move in every direction, minus the starting point.
                    //thus, the control if "to" equals the current worker starting point.
                    if (available.contains(to) && !((current.getCurrentWorker().getRow()==to.getRow()) && current.getCurrentWorker().getColumn()==to.getColumn())) {
                        current.getCurrentWorker().move(to.getRow(), to.getColumn());
                        lastMoved=to;
                        notifyObservers(new MovedServer(new SnapWorker(to.getRow(),to.getColumn(),current.getName(),current.getWorker1().isActive() ? 1 : 2)));
                        return true;
                    }
                }
            }
        }
        return false;
    }
}