package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.view.ViewInterface;

import java.io.Serializable;

public class QuestionAbilityServer extends ServerMessage {
    @Override
    public void accept(ViewInterface vi) {vi.handleMessage(this);}

    public String name;
    public Status status;


    public QuestionAbilityServer(String name, Status status){
        super(name);
        this.status= status;
        this.name=name;
    }
}