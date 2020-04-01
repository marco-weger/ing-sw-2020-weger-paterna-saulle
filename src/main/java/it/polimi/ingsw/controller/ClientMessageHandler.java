package it.polimi.ingsw.controller;

import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.messages.clientMessage.*;

public interface ClientMessageHandler {

    void reciveMessage(ClientMessage cm);

    void handleMessage(ConnectionClient message);
    void handleMessage(DisconnectionClient message);
    void handleMessage(ReConnectionClient message);
    void handleMessage(Ping event);

    void handleMessage(ChallengerChoseClient message);
    void handleMessage(PlayerChoseClient message);
    void handleMessage(WorkerInizializeClient message);
    void handleMessage(WorkerChoseClient message);
    void handleMessage(AnswerAbilityClient message);
    void handleMessage(MoveClient message);
    void handleMessage(BuildClient message);
}
