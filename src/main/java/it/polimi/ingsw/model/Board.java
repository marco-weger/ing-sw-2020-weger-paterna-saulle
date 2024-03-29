package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Board implements Serializable {

    /**
     * It is a 25 items list to represent the board
     */
    private List<Cell> field;


    /**
     * The board is initialized with all cells at 0 level
     */
    public Board(){
        field = new ArrayList<>();
        for(int i=0; i<5; i++)
            for(int j=0; j<5; j++)
                field.add(new Cell(i,j,0));
    }


    public List<Cell> getField() {
        return field;
    }


    public void setField(List<Cell> field) {
        this.field = field;
    }


    /**
     * @param c the cell
     * @return true if the cell is in the 5x5 board
     */
    public boolean isCellInBoard(Cell c){
        if(c != null)
            for(Cell cell:this.field)
                if(cell.getRow() == c.getRow() && cell.getColumn() == c.getColumn())
                    return true;
        return false;
    }


    /**
     * It sets the level of the cell by using params
     * @param c the cell
     * @param level the level
     */
    protected void build(Cell c, int level){
        if(c != null && isCellInBoard(c) && level >= 0 && level <= 4){
            for(Cell inBoard : field){
                if(c.getRow() == inBoard.getRow() && c.getColumn() == inBoard.getColumn()){
                    inBoard.setLevel(level);
                }
            }
        }
    }


    /**
     * It returns the required cell if exists
     * @param row the row
     * @param column the column
     * @return the cell
     */
    public Cell getCell(int row, int column){
        for(Cell inBoard : field)
            if(row == inBoard.getRow() && column == inBoard.getColumn())
                return inBoard;
        return null;
    }
}
