package it.polimi.ingsw.commons.clientMessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.commons.ClientMessage;

import java.io.Serializable;

public class PingClient extends ClientMessage implements Serializable {
    @Override
    public void Accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

}
