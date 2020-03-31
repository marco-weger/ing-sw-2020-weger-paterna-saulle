package it.polimi.ingsw.Message;

import it.polimi.ingsw.controller.ClientMessageHandler;

public interface ClientMessage {

    void Accept(ClientMessageHandler);
}
