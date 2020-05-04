package it.polimi.ingsw.view;

import it.polimi.ingsw.model.cards.CardName;

public class SnapPlayer {

    /**
     * The name
     */
    public final String name;

    /**
     * The symbol used in CLI version
     */
    public static String symbol;

    /**
     * The color used in CLI version
     */
    public static String color;

    /**
     * The chosen god
     */
    public static CardName card = null;

    /**
     * True if this player has lost
     */
    public static boolean loser = false;

    /**
     * It is initialized with default value
     * @param name of player
     */
    public SnapPlayer(String name){
        this.name=name;
        //this.symbol="@";
        //this.color="";
    }

}
