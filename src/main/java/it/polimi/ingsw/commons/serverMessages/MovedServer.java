package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.SnapWorker;
import it.polimi.ingsw.view.ViewInterface;

public class MovedServer extends ServerMessage {
    @Override
    public void accept(ViewInterface vi) {vi.handleMessage(this);}

    public SnapWorker sw;

    /**
     * The players receive this message whit the modified board, after a movement
     * @param sw list of workers in board (snapshot of the workers)
     */

    public MovedServer(SnapWorker sw){
        super("");
        this.sw = sw;
    }
}
