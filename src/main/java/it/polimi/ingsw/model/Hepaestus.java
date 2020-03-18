package it.polimi.ingsw.model;

import java.util.List;

public class Hepaestus extends Card {

    public Hepaestus(CardName name) {
        super(name);
    }

    @Override
    public List<Cell> checkBuild(Worker w, Board b) {
        return super.checkBuild(w, b);
    }
}

