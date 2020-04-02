package it.polimi.ingsw.view.server;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.commons.serverMessages.CheckBuildServer;
import it.polimi.ingsw.commons.serverMessages.CheckMoveServer;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

public class VirtualView extends Observable implements Observer, ServerMessageHandler {

    private ArrayList<Player> playersName;

    @Override
    public void update(Object obj) {

    }


    @Override
    public void handleMessage(CheckMoveServer cms) {

    }

    @Override
    public void handleMessage(CheckBuildServer cbs) {

    }

    @Override
    public void Accept(ServerMessageHandler smh) {

    }
}
