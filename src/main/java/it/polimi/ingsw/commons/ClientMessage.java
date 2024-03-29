package it.polimi.ingsw.commons;

import it.polimi.ingsw.controller.ClientMessageHandler;

import java.io.Serializable;

public abstract class ClientMessage implements Serializable {
    public final String name;

    public abstract void accept(ClientMessageHandler cmh);

    public ClientMessage(String name){
        this.name=name;
    }
}
