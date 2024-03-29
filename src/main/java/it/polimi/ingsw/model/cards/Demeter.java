package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.commons.servermessages.BuiltServer;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.VirtualView;

import java.util.ArrayList;
import java.util.List;

public class Demeter extends Card {
    /**
     * Store the first build
     */
    Cell lastBuild;

    /**
     * Card Constructor
     * @param name the name of the card
     * @param active tell if the power is active
     * @param opponent OPPONENT'S TURN ABILITY FLAG, remove the respective cells to opponents
     * @param question tell if the god needs the Question Ability (Banner on GUI / Input and Print on CLI)
     * @param status tell in which state the God use his Ability
     * @param vw the Client's VirtualView
     */
    public Demeter(CardName name, boolean active, boolean opponent, boolean question, Status status, VirtualView vw) {
        super(name, active, opponent, question, status, vw);
        lastBuild = null;
    }


    /**
     * if Demeter is Active, the player follows this line
     * start-> chosen-> quesion_m-> moved-> question_b-> question_b-> built-> end
     *
     * otherwise the player follows the classical line
     * start-> chosen-> question_m-> moved-> question_b-> built-> end
     *
     * @param current current state of current turn
     * @return next state
     */
    @Override
    public Status getNextStatus(Status current) {
        if (super.isActive()) {
            if (current == null) return null;
            if (current == Status.QUESTION_B) {
                super.setActive(false);
                return Status.QUESTION_B;
            }
        }
        return super.getNextStatus(current);
    }


    /**
     * It checks buildable cells by watching active attribute
     * @param p list of player
     * @param b board
     * @return where to build
     */
    @Override
    public List<Cell> checkBuild(List<Player> p, Board b) {
        if(p == null || b == null) return new ArrayList<>();
        Worker actived = null;
        for(Player player:p)
            if(player.getCard().getName().compareTo(this.getName()) == 0)
                actived = player.getCurrentWorker();
        if(actived == null) return new ArrayList<>();
        List<Cell> ret = super.checkBuild(p,b);
        if(!this.isActive() && lastBuild != null)
            ret.remove(lastBuild);
        return ret;
    }


    /**
     * It builds and set the value of lastBuild attribute
     * @param p list of player
     * @param b board
     * @param to where to build
     * @return true if builded
     */
    @Override
    public boolean build(List<Player> p, Board b, Cell to){
        if(!(p == null || b == null || to == null)){
            Player current = null;
            for(Player player:p)
                if(player.getCard().getName().compareTo(this.getName()) == 0)
                    current = player;
            if(current != null && current.getCurrentWorker() != null) {
                List<Cell> available = checkBuild(p,b);
                if(available.contains(to)){
                    super.build(p,b,to);
                    lastBuild = to;
                    notifyObservers(new BuiltServer(new SnapCell(available.get(available.indexOf(to)).getRow(),available.get(available.indexOf(to)).getColumn(),available.get(available.indexOf(to)).getLevel())));
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Check if the Ability of Demeter can be turn on safely (To avoid player's lose due to a true AnswerAbility Client)
     * @param p list of player
     * @param b board
     * @return true if active the ability is safely, false otherwise
     */
    @Override
    public boolean activable(List<Player> p, Board b) {
        return super.checkBuild(p, b).size() >= 2;
    }

    /**
     * Clean the parameter lastBuild
     */
    @Override
    public void initializeTurn() { lastBuild = null;}
}
