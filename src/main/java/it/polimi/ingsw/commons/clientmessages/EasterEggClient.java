package it.polimi.ingsw.commons.clientmessages;

import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.controller.ClientMessageHandler;

public class EasterEggClient extends ClientMessage {
    @Override
    public void accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    public EasterEggClient(String name) {
        super(name);
    }
}
