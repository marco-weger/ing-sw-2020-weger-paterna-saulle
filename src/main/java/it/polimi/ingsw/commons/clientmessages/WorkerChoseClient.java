package it.polimi.ingsw.commons.clientmessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.commons.ClientMessage;

public class WorkerChoseClient extends ClientMessage {

    @Override
    public void accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    public final int worker;

    /**
     *This messagge will be send to the server from the client with the decision of the current player about the worker to use.
     * @param name the name of the player
     * @param worker the worker chosen
     */
    public WorkerChoseClient(String name, int worker)
    {
        super(name);
        this.worker=worker;
    }

}
