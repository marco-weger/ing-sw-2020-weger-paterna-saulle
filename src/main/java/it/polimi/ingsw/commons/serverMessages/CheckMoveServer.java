package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.view.server.ServerMessageHandler;

import java.io.Serializable;
import java.util.ArrayList;

public class CheckMoveServer implements ServerMessage, Serializable {
    @Override
    public void Accept(ServerMessageHandler smh) {smh.handleMessage(this);}

    ArrayList<SnapCell> sc;

    public CheckMoveServer(ArrayList<SnapCell> sc){
        this.sc=sc;
    }
}