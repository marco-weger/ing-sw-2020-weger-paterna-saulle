package it.polimi.ingsw.commons.clientMessages;

import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.network.ServerClientHandler;

public class ModeChoseClient extends ClientMessage {
    /**
     * This messagge will be send to the server from the client the Game Mode Chosen by the player [2 or 3]
     * Controller doesn't use this message
     * @param cmh nullable
     */
    @Override
    @Deprecated
    public void accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    public final int mode;
    public ServerClientHandler sch;

    public ModeChoseClient(String name, int mode){
        super(name);
        this.mode=mode;
        this.sch=null;
    }
}
