package it.polimi.ingsw.commons.clientMessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.model.Status;

import java.io.Serializable;

public class AnswerAbilityClient implements ClientMessage, Serializable {

    @Override
    public void Accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    public String name;
    public boolean ability;

    /**
     * this method send to the server the decision of the player about switch on or not his God ability.
     * BEFORE_M or BEFORE_B
     */
    public Status type;

    public AnswerAbilityClient(String name, boolean ability, Status type)
    {
        this.name=name;
        this.ability=ability;
        this.type=type;
    }
}
