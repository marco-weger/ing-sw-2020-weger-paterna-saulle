package it.polimi.ingsw.cards;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class Demeter extends Card {
    Cell lastBuild;
    public Demeter()
    {
        super(CardName.DEMETER,false,false,true, Status.CHOSEN);
        lastBuild = null;
    }
    @Override
    public List<Cell> checkBuild(List<Player> p, Board b)
    {
        if(p == null || b == null) return new ArrayList<>();
        Worker actived = null;
        for(Player player:p)
            if(player.getCard().getName().compareTo(this.getName()) == 0)
                actived = player.getCurrentWorker();
        if(actived == null) return new ArrayList<>();
        List<Cell> ret = super.checkBuild(p,b);
        if(this.isActive() && lastBuild != null)
            ret.remove(lastBuild);
        return ret;
    }
    @Override
    public void build(List<Player> p, Board b, Cell to){
        if(!(p == null || b == null || to == null)){
            Player current = null;
            for(Player player:p)
                if(player.getCard().getName().compareTo(this.getName()) == 0)
                    current = player;
            if(current != null) {
                if(current.getCurrentWorker() != null){
                    List<Cell> available = checkBuild(p,b);
                    if(available.contains(to)){
                        super.build(p,b,to);
                        lastBuild = to;
                    }
                }
            }
        }
    }
}
