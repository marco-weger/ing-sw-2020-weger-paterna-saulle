package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.model.cards.CardName;
import it.polimi.ingsw.view.ViewInterface;

import java.io.Serializable;

public class CardChosenServer  extends ServerMessage implements Serializable{
    @Override
    public void Accept(ViewInterface smh) {smh.handleMessage(this);}

    public String player;
    public CardName cardName;

    public CardChosenServer(String player, CardName cardName){
        this.name="";
        this.cardName=cardName;
        this.player=player;
    }
}
