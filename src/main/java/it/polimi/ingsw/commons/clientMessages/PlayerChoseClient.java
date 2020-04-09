package it.polimi.ingsw.commons.clientMessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.commons.ClientMessage;

import it.polimi.ingsw.model.cards.CardName;

import java.io.Serializable;

public class PlayerChoseClient extends ClientMessage {

    @Override
    public void accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    public CardName c;


    /**
     * @param c the card chosen by the player
     * @param name the name of the player
     * this method send to the server the decision of the player about the card chosen
     */
    public PlayerChoseClient(String name, CardName c){
        this.c=c;
        this.name=name;
    }

}
