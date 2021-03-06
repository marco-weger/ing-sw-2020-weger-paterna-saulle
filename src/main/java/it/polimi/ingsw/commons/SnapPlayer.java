package it.polimi.ingsw.commons;

import it.polimi.ingsw.model.cards.CardName;

import java.io.Serializable;

public class SnapPlayer implements Serializable  {

    /**
     * The name
     */
    public final String name;

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
    public boolean loser = false;

    /**
     * It is initialized with default value
     * @param name of player
     */
    public SnapPlayer(String name){
        this.name=name;
        this.symbol="@";
        this.color="";
    }

}
