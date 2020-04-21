package it.polimi.ingsw.commons.clientMessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.model.cards.CardName;

import java.io.Serializable;
import java.util.ArrayList;

public class ChallengerChoseClient extends ClientMessage {
    @Override
    public void accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    public ArrayList<CardName> c;

    /**
     * @param name the name of the challenger
     * @param c ths list of card pick from the deck
     * this method send to the server the three or two cards picked from the deck by the challenger
     */
    public ChallengerChoseClient(String name, ArrayList<CardName> c) {
        this.name = name;
        this.c = c;
    }

}
