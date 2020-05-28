package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.commons.SnapWorker;
import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.commons.servermessages.MovedServer;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.VirtualView;

import java.util.ArrayList;
import java.util.List;

public class Artemis extends Card {
    /**
     * Store the first move
     */
    Cell lastMoved;

    /**
     * Card Constructor
     * @param name the name of the card
     * @param active tell if the power is active
     * @param opponent opponent's active his OPPONENT'S TURN ABILITY, remove the respective cells
     * @param question tell if the god needs the Question Ability (Banner on GUI / Input and Print on CLI)
     * @param status tell in which state the God use his Ability
     * @param vw the Client's VirtualView
     */
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
    public List<Cell> checkMove(List<Player> p, Board b) {
        if (p == null || b == null) return new ArrayList<>(0);
        Worker actived = null;
        for (Player player : p){
            if (player.getCard().getName().compareTo(this.getName()) == 0) { //get current player (matching card name)
                actived = player.getCurrentWorker();
            }
        }
        if (actived == null) return new ArrayList<>();
        List<Cell> available = super.checkMove(p,b);
        if(lastMoved != null)
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
    public boolean move(List<Player> p, Board b, Cell to) {
        if (!(p == null || b == null || to == null)) {
            Player current = null;
            for (Player player : p)
                if (player.getCard().getName().compareTo(this.getName()) == 0)
                    current = player;

            if (current != null && current.getCurrentWorker() != null) {
                List<Cell> available = checkMove(p, b);
                //the worker can move in every direction, minus the starting point.
                //thus, the control if "to" equals the current worker starting point.
                if (available.contains(to) && !((current.getCurrentWorker().getRow()==to.getRow()) && current.getCurrentWorker().getColumn()==to.getColumn())) {
                    lastMoved = b.getCell(current.getCurrentWorker().getRow(),current.getCurrentWorker().getColumn());
                    current.getCurrentWorker().move(to.getRow(), to.getColumn());

                    notifyObservers(new MovedServer(new SnapWorker(to.getRow(),to.getColumn(),current.getName(),current.getWorker1().isActive() ? 1 : 2)));
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Check if the Ability of Artemis can be turn on safely (To avoid player's lose due to a true AnswerAbility Client)
     * @param p list of player
     * @param b board
     * @return true if active the ability is safely, false otherwise
     */
    @Override
    public boolean activable(List<Player> p, Board b) {
        List<Cell> available;
        available = super.checkMove(p,b);

        if(lastMoved != null){
            available.remove(lastMoved);
        }
        return !available.isEmpty();
    }


    /**
     * Clean the parameter lastMoved
     */
    @Override
    public void initializeTurn() { lastMoved = null;}

}