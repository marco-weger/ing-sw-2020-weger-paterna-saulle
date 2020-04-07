package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.model.Status;
import it.polimi.ingsw.view.ViewInterface;

import java.io.Serializable;

public class CurrentStatusServer extends ServerMessage implements Serializable {
    @Override
    public void Accept(ViewInterface smh) {smh.handleMessage(this);}

    public String player;
    public Status status;

    public CurrentStatusServer(String player, Status status){
        this.name="";
        this.status=status;
        this.player=player;
    }
}
