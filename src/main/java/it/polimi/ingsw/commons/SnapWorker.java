package it.polimi.ingsw.commons;

import java.io.Serializable;

public class SnapWorker implements Serializable {

    public String name;
    public int row, column, n;

    /**
     *
     * @param row row of the worker
     * @param column column of the worker
     * @param name name of the player
     * @param n number of the worker
     * a "snap-class" to guarantee a safetly trasmission of model.worker information to the view
     * through the controller. (MVC pattern)
     */
    public SnapWorker(int row, int column, String name, int n){
        this.name=name;
        this.row=row;
        this.column=column;
        this.n=n;
    }

}
