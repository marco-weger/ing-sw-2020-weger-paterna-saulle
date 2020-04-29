package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.view.ViewInterface;

public class ReConnectionServer extends ServerMessage {
    @Override
    public void accept(ViewInterface vi) {vi.handleMessage(this);}

    public ReConnectionServer(String name) {
        super(name);
    }
}
