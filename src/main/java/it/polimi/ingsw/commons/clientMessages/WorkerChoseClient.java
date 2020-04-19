package it.polimi.ingsw.commons.clientMessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.commons.ClientMessage;

public class WorkerChoseClient extends ClientMessage {

    @Override
    public void accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    public int worker;

    /**
     * @param name the name of the player
     * @param worker the worker chosen
     * this method send to the server the decision of the player about the worker to use.
     */
    public WorkerChoseClient(String name, int worker)
    {
        this.name=name;
        this.worker=worker;
    }

}
