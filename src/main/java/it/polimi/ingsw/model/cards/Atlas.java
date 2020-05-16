package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.commons.servermessages.BuiltServer;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.VirtualView;

import java.util.List;

public class Atlas extends Card {


    public Atlas(CardName name, boolean active, boolean opponent, boolean question, Status status, VirtualView vw) {
        super(name, active, opponent, question, status ,vw);
    }


    /**
     * It builds normal or a dome by active attribute
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
                //check if "in" is contained in available.
                if(available.contains(in)){
                    if(current.getCard().isActive()){
                        available.get(available.indexOf(in)).setLevel(4);}
                    else {
                        available.get(available.indexOf(in)).setLevel(available.get(available.indexOf(in)).getLevel()+1);
                    }
                    notifyObservers(new BuiltServer(new SnapCell(available.get(available.indexOf(in)).getRow(),available.get(available.indexOf(in)).getColumn(),available.get(available.indexOf(in)).getLevel())));
                    return true;
                }
            }
        }
        return false;
    }
}
