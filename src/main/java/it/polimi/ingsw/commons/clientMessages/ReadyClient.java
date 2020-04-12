package it.polimi.ingsw.commons.clientMessages;

import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.controller.ClientMessageHandler;

public class ReadyClient extends ClientMessage {
    /**
     * Controller doesn't use this message
     * @param cmh nullable
     */
    @Override
    @Deprecated
    public void accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    /**
     * Usinig this parameter the controller know if its the moment to start the match
     */
    public boolean start;

    public ReadyClient(String name){
        this.name=name;
        this.start=false;
    }
}
