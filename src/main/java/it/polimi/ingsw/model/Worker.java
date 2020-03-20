package it.polimi.ingsw.model;

public class Worker {

    private int row,column;
    private boolean active;

    public Worker(int row, int column)
    {
        this.row = row;
        this.column = column;
        this.active = false;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void move(int x, int y){}

    public int getLevel(Board b){
        if(b == null) return -1;
        for(Cell c:b.getField())
            if(row == c.getRow() && column == c.getColumn())
                return c.getLevel();
        return -1;
    }

}
