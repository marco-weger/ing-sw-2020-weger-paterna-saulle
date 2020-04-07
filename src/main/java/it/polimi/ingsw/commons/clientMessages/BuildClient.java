package it.polimi.ingsw.commons.clientMessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.commons.ClientMessage;

import java.io.Serializable;

public class BuildClient extends ClientMessage implements Serializable {
    @Override
    public void Accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    public int x, y;

    /**
     * @param x the row chosen to build
     * @param y the column chosen to build
     * this method send to the server the decision of the player about where Build.
     */
    public BuildClient(String name, int x, int y)
    {
        this.name=name;
        this.x=x;
        this.y=y;
    }
}
