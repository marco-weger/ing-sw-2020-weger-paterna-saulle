package it.polimi.ingsw.model;

public class Cell {

    private int row, column, level, taken;

    public Cell(int row, int column, int level, int taken) {
        this.row = row;
        this.column = column;
        this.level = level;
        this.taken = taken;
    }

    public boolean isReachable(){return true;}

    public boolean couldBuild(){return true;}
}
