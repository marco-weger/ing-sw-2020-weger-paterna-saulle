package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.model.cards.CardName;
import it.polimi.ingsw.network.ServerMessageHandler;

import java.io.Serializable;
import java.util.ArrayList;

public class AvailableCardServer implements ServerMessage, Serializable {
    @Override
    public void Accept(ServerMessageHandler smh) {smh.handleMessage(this);}

    public String name;
    public ArrayList<CardName> cardName;

    public AvailableCardServer(ArrayList<CardName> cardName){
        this.name="";
        this.cardName=cardName;
    }
}