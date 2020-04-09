package it.polimi.ingsw.commons.clientMessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.commons.ClientMessage;

import java.io.Serializable;

public class WorkerInitializeClient extends ClientMessage {

    @Override
    public void accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    public int x, y;

    /**
     * @param name the name of the player
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
