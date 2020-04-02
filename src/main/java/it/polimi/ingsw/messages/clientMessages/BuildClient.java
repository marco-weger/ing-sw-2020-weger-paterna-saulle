package it.polimi.ingsw.messages.clientMessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.messages.ClientMessage;

public class BuildClient implements ClientMessage {

    @Override
    public void Accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    String name;
    int x;
    int y;

    public BuildClient(String name, int x, int y)
    {
        this.name=name;
        this.x=x;
        this.y=y;
    }
}
