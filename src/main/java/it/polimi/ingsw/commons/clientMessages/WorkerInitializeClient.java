package it.polimi.ingsw.commons.clientMessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.commons.ClientMessage;

import java.io.Serializable;

public class WorkerInitializeClient implements ClientMessage, Serializable {

    @Override
    public void Accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    public String name;
    public int x, y;

    /**
     * @param x the row chosen to build
     * @param y the column chosen to build
     * this method send to the server the decision of the player about the initial location of the worker
     */
    public WorkerInitializeClient(String name, int x, int y)
    {
        this.name=name;
        this.x=x;
        this.y=y;
    }

}
