package it.polimi.ingsw.commons;

import java.io.Serializable;

public class SnapWorker implements Serializable {

    String name;
    int row, column, n;

    public SnapWorker(int row, int column, String name, int n){
        this.name=name;
        this.row=row;
        this.column=column;
        this.n=n;
    }

}
