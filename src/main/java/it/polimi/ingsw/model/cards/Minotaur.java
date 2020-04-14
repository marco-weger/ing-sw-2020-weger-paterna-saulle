package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.commons.SnapWorker;
import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.commons.serverMessages.MovedServer;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.VirtualView;

import java.util.ArrayList;
public class Minotaur extends Card {

    public Minotaur(CardName name, boolean active, boolean opponent, boolean question, Status status, VirtualView vw) {
        super(name, active, opponent, question, status ,vw);
    }

    /**
     * It checks if it's possible to force some opponent worker
     * @param p list of player
     * @param b board
     * @return list of available cells
     */
    @Override
    protected ArrayList<Cell> checkMove(ArrayList<Player> p, Board b){
        if(p == null || b == null) return new ArrayList<>(0);
        Worker actived = null;
        for(Player player:p)
            if(player.getCard().getName().compareTo(this.getName()) == 0)
                actived = player.getCurrentWorker();
        if(actived == null) return new ArrayList<>();
        ArrayList<Cell> ret = super.checkMove(p, b);

        for(Player player:p){
            if(player.getCard().getName().compareTo(this.getName()) != 0){
                int x = player.getWorker1().getRow() - actived.getRow();
                int y = player.getWorker1().getColumn() - actived.getColumn();
                if(Math.abs(x) <= 1 && Math.abs(y) <= 1){
                    x = actived.getRow() + x*2;
                    y = actived.getColumn() + y*2;
                    if(x >= 0 && x <= 4 && y >= 0 && y <= 4)
                        if(!b.getCell(x,y).isOccupied(p))
                            ret.add(b.getCell(player.getWorker1().getRow(),player.getWorker1().getColumn()));
                }
                x = player.getWorker2().getRow() - actived.getRow();
                y = player.getWorker2().getColumn() - actived.getColumn();
                if(Math.abs(x) <= 1 && Math.abs(y) <= 1){
                    x = actived.getRow() + x*2;
                    y = actived.getColumn() + y*2;
                    if(x >= 0 && x <= 4 && y >= 0 && y <= 4)
                        if(!b.getCell(x,y).isOccupied(p))
                            ret.add(b.getCell(player.getWorker2().getRow(),player.getWorker2().getColumn()));
                }
            }
        }
        return ret;
    }

    /**
     * It moves and does the switch if necessary
     * @param p list of player
     * @param b board
     * @param to where to move
     * @return true if moved
     */
    @Override
    public boolean move(ArrayList<Player> p, Board b, Cell to){
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
                                        player.getWorker1().move(x, y);
                                        current.getCurrentWorker().move(to.getRow(), to.getColumn());
                                        notifyObservers(new MovedServer(new SnapWorker(to.getRow(),to.getColumn(),current.getName(),current.getWorker1().isActive() ? 1 : 2)));
                                        return true;
                                    }
                                }
                                else if(player.getWorker2().getRow() == to.getRow() && player.getWorker2().getRow() == to.getRow()){
                                    int x = player.getWorker2().getRow()+(player.getWorker2().getRow()-current.getCurrentWorker().getRow());
                                    int y = player.getWorker2().getColumn()+(player.getWorker2().getColumn()-current.getCurrentWorker().getColumn());
                                    if(x >= 0 && x <= 4 && y >= 0 && y <= 4){
                                        player.getWorker2().move(x, y);
                                        current.getCurrentWorker().move(to.getRow(), to.getColumn());
                                        notifyObservers(new MovedServer(new SnapWorker(to.getRow(),to.getColumn(),current.getName(),current.getWorker1().isActive() ? 1 : 2)));
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                    else{
                        super.move(p,b,to);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
