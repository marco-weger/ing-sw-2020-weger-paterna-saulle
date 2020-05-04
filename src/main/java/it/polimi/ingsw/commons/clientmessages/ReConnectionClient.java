package it.polimi.ingsw.commons.clientmessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.commons.ClientMessage;

public class ReConnectionClient extends ClientMessage {

    @Override
    public void accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}


    /**
     * This message will be sent to the server from the client that want to Re-Connect to an open Match
     * [Persistence]
     * @param name the name of the player
     */
    public ReConnectionClient(String name){
        super(name);
    }
}
