package it.polimi.ingsw.commons.servermessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.view.ViewInterface;

public class QuestionAbilityServer extends ServerMessage {
    @Override
    public void accept(ViewInterface vi) {vi.handleMessage(this);}

    public final Status status;

    /**
     * The player receives this message when his card
     * needs him to decide to use the ability in a certain status
     * (only made if the card needs it).
     * @param status current status of the player
     */
    public QuestionAbilityServer(Status status){
        super();
        this.status= status;
    }
}