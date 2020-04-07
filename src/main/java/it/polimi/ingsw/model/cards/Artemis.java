package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.VirtualView;

import java.util.ArrayList;

public class Artemis extends Card {

    public Artemis(CardName name, boolean active, boolean opponent, boolean question, Status status, VirtualView vw) {
        super(name, active, opponent, question, status ,vw);
    }

    /**
     * It checks for second move
     * @param p list of player
     * @param b board
     * @return list of available cells
     */
    @Override
    protected ArrayList<Cell> checkMove(ArrayList<Player> p, Board b) {
        if (p == null || b == null) return new ArrayList<>(0);
        Worker actived = null;
        Player current = null;
        for (Player player : p){
            if (player.getCard().getName().compareTo(this.getName()) == 0) { //get current player (matching card name)
                current=player; //references the player whit this card
                actived = player.getCurrentWorker();
            }
        }
        if (actived == null) return new ArrayList<>();
        ArrayList<Cell> available = super.checkMove(p,b);
        if(current.getCard().isActive()) {
            //this list is for counting purpose, otherwise it would loop because of the ".add()" function
            ArrayList<Cell> copy = new ArrayList<>(available);
            for (Cell c : copy)
               for (Cell j : b.getField())
                      if (Math.abs(j.getRow() - c.getRow()) <= 1 && Math.abs(j.getColumn() - c.getColumn()) <= 1 && j.getLevel() < 4 && j.getLevel() <= c.getLevel() + 1 && !j.isOccupied(p) && !(available.contains(j)) && !(j.getRow()==actived.getRow() && j.getColumn()==actived.getColumn()))
                         available.add(j);
        }
        return available;
    }

    /**
     * It moves the current worker
     * @param p list of player
     * @param b board
     * @param to where to move
     * @return true if moved
     */
    @Override
    public boolean move(ArrayList<Player> p, Board b, Cell to) {
        if (!(p == null || b == null || to == null)) {
            Player current = null;
            for (Player player : p)
                if (player.getCard().getName().compareTo(this.getName()) == 0)
                    current = player;
            if (current != null) {
                if (current.getCurrentWorker() != null) {
                    ArrayList<Cell> available = checkMove(p, b);
                    //the worker can move in every direction, minus the starting point.
                    //thus, the control if "to" equals the current worker starting point.
                    if (available.contains(to) && !((current.getCurrentWorker().getRow()==to.getRow()) && current.getCurrentWorker().getColumn()==to.getColumn())) {
                        current.getCurrentWorker().move(to.getRow(), to.getColumn());
                        return true;
                    }
                }
            }
        }
        return false;
    }
}