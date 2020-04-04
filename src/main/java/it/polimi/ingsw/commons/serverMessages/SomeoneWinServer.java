package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.network.ServerMessageHandler;

import java.io.Serializable;

public class SomeoneWinServer implements ServerMessage, Serializable {
    @Override
    public void Accept(ServerMessageHandler smh) {smh.handleMessage(this);}

    public String name, player;

    public SomeoneWinServer(String player){
        this.name="";
        this.player=player;
    }
}