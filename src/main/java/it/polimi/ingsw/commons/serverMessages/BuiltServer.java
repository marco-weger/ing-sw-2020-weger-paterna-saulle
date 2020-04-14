package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.commons.SnapWorker;
import it.polimi.ingsw.view.ViewInterface;

import java.util.ArrayList;

public class BuiltServer extends ServerMessage {
    @Override
    public void accept(ViewInterface vi) {vi.handleMessage(this);}

    public ArrayList<String> players;
    public SnapCell sc;

    public BuiltServer(SnapCell sc){
        super("");
        this.sc = sc;
    }
}
