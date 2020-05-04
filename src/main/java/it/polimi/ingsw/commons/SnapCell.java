package it.polimi.ingsw.commons;

import java.io.Serializable;

public class SnapCell implements Serializable {

    public final int row;
    public final int column;
    public static int level;

    /**
     *
     * @param row thw row of the cell
     * @param column the coloumn of the cell
     * @param level the level of the cell
     *a "snap-class" to guarantee a safetly trasmission of model.cell class information to the view
     *through the controller. (MVC pattern)
     */
    public SnapCell(int row, int column, int level){
        this.row=row;
        this.column=column;
        this.level=level;
    }

    @Override
    public String toString(){
        return this.row+"-"+this.column;
    }

}