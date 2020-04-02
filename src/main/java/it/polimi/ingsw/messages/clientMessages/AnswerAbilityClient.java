package it.polimi.ingsw.messages.clientMessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.messages.ClientMessage;

public class AnswerAbilityClient implements ClientMessage {

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
