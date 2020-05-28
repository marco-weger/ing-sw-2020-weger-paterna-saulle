package it.polimi.ingsw.commons;

import it.polimi.ingsw.view.ViewInterface;

import java.io.Serializable;

public abstract class ServerMessage implements Serializable {

   private static final long serialVersionUID = 86786458382L;

   public abstract void accept(ViewInterface vi);
   public ServerMessage(){}
}
