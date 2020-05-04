package it.polimi.ingsw.commons.servermessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.view.ViewInterface;

public class TimeOutServer extends ServerMessage {
    @Override
    public void accept(ViewInterface vi) {vi.handleMessage(this);}

    public final int n;
    public final int of;
    public final String player;

    public TimeOutServer(String name, String player, int n, int of){
        super(name);
        this.player=player;
        this.n=n;
        this.of=of;
    }
}
