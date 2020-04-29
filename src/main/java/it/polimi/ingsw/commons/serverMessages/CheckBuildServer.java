package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.view.ViewInterface;

import java.util.ArrayList;

public class CheckBuildServer extends ServerMessage {
    @Override
    public void accept(ViewInterface vi) {vi.handleMessage(this);}

    public ArrayList<SnapCell> sc;

    /**
     * The player receives a list of buildable cells
     * @param sc list of cells in board (snapshot of the board)
     */

    public CheckBuildServer(String name, ArrayList<SnapCell> sc){
        super(name);
        this.sc=sc;
    }
}
