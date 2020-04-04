package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.model.Status;
import it.polimi.ingsw.network.ServerMessageHandler;

import java.io.Serializable;

public class CurrentStatusServer implements ServerMessage, Serializable {
    @Override
    public void Accept(ServerMessageHandler smh) {smh.handleMessage(this);}

    public String name, player;
    public Status status;

    public CurrentStatusServer(String player, Status status){
        this.name="";
        this.status=status;
        this.player=player;
    }
}
