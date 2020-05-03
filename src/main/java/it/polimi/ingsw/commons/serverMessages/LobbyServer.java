package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.view.ViewInterface;

import java.util.ArrayList;

public class LobbyServer extends ServerMessage {
    @Override
    public void accept(ViewInterface vi) {vi.handleMessage(this);}

    public final ArrayList<String> players;
    public final boolean loaded;

    /**
     * The players receive this message after choosing which mode
     * they want to play.
     * @param players list of players in current Lobby
     */

    public LobbyServer(ArrayList<String> players){
        super("");
        this.players=players;
        this.loaded=false;
    }
}
