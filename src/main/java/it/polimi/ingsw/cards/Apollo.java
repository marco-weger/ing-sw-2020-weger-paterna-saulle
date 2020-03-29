package it.polimi.ingsw.cards;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class Apollo extends Card {

    public Apollo()
    {
        super(CardName.APOLLO,false,false,false,Status.CHOSEN);
    }

    /**
     * It checks if it's possible to force some opponent worker
     * @param p list of player
     * @param b board
     * @return list of available cells
     */
    @Override
    public List<Cell> checkMove(List<Player> p, Board b){
        if(p == null || b == null) return new ArrayList<>(0);
        Worker actived = null;
        for(Player player:p)
            if(player.getCard().getName().compareTo(this.getName()) == 0)
                actived = player.getCurrentWorker();
        if(actived == null) return new ArrayList<>();
        List<Cell> ret = super.checkMove(p, b);
        for(Player player:p){
            if(player.getCard().getName().compareTo(this.getName()) != 0){
                int x = player.getWorker1().getRow() - actived.getRow();
                int y = player.getWorker1().getColumn() - actived.getColumn();
                if(Math.abs(x) <= 1 && Math.abs(y) <= 1)
                    ret.add(b.getCell(player.getWorker1().getRow(), player.getWorker1().getColumn()));

                x = player.getWorker2().getRow() - actived.getRow();
                y = player.getWorker2().getColumn() - actived.getColumn();
                if(Math.abs(x) <= 1 && Math.abs(y) <= 1)
                    ret.add(b.getCell(player.getWorker2().getRow(), player.getWorker2().getColumn()));
            }
        }
        return ret;
    }

    /**
     * It moves the current worker and actives the ability if necessary
     * @param p list of player
     * @param b board
     * @param to where to move
     */
    @Override
    public void move(List<Player> p, Board b, Cell to){
        if (!(p == null || b == null || to == null)) {
            Player current = null;
            for (Player player : p)
                if (player.getCard().getName().compareTo(this.getName()) == 0)
                    current = player;
            if (current != null) {
                if (current.getCurrentWorker() != null) {
                    if(to.isOccupied(p)){
                        for (Player player : p){
                            if (player.getCard().getName().compareTo(this.getName()) != 0){
                                if(player.getWorker1().getRow() == to.getRow() && player.getWorker1().getRow() == to.getRow()){
                                    player.getWorker1().setRow(current.getCurrentWorker().getRow());
                                    player.getWorker1().setColumn(current.getCurrentWorker().getColumn());
                                    current.getCurrentWorker().setRow(to.getRow());
                                    current.getCurrentWorker().setColumn(to.getColumn());
                                }
                                else if(player.getWorker2().getRow() == to.getRow() && player.getWorker2().getRow() == to.getRow()){
                                    player.getWorker2().setRow(current.getCurrentWorker().getRow());
                                    player.getWorker2().setColumn(current.getCurrentWorker().getColumn());
                                    current.getCurrentWorker().setRow(to.getRow());
                                    current.getCurrentWorker().setColumn(to.getColumn());
                                }
                            }
                        }
                    }
                    else{
                        super.move(p,b,to);
                    }
                }
            }
        }
    }

}
