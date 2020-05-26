package it.polimi.ingsw.commons.servermessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.view.ViewInterface;

public class NameRequestServer extends ServerMessage {
    @Override
    public void accept(ViewInterface vi) {vi.handleMessage(this);}

    public boolean isFirstTime;

    /**
     * It informs the server if the player has already entered the server
     * or is the first connection.
     * (this is for permanence and connection handling purpose)
     * @param isFirstTime if the player has already made a connection
     */

    public NameRequestServer(boolean isFirstTime){
        //super("");
        super();
        this.isFirstTime = isFirstTime;
    }
}
