package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.view.ViewInterface;

import java.io.Serializable;

public class SomeoneLoseServer extends ServerMessage {
    @Override
    public void accept(ViewInterface vi) {vi.handleMessage(this);}

    public String player;

    /**
     * The players receive this message when someone lose
     * @param player name of the loser
     */

    public SomeoneLoseServer(String player){
        super("");
        this.player=player;
    }
}