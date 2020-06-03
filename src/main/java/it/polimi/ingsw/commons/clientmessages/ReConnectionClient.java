package it.polimi.ingsw.commons.clientmessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.commons.ClientMessage;

public class ReConnectionClient extends ClientMessage {

    @Override
    public void accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    public int type;

    /**
     * This message will be sent to the server from the client that want to Re-Connect to an open Match
     * [Persistence]
     * @param name the name of the player
     * @param type 1 to reconnection to a match, 2 for new lobby
     */
    public ReConnectionClient(String name, int type){
        super(name);
        this.type=type;
    }
}
