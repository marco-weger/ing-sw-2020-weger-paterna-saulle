package it.polimi.ingsw.model;

import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.commons.SnapWorker;
import it.polimi.ingsw.commons.serverMessages.BuiltServer;

public class Worker {

    /**
     * Position of worker
     */
    private int row,column;

    /**
     * It represents if this worker is the active one
     */
    private boolean active;

    public Worker(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    protected void setRow(int row) {
        if(0<=row && row<5)
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    protected void setColumn(int column) {
        if(0<=column && column<5)
        this.column = column;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active){this.active=active;}

    /**
     * It moves the worker to the chosen cell
     * @param x row
     * @param y column
     */
    public void move(int x, int y){
        setRow(x);
        setColumn(y);
    }

    /**
     * @param b the board
     * @return The level of current worker's cell
     */
    public int getLevel(Board b){
        if (b != null) {
        for(Cell c:b.getField())
            if(c.getRow() == this.row && c.getColumn() == this.column)
                return c.getLevel();}
        return -1;
    }
}
