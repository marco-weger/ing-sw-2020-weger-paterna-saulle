package it.polimi.ingsw.commons.clientMessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.commons.ClientMessage;

import java.io.Serializable;

public class WorkerInizializeClient implements ClientMessage, Serializable {

    @Override
    public void Accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    String name;
    int x;
    int y;

    public WorkerInizializeClient(String name, int x, int y)
    {
        this.name=name;
        this.x=x;
        this.y=y;
    }

}
