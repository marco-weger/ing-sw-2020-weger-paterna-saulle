package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.view.ViewInterface;

public class CurrentStatusServer extends ServerMessage {
    @Override
    public void accept(ViewInterface vi) {vi.handleMessage(this);}

    public final String player;
    public final Status status;

    /**
     * True if the player could move the first worker
     */
    public boolean worker1;

    /**
     * True if the player could move the second worker
     */
    public boolean worker2;

    /**
     * Timer in second if set
     */
    public int timer;

    /**
     * The player receives this message upon a request of the server,
     * informing him of the current status.
     * @param player name of the player
     * @param status current status of the player
     */

    public CurrentStatusServer(String player, Status status){
        super("");
        this.status=status;
        this.player=player;
        this.timer=0;
    }
}
