package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.SnapWorker;
import it.polimi.ingsw.view.ViewInterface;

import java.util.ArrayList;

public class MovedServer extends ServerMessage {
    @Override
    public void accept(ViewInterface vi) {vi.handleMessage(this);}

    public String players;
    public SnapWorker sw;

    public MovedServer(SnapWorker sw){
        super("");
        this.sw = sw;
    }
}
