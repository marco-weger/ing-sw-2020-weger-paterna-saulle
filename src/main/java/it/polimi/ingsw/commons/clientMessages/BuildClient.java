package it.polimi.ingsw.commons.clientMessages;

import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.controller.ClientMessageHandler;

public class BuildClient extends ClientMessage {
    @Override
    public void accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    public final int x, y;

    /**
     *This messagge will be send to the server from the client with the decision of the player about where Build.
     * @param x the row chosen to build
     * @param y the column chosen to build
     */
    public BuildClient(String name, int x, int y)
    {
        super(name);
        this.x=x;
        this.y=y;
    }
}
