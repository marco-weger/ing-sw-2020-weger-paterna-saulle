package it.polimi.ingsw.model;

public class Cell {

    private int row, column, level;

    public Cell(int row, int column, int level) {
        this.row = row;
        this.column = column;
        this.level = level;
    }

    public int getRow() {return row;}

    public void setRow(int row) {this.row = row;}

    public int getColumn() {return column;}

    public void setColumn(int column) {this.column = column;}

    public int getLevel() {return level;}

    /*@
      requires level >= 0 && level <= 4
      ensures this.level >= 0 && this.level <= 4
    @*/
    public void setLevel(int level) {
        if(level >= 0 && level <= 4)
            this.level = level;
    }
}
