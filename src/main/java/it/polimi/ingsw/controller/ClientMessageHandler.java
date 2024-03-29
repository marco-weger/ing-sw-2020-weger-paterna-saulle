package it.polimi.ingsw.controller;

import it.polimi.ingsw.commons.clientmessages.*;

public interface ClientMessageHandler {
    void handleMessage(ConnectionClient message);
    void handleMessage(DisconnectionClient message);
    void handleMessage(ReConnectionClient message);
    void handleMessage(PingClient message);
    void handleMessage(ChallengerChoseClient message);
    void handleMessage(PlayerChoseClient message);
    void handleMessage(WorkerInitializeClient message);
    void handleMessage(WorkerChoseClient message);
    void handleMessage(AnswerAbilityClient message);
    void handleMessage(MoveClient message);
    void handleMessage(BuildClient message);
    void handleMessage(ModeChoseClient message);
    void handleMessage(EasterEggClient easterEggClient);
}
