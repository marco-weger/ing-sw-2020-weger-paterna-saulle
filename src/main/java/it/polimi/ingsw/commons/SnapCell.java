package it.polimi.ingsw.commons;

import java.io.Serializable;

public class SnapCell implements Serializable {

    int row, column, level;

    public SnapCell(int row, int column, int level){
        this.row=row;
        this.column=column;
        this.level=level;
    }

}
