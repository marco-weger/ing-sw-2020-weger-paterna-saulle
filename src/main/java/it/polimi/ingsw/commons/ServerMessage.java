package it.polimi.ingsw.commons;

import it.polimi.ingsw.view.ViewInterface;

import java.io.Serializable;

public abstract class ServerMessage implements Serializable {
   public String name;
   public String ip;
   public abstract void accept(ViewInterface vi);
}
