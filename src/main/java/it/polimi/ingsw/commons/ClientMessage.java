package it.polimi.ingsw.commons;

import it.polimi.ingsw.controller.ClientMessageHandler;

import java.io.Serializable;

public abstract class ClientMessage implements Serializable {
    public String name;

    public abstract void accept(ClientMessageHandler cmh);
}
