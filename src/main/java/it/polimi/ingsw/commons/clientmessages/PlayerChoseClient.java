package it.polimi.ingsw.commons.clientmessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.commons.ClientMessage;

import it.polimi.ingsw.model.cards.CardName;

public class PlayerChoseClient extends ClientMessage {

    @Override
    public void accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    public final CardName c;


    /**
     * This messagge will be send to the server from the client with the decision of the player about the card chosen
     * @param c the card chosen by the player
     * @param name the name of the player
     */
    public PlayerChoseClient(String name, CardName c){
        super(name);
        this.c=c;
    }

}
