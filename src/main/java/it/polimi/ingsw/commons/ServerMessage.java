package it.polimi.ingsw.commons;

import it.polimi.ingsw.view.server.ServerMessageHandler;

public interface ServerMessage {

   void Accept(ServerMessageHandler smh);
}
