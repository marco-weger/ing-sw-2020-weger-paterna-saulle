package it.polimi.ingsw.commons.clientmessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.commons.Status;

public class AnswerAbilityClient extends ClientMessage {
    @Override
    public void accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    public final Status type;
    public final boolean ability;

    /**
     * This messagge will be send to the server from the client with the decision of the current player about switch on or not his God ability.
     * @param name the sender
     * @param ability The decision about switch on or off the ability
     * @param type The current status of the player
     */
    public AnswerAbilityClient(String name, boolean ability, Status type)
    {
        super(name);
        this.ability=ability;
        this.type=type;
    }
}
