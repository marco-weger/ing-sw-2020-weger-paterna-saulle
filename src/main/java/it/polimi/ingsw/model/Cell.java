package it.polimi.ingsw.model;

import it.polimi.ingsw.Observable;

import java.util.List;

public class Cell extends Observable implements Cloneable{

    private int row, column, level;

    public Cell(int row, int column, int level) {
        this.row = row;
        this.column = column;
        this.level = level;
    }

    public int getRow() {return row;}

    public void setRow(int row) {
        if(row >= 0 && row <= 4)
            this.row = row;
    }

    public int getColumn() {return column;}

    public void setColumn(int column) {
        if(column >= 0 && column <= 4)
            this.column = column;
    }

    public int getLevel() {return level;}

    public void setLevel(int level) {
        if(level >= 0 && level <= 4)
            this.level = level;
    }

    /**
     * It checks for any workers in the cell
     * @param p list of players
     * @return true if occupied
     */
    public boolean isOccupied(List<Player> p)
    {
        if(p != null)
            for(Player player:p)
                if(player.getWorker1() != null && player.getWorker2() != null)
                    if((player.getWorker1().getRow() == this.row && player.getWorker1().getColumn() == this.column) || (player.getWorker2().getRow() == this.row && player.getWorker2().getColumn() == this.column))
                        return true;
        return false;
    }

    @Override
    public Cell clone() throws CloneNotSupportedException {
        Cell c = (Cell)super.clone();
        c.setLevel(this.getLevel());
        c.setRow(this.getRow());
        c.setColumn(this.getColumn());
        return c;
    }
}
