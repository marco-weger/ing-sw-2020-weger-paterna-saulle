package it.polimi.ingsw.commons.servermessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.view.ViewInterface;

import java.util.List;

public class CheckMoveServer extends ServerMessage {
    @Override
    public void accept(ViewInterface vi) {vi.handleMessage(this);}

    public final List<SnapCell> sc;

    /**
     * The player receives a list of movable cells
     * @param sc list of cells in board (snapshot of the board)
     */

    public CheckMoveServer(List<SnapCell> sc){
        super();
        this.sc=sc;
    }
}
