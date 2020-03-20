package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Board {

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

    public static boolean isCellInBoard(Cell c){
        if(c != null)
            return (c.getRow() >= 0 && c.getRow() < 5 && c.getColumn() >= 0 && c.getColumn() < 5);
        return false;
    }

    public void build(Cell c, int level){
        if(c != null)
            if(isCellInBoard(c) && level >= 0 && level <= 4)
                for(Cell inBoard : field)
                    if(c.getRow() == inBoard.getRow() && c.getColumn() == inBoard.getColumn())
                        inBoard.setLevel(level);
    }

    public Cell getCell(int row, int column){
        for(Cell inBoard : field)
            if(row == inBoard.getRow() && column == inBoard.getColumn())
                return inBoard;
        return null;
    }

}
