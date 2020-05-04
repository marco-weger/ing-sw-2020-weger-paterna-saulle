package it.polimi.ingsw.commons.servermessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.view.ViewInterface;

public class SomeoneWinServer extends ServerMessage {
    @Override
    public void accept(ViewInterface vi) {vi.handleMessage(this);}

    public final String player;

    /**
     * The players receive this message when someone win
     * @param player name of the winner
     */
    public SomeoneWinServer(String player){
        super("");
        this.player=player;
    }
}