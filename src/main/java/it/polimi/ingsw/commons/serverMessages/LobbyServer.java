package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.view.ViewInterface;

import java.util.ArrayList;

public class LobbyServer extends ServerMessage {
    @Override
    public void accept(ViewInterface vi) {vi.handleMessage(this);}

    public ArrayList<String> players;

    public LobbyServer(ArrayList<String> players){
        this.name="";
        this.ip="";
        this.players=players;
    }
}
