package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.ClientMessageHandler;

public interface ClientMessage {

    void Accept(ClientMessageHandler);
}
