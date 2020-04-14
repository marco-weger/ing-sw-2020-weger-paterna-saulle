package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.view.ViewInterface;

public class NameRequestServer extends ServerMessage {
    @Override
    public void accept(ViewInterface vi) {vi.handleMessage(this);}

    //public ArrayList<String> players;
    public boolean isFirstTime;

    public NameRequestServer(boolean isFirstTime){
        super("");
        //this.players = players;
        this.isFirstTime = isFirstTime;
    }
}
