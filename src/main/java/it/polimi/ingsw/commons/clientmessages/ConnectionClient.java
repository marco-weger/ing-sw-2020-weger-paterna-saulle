package it.polimi.ingsw.commons.clientmessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.commons.ClientMessage;

public class ConnectionClient extends ClientMessage{
    @Override
    public void accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    /**
     * This message will be sent to the client from server in case of a connection request.
     * @param name the name of the player
     */
    public ConnectionClient(String name){
        super(name);
    }
}
