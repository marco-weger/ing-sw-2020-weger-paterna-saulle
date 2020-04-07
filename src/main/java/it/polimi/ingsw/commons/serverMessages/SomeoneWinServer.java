package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.view.ViewInterface;

import java.io.Serializable;

public class SomeoneWinServer extends ServerMessage implements Serializable {
    @Override
    public void Accept(ViewInterface smh) {smh.handleMessage(this);}

    public String player;

    public SomeoneWinServer(String player){
        this.name="";
        this.player=player;
    }
}