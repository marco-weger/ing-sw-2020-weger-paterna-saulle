package it.polimi.ingsw.view;

import it.polimi.ingsw.model.cards.CardName;

public class SnapPlayer {

    /**
     * The name
     */
    final public String name;

    /**
     * The symbol used in CLI version
     */
    public String symbol;

    /**
     * The color used in CLI version
     */
    public String color;

    /**
     * The chosen god
     */
    public CardName card;

    /**
     * True if this player has lost
     */
    public boolean loser;

    /**
     * It is initialized with default value
     * @param name of player
     */
    public SnapPlayer(String name){
        this.name=name;
        this.symbol="@";
        this.color="";
        card=null;
        loser=false;
    }

}
