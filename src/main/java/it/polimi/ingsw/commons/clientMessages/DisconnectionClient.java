package it.polimi.ingsw.commons.clientMessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.commons.ClientMessage;

public class DisconnectionClient extends ClientMessage {
    @Override
    public void accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    public boolean isTimesUp;

    /**
     * This message will be sent to the client from server in case of a disconnection request.
     * @param name the name of the player
     */
    public DisconnectionClient(String name,boolean isTimesUp){
        super(name);
        this.isTimesUp=isTimesUp;
    }
}
