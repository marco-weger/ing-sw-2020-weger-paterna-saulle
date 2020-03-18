package it.polimi.ingsw.model;

import java.util.List;

public class Card extends FactoryCard {
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
        return null;
    }
    public List<Cell> getBlocked(Worker w, Board b, Status current){
        return null;
    }
    public boolean checkWin(){
        return true;
    }


}
