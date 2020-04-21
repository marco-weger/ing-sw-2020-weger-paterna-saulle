package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.view.ViewInterface;

import java.util.ArrayList;

public class BuiltServer extends ServerMessage {
    @Override
    public void accept(ViewInterface vi) {vi.handleMessage(this);}

    public SnapCell sc;

    /**
     * The players receive this message with the modified board, after a built
     * @param sc cell modified after built
     */

    public BuiltServer(SnapCell sc){
        super("");
        this.sc = sc;
    }
}
