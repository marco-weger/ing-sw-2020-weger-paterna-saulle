package it.polimi.ingsw.view;

import it.polimi.ingsw.model.cards.CardName;

public class SnapPlayer {

    public String name;
    public char symbol;
    public String color;
    public CardName card;

    public SnapPlayer(String name, char code, int i){
        this.name=name;
        this.symbol=code;

        // color settings
        if(i==0)
            this.color = (TextFormatting.BACKGROUND_BRIGHT_RED.toString()+TextFormatting.COLOR_BLACK);
        if(i==1)
            this.color = (TextFormatting.BACKGROUND_BRIGHT_YELLOW.toString()+TextFormatting.COLOR_BLACK);
        if(i==2)
            this.color = (TextFormatting.BACKGROUND_BRIGHT_PURPLE.toString()+TextFormatting.COLOR_BLACK);

        card=null;
    }
    public SnapPlayer(String name, char code){
        this.name=name;
        this.symbol=code;
        this.color="";
        card=null;
    }

}
