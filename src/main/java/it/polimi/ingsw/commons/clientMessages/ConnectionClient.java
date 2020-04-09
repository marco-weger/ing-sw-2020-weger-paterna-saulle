package it.polimi.ingsw.commons.clientMessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.network.ServerClientHandler;

import java.io.Serializable;

public class ConnectionClient extends ClientMessage{
    @Override
    public void accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    public String ip;
    public ServerClientHandler sch;

    public ConnectionClient(String name){
        this.name=name;
        //this.name= ""//new String(name.toString());
        this.ip="";
        sch = null;
    }
}
