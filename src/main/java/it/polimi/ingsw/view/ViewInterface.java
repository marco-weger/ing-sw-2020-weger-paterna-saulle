package it.polimi.ingsw.view;

import it.polimi.ingsw.commons.servermessages.*;

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
    void handleMessage(PingServer message);
    void handleMessage(TimeOutServer message);
    void handleMessage(ReConnectionServer message);
    void handleMessage(EasterEggServer easterEggServer);

    void displayFirstWindow();
    void statusHandler(CurrentStatusServer message);
    void close(boolean isError);
    void displayBoard();
}
