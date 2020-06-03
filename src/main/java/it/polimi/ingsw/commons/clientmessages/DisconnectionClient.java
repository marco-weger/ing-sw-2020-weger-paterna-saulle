package it.polimi.ingsw.commons.clientmessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.commons.ClientMessage;

public class DisconnectionClient extends ClientMessage {
    @Override
    public void accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    public final boolean isTimesUp;

    /**
     * This message will be sent to the client from server in case of a disconnection request.
     * @param name the name of the player
     * @param isTimesUp true if the player end his turn's timer
     */
    public DisconnectionClient(String name,boolean isTimesUp){
        super(name);
        this.isTimesUp=isTimesUp;
    }
}
