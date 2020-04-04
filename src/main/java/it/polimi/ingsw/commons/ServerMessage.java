package it.polimi.ingsw.commons;

import it.polimi.ingsw.network.ServerMessageHandler;

public interface ServerMessage {

   void Accept(ServerMessageHandler smh);
}
