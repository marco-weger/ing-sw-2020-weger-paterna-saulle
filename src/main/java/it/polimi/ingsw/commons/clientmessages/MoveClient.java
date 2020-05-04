package it.polimi.ingsw.commons.clientmessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.commons.ClientMessage;

public class MoveClient extends ClientMessage {
    @Override
    public void accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    public int x;

    public int y;

    /**
     * This messagge will be send to the server from the client with the decision of the player about where move.
     * @param x the row chosen to move
     * @param y the column chosen to move
     */
    public MoveClient(String name, int x, int y)
    {
        super(name);
        this.x=x;
        this.y=y;
    }

}
