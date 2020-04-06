package it.polimi.ingsw.commons.clientMessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.commons.ClientMessage;

import java.io.Serializable;

public class ReConnectionClient implements ClientMessage , Serializable {
    @Override
    public void Accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    /**
     * @a require to reconnection by the client, to guarantee persistence
     */
}
