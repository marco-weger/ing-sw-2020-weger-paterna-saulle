package it.polimi.ingsw.commons.clientMessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.commons.ClientMessage;

import it.polimi.ingsw.model.cards.CardName;

import java.io.Serializable;

public class PlayerChoseClient implements ClientMessage , Serializable {

    @Override
    public void Accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    public String name;
    public CardName c;

    public PlayerChoseClient(CardName c, String name){
        this.c=c;
        this.name=name;
    }

}
