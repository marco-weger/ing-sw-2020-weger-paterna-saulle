package it.polimi.ingsw.commons;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.view.ViewInterface;

public abstract class ServerMessage {
   public String name;
   public abstract void Accept(ViewInterface smh);
}
