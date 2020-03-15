package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private List<Cell> field;

    public Board(){
        field = new ArrayList<>();
        for(int i=1; i<=5; i++)
            for(int j=1; j<=5; j++)
                field.add(new Cell(i,j,0,0));
    }

    public boolean hasLost(Player p){return true;}

    public void move(Cell from, Cell to){}

    public void build(Cell c, int level){}

}
