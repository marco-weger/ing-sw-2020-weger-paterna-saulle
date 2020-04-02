package it.polimi.ingsw.messages.clientMessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.messages.ClientMessage;

import it.polimi.ingsw.model.cards.CardName;

public class PlayerChoseClient implements ClientMessage {

    @Override
    public void Accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    CardName c;

    public PlayerChoseClient(CardName c){ this.c=c; }

}
