package it.polimi.ingsw.commons.servermessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.view.ViewInterface;

import java.util.HashMap;

public class EasterEggServer extends ServerMessage {
    @Override
    public void accept(ViewInterface vi) {vi.handleMessage(this);}

    public HashMap<String, Integer> win;
    public AvailableCardServer last;
    public String player;

    public EasterEggServer(String player, HashMap<String, Integer> win){
        super();
        this.player=player;
        this.win=win;
        last=null;
    }
}
