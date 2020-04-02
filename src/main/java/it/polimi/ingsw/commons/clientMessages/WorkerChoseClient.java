package it.polimi.ingsw.commons.clientMessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.commons.ClientMessage;

import java.io.Serializable;

public class WorkerChoseClient implements ClientMessage , Serializable {

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
