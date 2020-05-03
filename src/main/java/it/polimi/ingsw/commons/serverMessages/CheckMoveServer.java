package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.view.ViewInterface;

import java.util.ArrayList;

public class CheckMoveServer extends ServerMessage {
    @Override
    public void accept(ViewInterface vi) {vi.handleMessage(this);}

    public final ArrayList<SnapCell> sc;

    /**
     * The player receives a list of movable cells
     * @param sc list of cells in board (snapshot of the board)
     */

    public CheckMoveServer(String name, ArrayList<SnapCell> sc){
        super(name);
        this.sc=sc;
    }
}
