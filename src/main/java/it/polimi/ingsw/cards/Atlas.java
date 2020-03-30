package it.polimi.ingsw.cards;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class Atlas extends Card {

    public Atlas()
    {
        super(CardName.ATLAS,false,false,true, Status.CHOSEN);
    }
    //checkBuild
    // TODO: is it necessary? Its a normal check (MARCO)
    //I think it's not (GIULIO)
    @Override
    public List<Cell> checkBuild(List<Player> p, Board b)
    {
        if(p == null || b == null) return new ArrayList<>();
        Worker actived = null;
        for(Player player:p)
            if(player.getCard().getName().compareTo(this.getName()) == 0)
                actived = player.getCurrentWorker();
        if(actived == null) return new ArrayList<>();
        return super.checkBuild(p,b);
    }

    /**
     * It builds normal or a dome by active attribute
     * @param p list of player
     * @param b board
     * @param in where to build
     */
    @Override
    public void build(List<Player> p, Board b, Cell in) {
        if(!(p == null || b == null || in == null)){
            Player current = null;
            for(Player player:p)
                if(player.getCard().getName().compareTo(this.getName()) == 0)
                    current = player;
            if(current != null) {
                if(current.getCurrentWorker() != null){
                    List<Cell> available = checkBuild(p,b);
                        //check if "in" is contained in available.
                        if(available.contains(in)){
                            if(current.getCard().isActive()){
                                 available.get(available.indexOf(in)).setLevel(4);}
                             else {
                                 available.get(available.indexOf(in)).setLevel(available.get(available.indexOf(in)).getLevel()+1);
                             }
                        }
                }
            }
        }
    }
}
