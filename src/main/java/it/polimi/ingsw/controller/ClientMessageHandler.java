package it.polimi.ingsw.controller;

import it.polimi.ingsw.messages.ClientMessage;

public interface ClientMessageHandler {



    void reciveMessage(ClientMessage);

    void handleMessage();

}
