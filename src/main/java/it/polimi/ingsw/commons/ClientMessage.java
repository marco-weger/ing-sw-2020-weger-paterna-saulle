package it.polimi.ingsw.commons;

import it.polimi.ingsw.controller.ClientMessageHandler;

public abstract class ClientMessage {
    public String name;

    public abstract void Accept(ClientMessageHandler cmh);
}
