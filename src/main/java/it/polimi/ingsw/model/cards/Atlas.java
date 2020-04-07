package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.VirtualView;

import java.util.ArrayList;

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
    public boolean build(ArrayList<Player> p, Board b, Cell in) {
        if(!(p == null || b == null || in == null)){
            Player current = null;
            for(Player player:p)
                if(player.getCard().getName().compareTo(this.getName()) == 0)
                    current = player;
            if(current != null) {
                if(current.getCurrentWorker() != null){
                    ArrayList<Cell> available = checkBuild(p,b);
                    //check if "in" is contained in available.
                    if(available.contains(in)){
                        if(current.getCard().isActive()){
                            available.get(available.indexOf(in)).setLevel(4);}
                        else {
                            available.get(available.indexOf(in)).setLevel(available.get(available.indexOf(in)).getLevel()+1);
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
