package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class Minotaur extends Card {

    public Minotaur()
    {
        super(CardName.MINOTAUR,false,false,true, Status.CHOSEN);
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
                    if(!b.getCell(x*2,y*2).isOccupied(p))
                        ret.add(b.getCell(player.getWorker1().getRow(),player.getWorker1().getColumn()));

                x = player.getWorker2().getRow() - actived.getRow();
                y = player.getWorker2().getColumn() - actived.getColumn();
                if(Math.abs(x) <= 1 && Math.abs(y) <= 1)
                    if(!b.getCell(x*2,y*2).isOccupied(p))
                        ret.add(b.getCell(player.getWorker2().getRow(),player.getWorker2().getColumn()));
            }
        }
        return ret;
    }

    /**
     * It moves and does the switch if necessary
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
                                    int x = player.getWorker1().getRow()+(player.getWorker1().getRow()-current.getCurrentWorker().getRow());
                                    int y = player.getWorker1().getColumn()+(player.getWorker1().getColumn()-current.getCurrentWorker().getColumn());
                                    if(x >= 0 && x <= 4 && y >= 0 && y <= 4){
                                        player.getWorker1().setRow(x);
                                        player.getWorker1().setColumn(y);
                                        current.getCurrentWorker().setRow(to.getRow());
                                        current.getCurrentWorker().setColumn(to.getColumn());
                                    }
                                }
                                else if(player.getWorker2().getRow() == to.getRow() && player.getWorker2().getRow() == to.getRow()){
                                    int x = player.getWorker2().getRow()+(player.getWorker2().getRow()-current.getCurrentWorker().getRow());
                                    int y = player.getWorker2().getColumn()+(player.getWorker2().getColumn()-current.getCurrentWorker().getColumn());
                                    if(x >= 0 && x <= 4 && y >= 0 && y <= 4){
                                        player.getWorker2().setRow(x);
                                        player.getWorker2().setColumn(y);
                                        current.getCurrentWorker().setRow(to.getRow());
                                        current.getCurrentWorker().setColumn(to.getColumn());
                                    }
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
