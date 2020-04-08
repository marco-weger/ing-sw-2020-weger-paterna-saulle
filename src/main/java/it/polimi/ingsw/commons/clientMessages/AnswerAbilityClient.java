package it.polimi.ingsw.commons.clientMessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.model.Status;

import java.io.Serializable;

public class AnswerAbilityClient extends ClientMessage implements Serializable {
    @Override
    public void Accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    public Status type;
    public boolean ability;

    /**
     * @param name the sender
     * @param ability The decision about switch on or off the ability
     * @param type The current status of the player
     * this method send to the server the decision of the player about switch on or not his God ability.
     */
    public AnswerAbilityClient(String name, boolean ability, Status type)
    {
        this.name=name;
        this.ability=ability;
        this.type=type;
    }
}
