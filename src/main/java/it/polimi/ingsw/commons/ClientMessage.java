package it.polimi.ingsw.commons;

import it.polimi.ingsw.controller.ClientMessageHandler;

public interface ClientMessage {

    void Accept(ClientMessageHandler cmh);
}
