package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.view.ViewInterface;

import java.io.Serializable;
import java.util.ArrayList;

public class CheckMoveServer extends ServerMessage implements Serializable {
    @Override
    public void Accept(ViewInterface smh) {smh.handleMessage(this);}

    public ArrayList<SnapCell> sc;

    public CheckMoveServer(String name, ArrayList<SnapCell> sc){
        this.name=name;
        this.sc=sc;
    }
}
