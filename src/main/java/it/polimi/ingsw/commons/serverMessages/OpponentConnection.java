package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.model.Status;
import it.polimi.ingsw.view.ViewInterface;

import java.io.Serializable;

public class OpponentConnection extends ServerMessage {
    @Override
    public void accept(ViewInterface vi) {vi.handleMessage(this);}

    public String player;

    public OpponentConnection(String player){
        this.name="";
        this.ip="";
        this.player=player;
    }
}
