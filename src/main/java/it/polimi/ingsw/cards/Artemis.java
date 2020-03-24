package it.polimi.ingsw.cards;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class Artemis extends Card {

    public Artemis() {
        super(CardName.ARTEMIS, false, false, true, Status.CHOSEN);
    }

    public List<Cell> checkMove(List<Player> p, Board b) {
        if (p == null || b == null) return new ArrayList<>(0);
        Worker actived = null;
        for (Player player : p)
            if (player.getCard().getName().compareTo(this.getName()) == 0) //get current player (matching card name)
                actived = player.getCurrentWorker();
        if (actived == null) return new ArrayList<>();
        List<Cell> available = super.checkMove(p, b);
        for (Cell c : b.getField())
            if (Math.abs(c.getRow() - actived.getRow()) <= 2 && Math.abs(c.getColumn() - actived.getColumn()) <= 2 && c.getLevel() < 4 && c.getLevel() <= actived.getLevel(b) + 1 && !c.isOccupied(p))
                available.add(c);
        return available;
    }

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