package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.List;

public class Cell implements Serializable {


    /**
     * The row
     */
    private final int row;

    /**
     * The column
     */
    private final int column;

    /**
     * The level is a value from 0 (no buildings) to 4 (dome)
     */
    private int level;

    public Cell(int row, int column, int level) {
        this.row = row;
        this.column = column;
        this.level = level;
    }

    public int getRow() {return row;}

    public int getColumn() {return column;}

    public int getLevel() {return level;}


    /**
     * It checks for a level from 0 to 4
     * @param level the level
     */
    public void setLevel(int level) {
        if(level >= 0 && level <= 4)
            this.level = level;
    }


    /**
     * It checks for any workers in the cell
     * @param p list of players
     * @return true if occupied
     */
    public boolean isOccupied(List<Player> p) {
        if(p != null){
            for(Player player:p){
                if(player.getWorker1() != null && player.getWorker1().getRow() == this.row && player.getWorker1().getColumn() == this.column){
                    return true;
                }
                if(player.getWorker2() != null && player.getWorker2().getRow() == this.row && player.getWorker2().getColumn() == this.column){
                    return true;
                }
            }
        }
        return false;
    }
}
