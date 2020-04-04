package it.polimi.ingsw.network;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.commons.serverMessages.*;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

public class VirtualView extends Observable implements Observer, ServerMessageHandler {

    // TODO: the virtual view has to check name params of MESSAGE every time
    private ArrayList<Player> playersName;

    @Override
    public void update(Object obj) {

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
