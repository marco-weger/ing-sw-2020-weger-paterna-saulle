package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.view.ViewInterface;

import java.io.Serializable;
import java.util.ArrayList;

public class CheckBuildServer extends ServerMessage implements Serializable {
    @Override
    public void Accept(ViewInterface smh) {smh.handleMessage(this);}

    public ArrayList<SnapCell> sc;

    public CheckBuildServer(String name, ArrayList<SnapCell> sc){
        this.name=name;
        this.sc=sc;
    }
}
