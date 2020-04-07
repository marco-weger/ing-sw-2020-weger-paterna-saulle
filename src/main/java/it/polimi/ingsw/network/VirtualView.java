package it.polimi.ingsw.network;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.serverMessages.*;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

public class VirtualView extends Observable implements Observer, ServerMessageHandler {

    // TODO: the virtual view has to check name params of MESSAGE every time
    private ArrayList<Player> playersName;
    private Server server;

    public VirtualView(Server server){
        this.server = server;
    }

    @Override
    public void update(Object arg) {
        if( ! (arg instanceof ClientMessage))
            throw new RuntimeException("This must be an ClientMessage object");
        ServerMessage cm = (ServerMessage) arg;
        cm.Accept(this);

        // TODO: not sure this is the best way to call correct method... test!
        // new Thread(() -> cm.Accept(this)).start();
    }


    @Override
    public void handleMessage(CheckMoveServer cms) {
        // TODO: let client know where to move
    }

    @Override
    public void handleMessage(CheckBuildServer cbs) {

    }

    @Override
    public void handleMessage(CardChosenServer message) {

    }

    @Override
    public void handleMessage(WorkerChosenServer message) {

    }

    @Override
    public void handleMessage(QuestionAbilityServer message) {

    }

    @Override
    public void handleMessage(CurrentStatusServer message) {

    }

    @Override
    public void handleMessage(SomeoneLoseServer message) {

    }

    @Override
    public void handleMessage(AvailableCardServer message) {

    }

    @Override
    public void handleMessage(SomeoneWinServer message) {

    }

    @Override
    public void Accept(ServerMessageHandler smh) {

    }
}
