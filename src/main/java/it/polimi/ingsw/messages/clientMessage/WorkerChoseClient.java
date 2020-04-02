package it.polimi.ingsw.messages.clientMessage;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.messages.ClientMessage;

public class WorkerChoseClient implements ClientMessage {

    @Override
    public void Accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    String name;
    int worker;

    public WorkerChoseClient(String name, int worker)
    {
        this.name=name;
        this.worker=worker;
    }

}
