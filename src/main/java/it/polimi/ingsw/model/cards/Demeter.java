package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.commons.serverMessages.BuiltServer;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.VirtualView;

import java.util.ArrayList;

public class Demeter extends Card {
    Cell lastBuild;

    public Demeter(CardName name, boolean active, boolean opponent, boolean question, Status status, VirtualView vw) {
        super(name, active, opponent, question, status, vw);
        lastBuild = null;
    }



    @Override
    public Status getNextStatus(Status current) {
        if (!super.isActive()) {
            return super.getNextStatus(current);
        }
        else {
            if (current == null) return null;
            switch (current) {
                case QUESTION_B:
                    super.setActive(false);
                    return Status.QUESTION_B;
                default:
                    return super.getNextStatus(current);
            }
        }
    }


    /**
     * It checks buildable cells by watching active attribute
     * @param p list of player
     * @param b board
     * @return where to build
     */
    @Override
    public ArrayList<Cell> checkBuild(ArrayList<Player> p, Board b)
    {
        if(p == null || b == null) return new ArrayList<>();
        Worker actived = null;
        for(Player player:p)
            if(player.getCard().getName().compareTo(this.getName()) == 0)
                actived = player.getCurrentWorker();
        if(actived == null) return new ArrayList<>();
        ArrayList<Cell> ret = super.checkBuild(p,b);
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
    public boolean build(ArrayList<Player> p, Board b, Cell to){
        if(!(p == null || b == null || to == null)){
            Player current = null;
            for(Player player:p)
                if(player.getCard().getName().compareTo(this.getName()) == 0)
                    current = player;
            if(current != null) {
                if(current.getCurrentWorker() != null){
                    ArrayList<Cell> available = checkBuild(p,b);
                    if(available.contains(to)){
                        super.build(p,b,to);
                        lastBuild = to;
                        notifyObservers(new BuiltServer(new SnapCell(available.get(available.indexOf(to)).getRow(),available.get(available.indexOf(to)).getColumn(),available.get(available.indexOf(to)).getLevel())));
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
