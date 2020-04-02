package it.polimi.ingsw.messages.clientMessages;

import it.polimi.ingsw.controller.ClientMessageHandler;
import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.model.cards.CardName;

public class ChallengerChoseClient implements ClientMessage {

    @Override
    public void Accept(ClientMessageHandler cmh) {cmh.handleMessage(this);}

    CardName c1;
    CardName c2;
    CardName c3;

    public ChallengerChoseClient(CardName c1, CardName c2, CardName c3) {
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
    }

}
