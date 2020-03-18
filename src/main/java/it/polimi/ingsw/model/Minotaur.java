package it.polimi.ingsw.model;

import java.util.List;

public class Minotaur extends Card{
    public Minotaur(CardName name) {
        super(name);
    }

    @Override
    public List<Cell> checkMove(Worker w, Board b) {
        return super.checkMove(w, b);
    }
}
