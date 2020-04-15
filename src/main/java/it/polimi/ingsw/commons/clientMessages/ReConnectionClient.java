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

    public ReConnectionClient(String name, ServerClientHandler sch){
        this.name=name;
        this.sch=sch;
    }
}
