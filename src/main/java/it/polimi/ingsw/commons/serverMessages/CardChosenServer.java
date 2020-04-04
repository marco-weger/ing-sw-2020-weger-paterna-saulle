package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.model.cards.CardName;
import it.polimi.ingsw.network.ServerMessageHandler;

import java.io.Serializable;

public class CardChosenServer implements ServerMessage, Serializable {
    @Override
    public void Accept(ServerMessageHandler smh) {smh.handleMessage(this);}

    public String name, player;
    public CardName cardName;

    public CardChosenServer(String player, CardName cardName){
        this.name="";
        this.cardName=cardName;
        this.player=player;
    }
}
