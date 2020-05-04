package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.commons.servermessages.BuiltServer;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.VirtualView;

import java.util.ArrayList;
import java.util.List;

public class Hephaestus extends Card {

    public Hephaestus(CardName name, boolean active, boolean opponent, boolean question, Status status, VirtualView vw) {
        super(name, active, opponent, question, status ,vw);
    }

    /**
     * It checks for available cells
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

        List<Cell> available = super.checkBuild(p,b);
        ArrayList<Cell> toRemove = new ArrayList<>();

        // if the player has chosen to active the ability i remove the cell with a level more than 1 (MARCO)
        if(isActive()){
            for(Cell c : available){
                if(c.getLevel() > 1)
                    toRemove.add(c);
            }
        }
        available.removeAll(toRemove);
        return available;
    }

    /**
     * It checks active attribute and then it builds
     * @param p list of player
     * @param b board
     * @param in where to build
     * @return true if builded
     */
    @Override
    public boolean build(List<Player> p, Board b, Cell in) {
        if(!(p == null || b == null || in == null)){
            Player current = null;
            for(Player player:p)
                if(player.getCard().getName().compareTo(this.getName()) == 0)
                    current = player;
            if(current != null && current.getCurrentWorker() != null) {
                List<Cell> available = checkBuild(p,b);
                if(available.contains(in)){
                    if(isActive() && in.getLevel()<2){
                        available.get(available.indexOf(in)).setLevel(available.get(available.indexOf(in)).getLevel()+2);
                        notifyObservers(new BuiltServer(new SnapCell(available.get(available.indexOf(in)).getRow(),available.get(available.indexOf(in)).getColumn(),available.get(available.indexOf(in)).getLevel())));
                        return true;
                    }
                    else if(!isActive() && in.getLevel()<4)
                    {
                        available.get(available.indexOf(in)).setLevel(available.get(available.indexOf(in)).getLevel()+1);
                        notifyObservers(new BuiltServer(new SnapCell(available.get(available.indexOf(in)).getRow(),available.get(available.indexOf(in)).getColumn(),available.get(available.indexOf(in)).getLevel())));
                        return true;
                    }
                }
            }
        }
        return false;
    }


    @Override
    public boolean activable(List<Player> p, Board b) {
        if (p == null || b == null) return true;
        Player current = null;
        for (Player player : p) {
            if (player.getCard().getName().compareTo(this.getName()) == 0) {
                current = player;
            }
        }
        if(current == null){
            return true;
        }
        current.getCard().setActive(true);
        if(current.getCard().checkMove(p,b).isEmpty()){
            current.getCard().setActive(false);
            return false;
        }

        current.getCard().setActive(false);
        return true;
    }

}
