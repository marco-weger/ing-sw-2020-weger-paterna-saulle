package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.commons.SnapWorker;
import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.commons.serverMessages.MovedServer;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.VirtualView;

import java.util.ArrayList;

public class Apollo extends Card {

    public Apollo(CardName name, boolean active, boolean opponent, boolean question, Status status, VirtualView vw) {
        super(name, active, opponent, question, status ,vw);
    }

    /**
     * It checks if it's possible to force some opponent worker
     * @param p list of player
     * @param b board
     * @return list of available cells
     */
    @Override
    public ArrayList<Cell> checkMove(ArrayList<Player> p, Board b){
        if(p == null || b == null) return new ArrayList<>(0);
        Worker actived = null;
        boolean OTAbility = false;                                                  /*THIS LINE MAKE "OPPONENT'S TURN ABILITIES" DOMINANT ON APOLLO*/
        for(Player player:p) {
            if (player.getCard().getName().compareTo(this.getName()) == 0) {
                actived = player.getCurrentWorker();
            }
            if (player.getCard().isOpponent() && player.getCard().isActive()) {     /*THIS LINE MAKE "OPPONENT'S TURN" ABILITIES DOMINANT ON APOLLO*/
                OTAbility = true;                                                   /*THIS LINE MAKE "OPPONENT'S TURN" ABILITIES DOMINANT ON APOLLO*/
            }                                                                       /*THIS LINE MAKE "OPPONENT'S TURN" ABILITIES DOMINANT ON APOLLO*/
        }
        if(actived == null) return new ArrayList<>();
        ArrayList<Cell> ret = super.checkMove(p, b);

        for(Player player:p){
            if(player.getCard().getName().compareTo(this.getName()) != 0){
                player.setCurrentWorker(1);
                int x = player.getWorker1().getRow() - actived.getRow();
                int y = player.getWorker1().getColumn() - actived.getColumn();
                if(Math.abs(x) <= 1 && Math.abs(y) <= 1 && b.getCell(player.getWorker1().getRow(),player.getWorker1().getColumn()).getLevel() <= actived.getLevel(b)+1 && player.getCard().checkBuild(p,b).size() > 0)
                        ret.add(b.getCell(player.getWorker1().getRow(), player.getWorker1().getColumn()));

                player.setCurrentWorker(2);
                x = player.getWorker2().getRow() - actived.getRow();
                y = player.getWorker2().getColumn() - actived.getColumn();
                if(Math.abs(x) <= 1 && Math.abs(y) <= 1 && b.getCell(player.getWorker2().getRow(),player.getWorker2().getColumn()).getLevel() <= actived.getLevel(b)+1 && player.getCard().checkBuild(p,b).size() > 0)
                        ret.add(b.getCell(player.getWorker2().getRow(), player.getWorker2().getColumn()));
                player.setCurrentWorker(0);
            }
        }
        if(OTAbility){                                                                                                                                                                                                              /*THIS LINE MAKE ATHENA DOMINANT ON APOLLO*/
            for (Cell c : b.getField())                                                                                                                                                                                             /*THIS LINE MAKE ATHENA DOMINANT ON APOLLO*/
                if (Math.abs(c.getRow() - actived.getRow()) <= 1 && Math.abs(c.getColumn() - actived.getColumn()) <= 1 && c.getLevel() < 4 && c.getLevel() == actived.getLevel(b) + 1 && c.isOccupied(p))                           /*THIS LINE MAKE ATHENA DOMINANT ON APOLLO*/
                    ret.remove(c);
        }
        return ret;
    }

    /**
     * It moves the current worker and actives the ability if necessary
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
                                if(player.getWorker1().getRow() == to.getRow() && player.getWorker1().getColumn() == to.getColumn()){
                                        player.getWorker1().move(current.getCurrentWorker().getRow(), current.getCurrentWorker().getColumn());
                                        notifyObservers(new MovedServer(new SnapWorker(current.getCurrentWorker().getRow(), current.getCurrentWorker().getColumn(), player.getName(), 1)));

                                        current.getCurrentWorker().move(to.getRow(), to.getColumn());
                                        notifyObservers(new MovedServer(new SnapWorker(to.getRow(), to.getColumn(), current.getName(), current.getWorker1().isActive() ? 1 : 2)));
                                        return true;
                                }
                                else if(player.getWorker2().getRow() == to.getRow() && player.getWorker2().getColumn() == to.getColumn()){
                                        player.getWorker2().move(current.getCurrentWorker().getRow(), current.getCurrentWorker().getColumn());
                                        notifyObservers(new MovedServer(new SnapWorker(current.getCurrentWorker().getRow(), current.getCurrentWorker().getColumn(), player.getName(), 2)));

                                        current.getCurrentWorker().move(to.getRow(), to.getColumn());
                                        notifyObservers(new MovedServer(new SnapWorker(to.getRow(), to.getColumn(), current.getName(), current.getWorker1().isActive() ? 1 : 2)));
                                        return true;
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
