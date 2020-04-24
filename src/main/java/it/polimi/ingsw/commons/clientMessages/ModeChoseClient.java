package it.polimi.ingsw.commons.clientMessages;

import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.network.ServerClientHandler;

public class ModeChoseClient extends ClientMessage {
    /**
     * Controller doesn't use this message
     * @param cmh nullable
     */
    @Override
    @Deprecated
    public void accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    public int mode;
    public ServerClientHandler sch;

    public ModeChoseClient(String name, int mode){
        super(name);
        this.mode=mode;
        this.sch=null;
    }
}
