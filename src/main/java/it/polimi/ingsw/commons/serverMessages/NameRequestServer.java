package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.view.ViewInterface;

import java.io.Serializable;
import java.util.ArrayList;

public class NameRequestServer extends ServerMessage {
    @Override
    public void accept(ViewInterface vi) {vi.handleMessage(this);}

    public ArrayList<String> players;
    public boolean isFirstTime;

    public NameRequestServer(ArrayList<String> players, boolean isFirstTime){
        super("");
        this.players = players;
        this.isFirstTime = isFirstTime;
    }
}
