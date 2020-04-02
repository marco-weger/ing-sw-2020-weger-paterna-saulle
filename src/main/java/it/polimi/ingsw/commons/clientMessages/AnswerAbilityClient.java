package it.polimi.ingsw.commons.clientMessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.commons.ClientMessage;

import java.io.Serializable;

public class AnswerAbilityClient implements ClientMessage, Serializable {

    @Override
    public void Accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    String name;
    boolean ability;

    /**
     * 0 - BEFORE_M question
     * 1 - BEFORE_B question
     */
    int type;

    public AnswerAbilityClient(String name, boolean ability, int type)
    {
        this.name=name;
        this.ability=ability;
        this.type=type;
    }
}
