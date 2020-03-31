package it.polimi.ingsw.model;

public class Worker {

    private int row,column;
    private boolean active;

    public Worker(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        if(0<=row && row<5)
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        if(0<=column && column<5)
        this.column = column;
    }

    public boolean isActive() {
        return active;
    }

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
