package it.polimi.ingsw.model;

import it.polimi.ingsw.cards.Card;

import java.util.List;

public class Worker {

    private int row,column;
    private boolean active, blocked;

    public Worker(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    /*
    //this version DOES consider the Gods abilities to block
    public boolean getBlocked(Match m, Cell current, Card c) {
        Cell next = new Cell(0,0,0);
        for(int i=-1; i<2;i++){
            for (int j=-1;j<2;j++){
                if (i==0&&j==0)
                    continue; //permit excluding the case in which next is the same as current
                next.setColumn(getColumn()+j);
                next.setRow(getRow()+i);
                if(!(next.isOccupied(m.getPlayers()) || next.getLevel()==4 || next.getLevel()>current.getLevel()+1 || !(next.equals(c.getBlock(this, m.getBoard(), m.getStatus())))))
                    return blocked = false;
            }
        }
        return blocked;
    }
    */

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void move(int x, int y){
        setRow(x);
        setColumn(y);
    }

    public int getLevel(Board b){
        for(Cell c:b.getField())
            if(c.getRow() == this.row && c.getColumn() == this.column)
                return c.getLevel();
        return -1;
    }
}
