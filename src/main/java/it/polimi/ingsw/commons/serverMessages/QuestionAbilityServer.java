package it.polimi.ingsw.commons.serverMessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.network.ServerMessageHandler;

import java.io.Serializable;

public class QuestionAbilityServer implements ServerMessage, Serializable {
    @Override
    public void Accept(ServerMessageHandler smh) {smh.handleMessage(this);}

    public String name;

    public QuestionAbilityServer(String name){
        this.name=name;
    }
}