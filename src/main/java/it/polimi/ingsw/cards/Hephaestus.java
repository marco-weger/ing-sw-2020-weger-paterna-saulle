package it.polimi.ingsw.cards;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class Hephaestus extends Card {

    public Hephaestus()
    {
        super(CardName.HEPHASTUS,false,false,true, Status.CHOSEN);
    }

    /**
     * It checks for availabl cells
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
        return super.checkBuild(p,b);
    }

    /**
     * It checks active attribute and then it builds
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
                    //check if "in" is contained in available,
                    // TODO: IMPLEMENT EXCEPTION
                    if(available.contains(in)){
                        //with this is impossible to build a lvl.3, and a dome consecutively.
                        if(current.getCard().isActive() && in.getLevel()<2){
                            available.get(available.indexOf(in)).setLevel(available.get(available.indexOf(in)).getLevel()+2);
                        }
                        else if(!current.getCard().isActive())
                         {
                            available.get(available.indexOf(in)).setLevel(available.get(available.indexOf(in)).getLevel()+1);
                        }
                    }
                }
            }
        }
    }
}
