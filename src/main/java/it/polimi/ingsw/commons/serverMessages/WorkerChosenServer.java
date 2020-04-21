package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.view.ViewInterface;

import java.io.Serializable;

public class WorkerChosenServer extends ServerMessage {
    @Override
    public void accept(ViewInterface vi) {vi.handleMessage(this);}

    public String player;
    public int worker,x,y;

    /**
     * The player receives this message after selecting
     * a worker on the board or setting up its position
     * for the first time.
     * @param player name of the player
     * @param worker worker selected
     * @param x row selected
     * @param y column selected
     */

    public WorkerChosenServer(String player, int worker, int x, int y){
        super("");
        this.worker=worker;
        this.player=player;
        this.x=x;
        this.y=y;
    }
}
