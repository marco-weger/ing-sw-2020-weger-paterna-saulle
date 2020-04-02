package it.polimi.ingsw.view.server;

import it.polimi.ingsw.messages.ServerMessage;

public interface ServerMessageView extends ServerMessage {

    public void handleMessage();
}
