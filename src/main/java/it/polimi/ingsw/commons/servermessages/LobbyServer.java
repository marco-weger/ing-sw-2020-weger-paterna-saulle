package it.polimi.ingsw.commons.servermessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.view.ViewInterface;

import java.util.List;

public class LobbyServer extends ServerMessage {
    @Override
    public void accept(ViewInterface vi) {vi.handleMessage(this);}

    public final List<String> players;

    /**
     * 1 new lobby - 2 loaded - others normal
     */
    public int type;

    /**
     * The players receive this message after choosing which mode
     * they want to play.
     * @param players list of players in current Lobby
     */

    public LobbyServer(List<String> players){
        super("");
        this.players=players;
        this.type=0;
    }
}
