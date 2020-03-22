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

    public CardName getName() {
        return name;
    }

    public void setName(CardName name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isOpponent() {
        return opponent;
    }

    public boolean isQuestion() {
        return question;
    }

    public Status getStatus() {
        return status;
    }

    public boolean checkWin(Cell from, Cell to) throws NullPointerException
    {
        if(from != null && to != null)
            if(Board.isCellInBoard(from) && Board.isCellInBoard(to))
                return (from.getLevel() == 2 && to.getLevel() == 3);
        return false;
    }
    public List<Cell> checkBuild(List<Player> p, Board b)
    {
        if(p == null || b == null) return new ArrayList<>();
        Worker actived = null;
        for(Player player:p)
            if(player.getCard().name.compareTo(this.name) == 0)
                actived = player.getCurrentWorker();
        if(actived == null) return new ArrayList<>();
        List<Cell> ret = new ArrayList<>();
        for(Cell c:b.getField())
            if(Math.abs(c.getRow()-actived.getRow()) <= 1 && Math.abs(c.getColumn()-actived.getColumn()) <= 1 && c.getLevel() < 4 && !c.isOccupied(p))
                ret.add(c);
        return ret;
    }
    public List<Cell> checkMove(List<Player> p, Board b){
        if(p == null || b == null) return new ArrayList<>(0);
        Worker actived = null;
        for(Player player:p)
            if(player.getCard().name.compareTo(this.name) == 0)
                actived = player.getCurrentWorker();
        if(actived == null) return new ArrayList<>();
        List<Cell> ret = new ArrayList<>();
        for(Cell c:b.getField())
            if(Math.abs(c.getRow()-actived.getRow()) <= 1 && Math.abs(c.getColumn()-actived.getColumn()) <= 1 && c.getLevel() <= actived.getLevel(b)+1 && !c.isOccupied(p))
                ret.add(c);
        return ret;
    }
    public List<Cell> getBlock(Worker w, Board b, Status current){
        return new ArrayList<>();
    }

    // TODO: testing
    public void move(List<Player> p, Board b, Cell to){
        if(!(p == null || b == null || to == null)){
            Player current = null;
            for(Player player:p)
                if(player.getCard().getName().compareTo(this.getName()) == 0)
                    current = player;
            if(current != null) {
                if(current.getCurrentWorker() != null){
                    // do...
                }
            }
        }

        // TODO: check vari su movimenti (cupola, altri worker) + altre abilità (faccio chemove e vedo se la cella è compresa, poi check block)
        //current.getCurrentWorker().setRow(to.getRow());
        //current.getCurrentWorker().setColumn(to.getColumn());
    }

    // TODO: testing
    public void build(List<Player> p, Board b, Cell to){
        if(!(p == null || b == null || to == null)){
            Player current = null;
            for(Player player:p)
                if(player.getCard().getName().compareTo(this.getName()) == 0)
                    current = player;
            if(current != null) {
                if(current.getCurrentWorker() != null){
                    // do...
                }
            }
        }

        // TODO: check vari su build  + altre abilità (faccio chemove e vedo se la cella è compresa, poi check block)
        //for(Cell c:b.getField())
        //    if(c.getRow() == to.getRow() && c.getColumn() == to.getColumn())
        //        b.build(c,c.getLevel()+1);
    }

    public Status getNextStatus(Status current){
        if(current == null) return null;
        switch (current){
            case START:
                return Status.CHOSEN;
            case CHOSEN:
                return Status.QUESTION_M;
            case QUESTION_M:
                return Status.MOVED;
            case MOVED:
                return Status.QUESTION_B;
            case QUESTION_B:
                return Status.BUILT;
            case BUILT:
                return Status.END;
            case END:
                return Status.START;
            default:
                return null;
        }
    }
    public void inizializeTurn(){}
}