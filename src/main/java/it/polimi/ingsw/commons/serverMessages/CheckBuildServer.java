package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.network.ServerMessageHandler;

import java.io.Serializable;
import java.util.ArrayList;

public class CheckBuildServer implements ServerMessage, Serializable {
    @Override
    public void Accept(ServerMessageHandler smh) {smh.handleMessage(this);}

    public String name;
    public ArrayList<SnapCell> sc;

    public CheckBuildServer(String name, ArrayList<SnapCell> sc){
        this.name=name;
        this.sc=sc;
    }
}