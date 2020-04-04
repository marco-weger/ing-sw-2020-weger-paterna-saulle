package it.polimi.ingsw.commons.clientMessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.model.cards.CardName;

import java.io.Serializable;
import java.util.ArrayList;

public class ChallengerChoseClient implements ClientMessage, Serializable {

    @Override
    public void Accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    public String name;
    public ArrayList<CardName> c;

    public ChallengerChoseClient(String name, ArrayList<CardName> c) {
        this.name = name;
        this.c = c;
    }

}
