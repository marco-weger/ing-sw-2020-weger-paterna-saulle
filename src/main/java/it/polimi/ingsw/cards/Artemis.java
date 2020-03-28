package it.polimi.ingsw.cards;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class Artemis extends Card {

    public Artemis() {
        super(CardName.ARTEMIS, false, false, true, Status.CHOSEN);
    }

    @Override
    public List<Cell> checkMove(List<Player> p, Board b) {
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
        List<Cell> available = super.checkMove(p,b);
        if(current.getCard().isActive()) {
            //this list is for counting purpose, otherwise it would loop because of the ".add()" function
            List<Cell> copy = new ArrayList<>(available);
            for (Cell c : copy)
               for (Cell j : b.getField())
                      if (Math.abs(j.getRow() - c.getRow()) <= 1 && Math.abs(j.getColumn() - c.getColumn()) <= 1 && j.getLevel() < 4 && j.getLevel() <= c.getLevel() + 1 && !j.isOccupied(p) && !(available.contains(j)) && !(j.getRow()==actived.getRow() && j.getColumn()==actived.getColumn()))
                         available.add(j);
        }
        return available;
    }

    @Override
    public void move(List<Player> p, Board b, Cell to) {
        if (!(p == null || b == null || to == null)) {
            Player current = null;
            for (Player player : p)
                if (player.getCard().getName().compareTo(this.getName()) == 0)
                    current = player;
            if (current != null) {
                if (current.getCurrentWorker() != null) {
                    List<Cell> available = checkMove(p, b);
                    //the worker can move in every direction, minus the starting point.
                    //thus, the control if "to" equals the current worker starting point.
                    if (available.contains(to) && !((current.getCurrentWorker().getRow()==to.getRow()) && current.getCurrentWorker().getColumn()==to.getColumn())) {
                        current.getCurrentWorker().setRow(to.getRow());
                        current.getCurrentWorker().setColumn(to.getColumn());
                    }
                }
            }
        }
    }
}