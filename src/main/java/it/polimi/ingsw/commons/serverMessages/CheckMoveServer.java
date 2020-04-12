package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.view.ViewInterface;

import java.io.Serializable;
import java.util.ArrayList;

public class CheckMoveServer extends ServerMessage {
    @Override
    public void accept(ViewInterface vi) {vi.handleMessage(this);}

    public ArrayList<SnapCell> sc;

    public CheckMoveServer(String name, String ip, ArrayList<SnapCell> sc){
        this.name=name;
        this.ip=ip;
        this.sc=sc;
    }
}
