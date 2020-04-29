package it.polimi.ingsw.commons.clientMessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.model.cards.CardName;

import java.util.ArrayList;

public class ChallengerChoseClient extends ClientMessage {
    @Override
    public void accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    public ArrayList<CardName> c;

    /**
     * This messagge will be send to the server from the client with the three or two cards picked from the deck by the challenger
     * @param name the name of the challenger
     * @param c ths list of card pick from the deck
     */
    public ChallengerChoseClient(String name, ArrayList<CardName> c) {
        super(name);
        this.c = c;
    }

}
