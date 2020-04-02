package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class Card extends Observable {

    /**
     * Active is related to the ability.
     * Final parameters are used to manage abilities.
     * OPPONENT: it is true if the ability influences opposite turns.
     * QUESTION: it is true if the ability need a question to be actived.
     * STATUS: the state when the ability could be actived.
     */
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



    public void inizializeTurn(){}

    /**
     * It checks for the after move win condition
     * @param from cell
     * @param to cell
     * @return true if this move is a win condition
     */
    public boolean checkWin(Cell from, Cell to) throws NullPointerException
    {
        if(from != null && to != null)
            if(Board.isCellInBoard(from) && Board.isCellInBoard(to))
                return (from.getLevel() == 2 && to.getLevel() == 3);
        return false;
    }

    /**
     * @param p list of player
     * @param b board
     * @return list of cells where active worker could build
     */
    protected List<Cell> checkBuild(List<Player> p, Board b)
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

    /**
     * @param p list of player
     * @param b board
     * @return list of cells where active worker could move
     */
    protected List<Cell> checkMove(List<Player> p, Board b){
        if(p == null || b == null) return new ArrayList<>(0);
        Worker actived = null;
        for(Player player:p)
            if(player.getCard().name.compareTo(this.name) == 0)
                actived = player.getCurrentWorker();
        if(actived == null) return new ArrayList<>();
        List<Cell> ret = new ArrayList<>();
        for(Cell c:b.getField())
            if(Math.abs(c.getRow()-actived.getRow()) <= 1 && Math.abs(c.getColumn()-actived.getColumn()) <= 1 && c.getLevel() < 4 && c.getLevel() <= actived.getLevel(b)+1 && !c.isOccupied(p))
                ret.add(c);
        return ret;
    }

    /**
     * If the ability influences opposite turns this method return blocked cells (QUESTION_M for move and QUESTION_B for build).
     * @param w active worker
     * @param b board
     * @param current current state of current turn
     */
    protected List<Cell> activeBlock(List<Player> p, Board b, Worker w,  Status current){
        return new ArrayList<>();
    }

    /**
     * @param p list of player
     * @param b board
     * @param to where to move
     */
    public void move(List<Player> p, Board b, Cell to){
        if (!(p == null || b == null || to == null)) {
            Player current = null;
            for (Player player : p)
                if (player.getCard().getName().compareTo(this.getName()) == 0)
                    current = player;
            if (current != null) {
                if (current.getCurrentWorker() != null) {
                    List<Cell> available = checkMove(p, b);
                    for (Player player : p)
                        if (player.getCard().getName().compareTo(this.getName()) != 0)
                            available.removeAll(player.getCard().activeBlock(p, b,  current.getCurrentWorker(),Status.QUESTION_M));
                    if (available.contains(to)) {
                        current.getCurrentWorker().move(to.getRow(),to.getColumn());
                    }
                }
            }
        }
    }

    /**
     * @param p list of player
     * @param b board
     * @param to where to build
     */
    public void build(List<Player> p, Board b, Cell to){
        if(!(p == null || b == null || to == null)){
            Player current = null;
            for(Player player:p)
                if(player.getCard().getName().compareTo(this.getName()) == 0)
                    current = player;
            if(current != null) {
                if(current.getCurrentWorker() != null){
                    List<Cell> available = checkBuild(p,b);
                    for(Player player:p)
                        if(player.getCard().getName().compareTo(this.getName()) != 0)
                            available.removeAll(player.getCard().activeBlock(p, b, current.getCurrentWorker(),Status.QUESTION_B));
                    if(available.contains(to)){
                        available.get(available.indexOf(to)).setLevel(available.get(available.indexOf(to)).getLevel()+1);
                    }
                }
            }
        }
    }

    /**
     * @param current current state of current turn
     * @return next status by considering active abilities
     */
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

}

