package it.polimi.ingsw.cards;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class Prometheus extends Card {

    public Prometheus()
    {
        super(CardName.PROMETHEUS,false,false,true, Status.CHOSEN);
    }

    /**
     * Checkmove di Prometeo, se il potere è attivo, mostra solo le caselle dello stesso livello
     * se il potere è spento, mostra tutte le caselle
     */
    @Override
    public List<Cell> checkMove(List<Player> p, Board b) {
        if(p == null || b == null) return new ArrayList<>(0);
        Worker actived = null;
        for(Player player:p)
            if(player.getCard().getName().compareTo(this.getName()) == 0)
                actived = player.getCurrentWorker();
        if(actived == null) return new ArrayList<>();
        List<Cell> ret = new ArrayList<>();
        if(!super.isActive()) {
            for (Cell c : b.getField()) {
                if (Math.abs(c.getRow() - actived.getRow()) <= 1 && Math.abs(c.getColumn() - actived.getColumn()) <= 1 && c.getLevel() < 4 && c.getLevel() <= actived.getLevel(b) + 1 && !c.isOccupied(p))
                    ret.add(c);
            }
            return ret;
        }
        else {
            for (Cell c : b.getField()) {
                if (Math.abs(c.getRow() - actived.getRow()) <= 1 && Math.abs(c.getColumn() - actived.getColumn()) <= 1 && c.getLevel() < 4 && c.getLevel() == actived.getLevel(b) && !c.isOccupied(p))
                    ret.add(c);
            }
            return ret;
        }

    }


}
