package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.model.cards.CardName;
import it.polimi.ingsw.view.ViewInterface;

import java.io.Serializable;

public class CardChosenServer  extends ServerMessage {
    @Override
    public void accept(ViewInterface vi) {vi.handleMessage(this);}

    public String player;
    public CardName cardName;

    public CardChosenServer(String player, CardName cardName){
        super("");
        this.cardName=cardName;
        this.player=player;
    }
}
