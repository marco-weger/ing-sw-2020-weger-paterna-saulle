package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.commons.SnapWorker;
import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.commons.servermessages.MovedServer;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.VirtualView;

import java.util.ArrayList;
import java.util.List;

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
    public List<Cell> checkMove(List<Player> p, Board b){
        if(p == null || b == null) return new ArrayList<>(0);
        Worker actived = null;
        boolean otAbility = false;                                                  /*THIS LINE MAKE "OPPONENT'S TURN" ABILITIES DOMINANT ON MINOTAUR*/
        for(Player player:p){
            if(player.getCard().getName().compareTo(this.getName()) == 0) {
                actived = player.getCurrentWorker();
            }

            if (player.getCard().isOpponent() && player.getCard().isActive()) {     /*THIS LINE MAKE "OPPONENT'S TURN" ABILITIES DOMINANT ON MINOTAUR*/
                otAbility = true;                                                   /*THIS LINE MAKE "OPPONENT'S TURN" ABILITIES DOMINANT ON MINOTAUR*/
            }                                                                       /*THIS LINE MAKE "OPPONENT'S TURN" ABILITIES DOMINANT ON MINOTAUR*/

        }
        if(actived == null) return new ArrayList<>();
        List<Cell> ret = super.checkMove(p, b);

        for(Player player:p){
            if(player.getCard().getName().compareTo(this.getName()) != 0){
                int x = player.getWorker1().getRow() - actived.getRow();
                int y = player.getWorker1().getColumn() - actived.getColumn();
                if(Math.abs(x) <= 1 && Math.abs(y) <= 1 && b.getCell(player.getWorker1().getRow(),player.getWorker1().getColumn()).getLevel() <= (b.getCell(actived.getRow(),actived.getColumn()).getLevel()+1)){
                    x = actived.getRow() + x*2;
                    y = actived.getColumn() + y*2;
                    if(x >= 0 && x <= 4 && y >= 0 && y <= 4 && !b.getCell(x,y).isOccupied(p))
                        ret.add(b.getCell(player.getWorker1().getRow(),player.getWorker1().getColumn()));

                }
                x = player.getWorker2().getRow() - actived.getRow();
                y = player.getWorker2().getColumn() - actived.getColumn();
                if(Math.abs(x) <= 1 && Math.abs(y) <= 1 && b.getCell(player.getWorker2().getRow(),player.getWorker2().getColumn()).getLevel() <= (b.getCell(actived.getRow(),actived.getColumn()).getLevel()+1)){
                    x = actived.getRow() + x*2;
                    y = actived.getColumn() + y*2;
                    if(x >= 0 && x <= 4 && y >= 0 && y <= 4 && !b.getCell(x,y).isOccupied(p))
                        ret.add(b.getCell(player.getWorker2().getRow(),player.getWorker2().getColumn()));
                }
            }
        }

        if(otAbility){                                                                                                                                                                                                              /*THIS LINE MAKE ATHENA DOMINANT ON MINOTAUR*/
            for (Cell c : b.getField())                                                                                                                                                                                             /*THIS LINE MAKE ATHENA DOMINANT ON MINOTAUR*/
                if (Math.abs(c.getRow() - actived.getRow()) <= 1 && Math.abs(c.getColumn() - actived.getColumn()) <= 1 && c.getLevel() < 4 && c.getLevel() == actived.getLevel(b) + 1 && c.isOccupied(p))                           /*THIS LINE MAKE ATHENA DOMINANT ON MINOTAUR*/
                    ret.remove(c);
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
    public boolean move(List<Player> p, Board b, Cell to){
        if (!(p == null || b == null || to == null)) {
            Player current = null;
            for (Player player : p)
                if (player.getCard().getName().compareTo(this.getName()) == 0)
                    current = player;
            if (current != null && current.getCurrentWorker() != null) {
                if(to.isOccupied(p)){
                    for (Player player : p){
                        if (player.getCard().getName().compareTo(this.getName()) != 0){
                            if(player.getWorker1().getRow() == to.getRow() && player.getWorker1().getColumn() == to.getColumn() && b.getCell(player.getWorker1().getRow(),player.getWorker1().getColumn()).getLevel() <= (b.getCell(current.getCurrentWorker().getRow(),current.getCurrentWorker().getColumn()).getLevel()+1)){
                                int x = player.getWorker1().getRow()+(player.getWorker1().getRow()-current.getCurrentWorker().getRow());
                                int y = player.getWorker1().getColumn()+(player.getWorker1().getColumn()-current.getCurrentWorker().getColumn());
                                if(x >= 0 && x <= 4 && y >= 0 && y <= 4){
                                    player.getWorker1().move(x, y);
                                    notifyObservers(new MovedServer(new SnapWorker(x,y,player.getName(),1)));

                                    current.getCurrentWorker().move(to.getRow(), to.getColumn());
                                    notifyObservers(new MovedServer(new SnapWorker(to.getRow(),to.getColumn(),current.getName(),current.getWorker1().isActive() ? 1 : 2)));
                                    return true;
                                }
                            }
                            else if(player.getWorker2().getRow() == to.getRow() && player.getWorker2().getColumn() == to.getColumn() && b.getCell(player.getWorker2().getRow(),player.getWorker2().getColumn()).getLevel() <= (b.getCell(current.getCurrentWorker().getRow(),current.getCurrentWorker().getColumn()).getLevel()+1)){
                                int x = player.getWorker2().getRow()+(player.getWorker2().getRow()-current.getCurrentWorker().getRow());
                                int y = player.getWorker2().getColumn()+(player.getWorker2().getColumn()-current.getCurrentWorker().getColumn());
                                if(x >= 0 && x <= 4 && y >= 0 && y <= 4){
                                    player.getWorker2().move(x, y);
                                    notifyObservers(new MovedServer(new SnapWorker(x,y,player.getName(),2)));

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
        return false;
    }
}
