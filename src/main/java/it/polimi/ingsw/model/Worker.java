package it.polimi.ingsw.model;

import it.polimi.ingsw.Observable;

public class Worker extends Observable implements Cloneable{

    private int row,column;
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

    protected void setActive(boolean active){this.active=active;}

    /**
     * It moves the worker to the chosen cell
     * @param x row
     * @param y column
     */
    protected void move(int x, int y){
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

    @Override
    public Worker clone() throws CloneNotSupportedException {
        Worker w = (Worker)super.clone(); //new Worker(this.row,this.column);
        w.setActive(this.isActive());
        w.setColumn(this.getColumn());
        w.setRow(this.getRow());
        return w;
    }
}
