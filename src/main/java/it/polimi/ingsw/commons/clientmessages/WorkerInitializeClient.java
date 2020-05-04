package it.polimi.ingsw.commons.clientmessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.commons.ClientMessage;

public class WorkerInitializeClient extends ClientMessage {

    @Override
    public void accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    public final int x;
    public final int y;

    /**
     * This messagge will be send to the server from the client with the decision of the current player about the initial location of the worker
     * @param name the name of the player
     * @param x the row chosen to build
     * @param y the column chosen to build
     */
    public WorkerInitializeClient(String name, int x, int y)
    {
        super(name);
        this.x=x;
        this.y=y;
    }

}
