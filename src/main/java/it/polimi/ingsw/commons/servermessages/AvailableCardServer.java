package it.polimi.ingsw.commons.servermessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.model.cards.CardName;
import it.polimi.ingsw.view.ViewInterface;

import java.util.List;

public class AvailableCardServer extends ServerMessage {
    @Override
    public void accept(ViewInterface vi) {vi.handleMessage(this);}

    public final List<CardName> cardName;
    public String player;

    /**
     * The challenger will receive this message with an empty ArrayList
     * @param cardName list of available
     */
    public AvailableCardServer(String player, List<CardName> cardName){
        this.player=player;
        this.cardName=cardName;
    }
}