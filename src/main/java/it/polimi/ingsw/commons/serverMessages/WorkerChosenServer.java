package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.view.ViewInterface;

import java.io.Serializable;

public class WorkerChosenServer extends ServerMessage {
    @Override
    public void accept(ViewInterface vi) {vi.handleMessage(this);}

    public String player;
    public int worker,x,y;

    public WorkerChosenServer(String player, int worker, int x, int y){
        super("");
        this.worker=worker;
        this.player=player;
        this.x=x;
        this.y=y;
    }
}
