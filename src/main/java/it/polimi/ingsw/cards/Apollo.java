package it.polimi.ingsw.cards;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class Apollo extends Card {

    public Apollo()
    {
        super(CardName.APOLLO,false,false,true,Status.CHOSEN);
    }

    @Override
    public List<Cell> checkMove(List<Player> p, Board b){
        if(p == null || b == null) return new ArrayList<>();
        Player actived = null;
        for(Player player:p)
            if(player.getCard().getName().compareTo(this.getName()) == 0)
                actived = player;
        if(actived == null) return new ArrayList<>();
        List<Cell> ret = super.checkMove(p,b);
        for(Player player:p){
            if(player.getCard().getName().compareTo(this.getName()) != 0 && actived.getCurrentWorker() != null){
                int x = player.getWorker1().getRow() - actived.getCurrentWorker().getRow();
                int y = player.getWorker1().getColumn() - actived.getCurrentWorker().getColumn();
                if(Math.abs(x) <= 1 && Math.abs(y) <= 1)
                    for(Cell c:b.getField())
                        if(c.getRow() == x*2 && c.getColumn() == y*2 && !c.isOccupied(p))
                            ret.add(b.getCell(x,y));
                x = player.getWorker2().getRow() - actived.getCurrentWorker().getRow();
                y = player.getWorker2().getColumn() - actived.getCurrentWorker().getColumn();
                if(Math.abs(x) <= 1 && Math.abs(y) <= 1)
                    for(Cell c:b.getField())
                        if(c.getRow() == x*2 && c.getColumn() == y*2 && !c.isOccupied(p))
                            ret.add(b.getCell(x,y));
            }
        }
        return ret;
    }

}
