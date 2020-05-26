package it.polimi.ingsw.commons.servermessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.view.ViewInterface;

public class SomeoneWinServer extends ServerMessage {
    @Override
    public void accept(ViewInterface vi) {vi.handleMessage(this);}

    public final String player;

    public final boolean isTimesUp;

    /**
     * The players receive this message when someone win
     * @param player name of the winner
     */
    public SomeoneWinServer(String player, boolean isTimesUp){
        //super("");
        super();
        this.player=player;
        this.isTimesUp=isTimesUp;
    }
}