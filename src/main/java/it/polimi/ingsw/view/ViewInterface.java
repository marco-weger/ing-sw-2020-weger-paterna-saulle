package it.polimi.ingsw.view;

import it.polimi.ingsw.commons.serverMessages.*;

public interface ViewInterface {

    void handleMessage(CheckMoveServer message);
    void handleMessage(CheckBuildServer message);
    void handleMessage(CardChosenServer message);
    void handleMessage(WorkerChosenServer message);
    void handleMessage(QuestionAbilityServer message);
    void handleMessage(CurrentStatusServer message);
    void handleMessage(SomeoneLoseServer message);
    void handleMessage(AvailableCardServer message);
    void handleMessage(SomeoneWinServer message);
    void handleMessage(NameRequestServer message);
    void handleMessage(LobbyServer message);
    void handleMessage(ModeRequestServer message);
    void handleMessage(BuiltServer message);
    void handleMessage(MovedServer message);
    void handleMessage(PingServer pingServer);
    void handleMessage(CountdownServer countdownServer);
}
