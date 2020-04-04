package it.polimi.ingsw.network;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.serverMessages.*;

public interface ServerMessageHandler extends ServerMessage {

    void handleMessage(CheckMoveServer message);
    void handleMessage(CheckBuildServer message);
    void handleMessage(CardChosenServer message);
    void handleMessage(WorkerChosenServer message);
    void handleMessage(QuestionAbilityServer message);
    void handleMessage(CurrentStatusServer message);
    void handleMessage(SomeoneLoseServer message);
    void handleMessage(AvailableCardServer message);
    void handleMessage(SomeoneWinServer message);

}
