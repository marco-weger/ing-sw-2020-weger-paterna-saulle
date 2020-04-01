package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Board implements Cloneable{

    private List<Cell> field;

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
    public static boolean isCellInBoard(Cell c){
        if(c != null)
            return (c.getRow() >= 0 && c.getRow() < 5 && c.getColumn() >= 0 && c.getColumn() < 5);
        return false;
    }

    /**
     * It sets the level of the cell by using params
     * @param c the cell
     * @param level the level
     */
    protected void build(Cell c, int level){
        if(c != null)
            if(isCellInBoard(c) && level >= 0 && level <= 4)
                for(Cell inBoard : field)
                    if(c.getRow() == inBoard.getRow() && c.getColumn() == inBoard.getColumn())
                        inBoard.setLevel(level);
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

    @Override
    public Board clone() throws CloneNotSupportedException {
        Board b = (Board)super.clone();
        b.field = new ArrayList<>();
        for(Cell c : this.field)
            b.field.add(c.clone());
        return b;
    }

}
