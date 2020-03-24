package it.polimi.ingsw.cards;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class Atlas extends Card {

    public Atlas()
    {
        super(CardName.ATLAS,false,false,true, Status.CHOSEN);
    }

    @Override
    public List<Cell> checkBuild(List<Player> p, Board b) {
        if(p == null || b == null) return new ArrayList<>();
        Worker actived = null;
        for(Player player:p)
            if(player.getCard().getName().compareTo(this.getName()) == 0)
                actived = player.getCurrentWorker();
        if(actived == null) return new ArrayList<>();
        List<Cell> buildable = new ArrayList<>();
        for(Cell c:b.getField())
            //Checks: 3x3 grid enveloping actived, c is not a dome, is not occupied.
            if(Math.abs(c.getRow()-actived.getRow()) <= 1 && Math.abs(c.getColumn()-actived.getColumn()) <= 1 && c.getLevel() < 4  && !c.isOccupied(p))
                buildable.add(c);
        return buildable;
    }

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
        super.build(p, b, in);
    }
}
