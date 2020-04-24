package it.polimi.ingsw.commons.clientMessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.network.ServerClientHandler;

import javax.sound.midi.Receiver;
import java.io.Serializable;

public class ReConnectionClient extends ClientMessage {

    @Override
    public void accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    public ServerClientHandler sch;

    /**
     * This message will be sent to the server from the client that want to Re-Connect to an open Match
     * [Persistence]
     * @param name the name of the player
     * @param sch his old ServerClientHandler
     */
    public ReConnectionClient(String name, ServerClientHandler sch){
        super(name);
        this.sch=sch;
    }
}
