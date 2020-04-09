package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.view.ViewInterface;

import java.io.Serializable;

public class QuestionAbilityServer extends ServerMessage implements Serializable {
    @Override
    public void Accept(ViewInterface smh) {smh.handleMessage(this);}

    public QuestionAbilityServer(String name, String ip){
        this.name=name;
        this.ip=ip;
    }
}