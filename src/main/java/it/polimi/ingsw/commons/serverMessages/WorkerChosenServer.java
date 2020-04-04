package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.network.ServerMessageHandler;

import java.io.Serializable;

public class WorkerChosenServer implements ServerMessage, Serializable {
    @Override
    public void Accept(ServerMessageHandler smh) {smh.handleMessage(this);}

    public String name, player;
    public int worker,x,y;

    public WorkerChosenServer(String player, int worker, int x, int y){
        this.name="";
        this.worker=worker;
        this.player=player;
        this.x=x;
        this.y=y;
    }
}
