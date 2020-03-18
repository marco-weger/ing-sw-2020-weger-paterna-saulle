package it.polimi.ingsw.cards;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Status;
import it.polimi.ingsw.model.Worker;

import java.util.ArrayList;
import java.util.List;

public class Card {

    private CardName name;
    private boolean active;
    private boolean opponent;
    private boolean question;
    private Status status;

    public Card(CardName name) {
        this.name = name;
    }

    public List<Cell> checkBuild(Worker w, Board b){
        return null;
    }
    public List<Cell> checkMove(Worker w, Board b){
        List<Cell> available = new ArrayList<>();

        // TODO: check generici
        available.add(new Cell(0,0,0));

        return available;
    }
    public List<Cell> getBlocked(Worker w, Board b, Status current){
        return null;
    }
    public boolean checkWin(){
        return true;
    }

}