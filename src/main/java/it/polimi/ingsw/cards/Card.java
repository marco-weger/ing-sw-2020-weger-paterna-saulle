package it.polimi.ingsw.cards;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class Card {

    private CardName name;
    private boolean active;
    private final boolean opponent;
    private final boolean question;
    private final Status status;

    public Card(CardName name, boolean active, boolean opponent, boolean question, Status status) {
        this.name = name;
        this.active = active;
        this.opponent = opponent;
        this.question = question;
        this.status = status;
    }

    public boolean checkWin(Cell from, Cell to) throws NullPointerException
    {
        if(from != null && to != null)
            if(Board.isCellInBoard(from) && Board.isCellInBoard(to))
                return (from.getLevel() == 2 && to.getLevel() == 3);
        return false;
    }

    public List<Cell> checkBuild(List<Player> p, Board b){
        return null;
    }
    public List<Cell> checkMove(List<Player> p, Board b){
        List<Cell> available = new ArrayList<>();

        // TODO: check generici
        available.add(new Cell(0,0,0));

        return available;
    }
    public List<Cell> getBlock(Worker w, Board b, Status current){
        return null;
    }
    public Status getNextStatus(Status current){
        return Status.BUILT;
    }
    public void inizializeTurn(){}

}