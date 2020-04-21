package it.polimi.ingsw.view;

import it.polimi.ingsw.model.cards.CardName;

public class SnapPlayer {

    public String name;
    public String symbol;
    public String color;
    public CardName card;
    public boolean loser;

    public SnapPlayer(String name){
        this.name=name;
        this.symbol="@";
        this.color="";
        card=null;
        loser=false;
    }

}
