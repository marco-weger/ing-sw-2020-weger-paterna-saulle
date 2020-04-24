package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.commons.SnapWorker;
import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.commons.serverMessages.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.VirtualView;

import java.io.Serializable;
import java.util.ArrayList;

public class Card extends Observable implements Serializable {

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

    public Card(CardName name, boolean active, boolean opponent, boolean question, Status status, VirtualView vv) {
        this.name = name;
        this.active = active;
        this.opponent = opponent;
        this.question = question;
        this.status = status;
        if(vv != null)
            this.addObserver(vv);
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

    public void initializeTurn(){}

    /**
     * It checks for the after move win condition
     * @param from cell
     * @param to cell
     * @return true if this move is a win condition
     */
    public boolean checkWin(Cell from, Cell to)
    {
        if(from != null && to != null){
            System.out.println("[DEBUG] " + from.getLevel() + " -> " + to.getLevel());
            return (from.getLevel() == 2 && to.getLevel() == 3);
        }
        return false;
    }

    /**
     * @param p list of player
     * @param b board
     * @return list of cells where active worker could build
     */
    public ArrayList<Cell> checkBuild(ArrayList<Player> p, Board b)
    {
        if(p == null || b == null) return new ArrayList<>();
        Worker actived = null;
        for(Player player:p)
            if(player.getCard().name.compareTo(this.name) == 0)
                actived = player.getCurrentWorker();
        if(actived == null) return new ArrayList<>();
        ArrayList<Cell> ret = new ArrayList<>();
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
    public ArrayList<Cell> checkMove(ArrayList<Player> p, Board b){
        if(p == null || b == null) return new ArrayList<>(0);
        Player current = null;
        Worker actived = null;
        for(Player player:p)
            if(player.getCard().name.compareTo(this.name) == 0)
                actived = player.getCurrentWorker();
        if(actived == null) return new ArrayList<>();
        ArrayList<Cell> available = new ArrayList<>();
        for(Cell c:b.getField())
            if(Math.abs(c.getRow()-actived.getRow()) <= 1 && Math.abs(c.getColumn()-actived.getColumn()) <= 1 && c.getLevel() < 4 && c.getLevel() <= actived.getLevel(b)+1 && !c.isOccupied(p))
                available.add(c);

        // here i check for opponent's turn ability
        for (Player player : p)
            if (player.getCard().isOpponent() && player.getCard().isActive())
                available.removeAll(player.getCard().activeBlock(p, b, actived,Status.QUESTION_M));
        return available;
    }

    /**
     * If the ability influences opposite turns this method return blocked cells (QUESTION_M for move and QUESTION_B for build).
     * @param w active worker
     * @param b board
     * @param current current state of current turn
     */
    protected ArrayList<Cell> activeBlock(ArrayList<Player> p, Board b, Worker w,  Status current){
        return new ArrayList<>();
    }

    /**
     * @param p list of player
     * @param b board
     * @param to where to move
     */
    public boolean move(ArrayList<Player> p, Board b, Cell to){
        if (!(p == null || b == null || to == null)) {
            Player current = null;
            for (Player player : p)
                if (player.getCard().getName().compareTo(this.getName()) == 0)
                    current = player;
            if (current != null) {
                if (current.getCurrentWorker() != null) {
                    ArrayList<Cell> available = checkMove(p, b);
                    for (Player player : p)
                        if (player.getCard().isOpponent() && player.getCard().isActive())
                            available.removeAll(player.getCard().activeBlock(p, b, current.getCurrentWorker(),Status.QUESTION_M));
                        if (available.contains(to)) {
                            current.getCurrentWorker().move(to.getRow(), to.getColumn());
                            notifyObservers(new MovedServer(new SnapWorker(to.getRow(),to.getColumn(),current.getName(),current.getWorker1().isActive() ? 1 : 2)));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * @param p list of player
     * @param b board
     * @param to where to build
     */
    public boolean build(ArrayList<Player> p, Board b, Cell to){
        if(!(p == null || b == null || to == null)){
            Player current = null;
            for(Player player:p)
                if(player.getCard().getName().compareTo(this.getName()) == 0)
                    current = player;
            if(current != null) {
                if(current.getCurrentWorker() != null){
                    ArrayList<Cell> available = checkBuild(p,b);
                    for(Player player:p)
                        if(player.getCard().getName().compareTo(this.getName()) != 0)
                            available.removeAll(player.getCard().activeBlock(p, b, current.getCurrentWorker(),Status.QUESTION_B));
                    if(available.contains(to)){
                        available.get(available.indexOf(to)).setLevel(available.get(available.indexOf(to)).getLevel()+1);
                        notifyObservers(new BuiltServer(new SnapCell(available.get(available.indexOf(to)).getRow(),available.get(available.indexOf(to)).getColumn(),available.get(available.indexOf(to)).getLevel())));
                        return true;
                    }
                }
            }
        }
        return false;
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
                return Status.START;
            default:
                return null;
        }
    }

    /**
     * It creates a message and notify the VIEW for available cells
     * @param p players
     * @param b the board
     */
    public ArrayList<SnapCell> getCheckMove(ArrayList<Player> p, Board b){
        ArrayList<Cell> available = this.checkMove(p,b);
        ArrayList<SnapCell> snap = new ArrayList<>();
        for(Cell c:available)
            snap.add(new SnapCell(c.getRow(),c.getColumn(),c.getLevel()));
        for(Player player:p){
            if(player.getCard().name.compareTo(this.name) == 0){
                notifyObservers(new CheckMoveServer(player.getName(),snap));
            }
        }
        return snap;
    }

    /**
     * It creates a message and notify the VIEW for available cells
     * @param p players
     * @param b the board
     */
    public ArrayList<SnapCell> getCheckBuild(ArrayList<Player> p, Board b){
        ArrayList<Cell> available = this.checkBuild(p,b);
        ArrayList<SnapCell> snap = new ArrayList<>();
        for(Cell c:available)
            snap.add(new SnapCell(c.getRow(),c.getColumn(),c.getLevel()));
        for(Player player:p){
            if(player.getCard().name.compareTo(this.name) == 0){
                notifyObservers(new CheckBuildServer(player.getName(),snap));
            }
        }
        return snap;
    }

     /**
     * @param p list of player
     * @param b board
     * @return true if you could activate ability in this turn
     */
    public boolean activable(ArrayList<Player> p, Board b){
        return true;
    }

    /**
     * @param p list of players
     * @param b board
     * @return It checks if current player doesn't have move, and update workers status
     */
    public boolean hasLost(ArrayList<Player> p, Board b) {
        if(p == null || b == null) return false;
        Player current = null;
        for (Player player : p) {
            if (player.getCard().getName().compareTo(this.getName()) == 0)
                current = player;
        }

        if (current == null) {
            throw new NullPointerException("Current player is Null");
        }

        current.setCurrentWorker(1);
        if (current.getCard().checkMove(p, b).size() == 0) {
            current.setCurrentWorker(2);

            if (current.getCard().checkMove(p, b).size() == 0) {
                current.getCurrentWorker().move(-1, -1);
                current.setCurrentWorker(1);
                current.getCurrentWorker().move(-1, -1);
                return true;
            } else {
                current.setCurrentWorker(0);
                return false;
            }
        }
        else {
            current.setCurrentWorker(0);
            return false;
        }
    }


}

