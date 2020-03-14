package it.polimi.ingsw.model;

import java.util.List;

public enum DecoratedCard{

    ARTHEMIS,APPOLLO;

    //private final String description;
    //active;

    DecoratedCard() {

    }

    public List<Cell> checkMove(Worker w, Board b){
        List<Cell> avaiable = Card.checkMove(w,b);
        switch (this){
            case ARTHEMIS:
                break;
            case APPOLLO:
                break;
            default:
                break;
        }
        return avaiable;
    };
}
