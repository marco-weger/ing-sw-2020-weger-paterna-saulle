package it.polimi.ingsw;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.commons.servermessages.ReConnectionServer;
import it.polimi.ingsw.model.Match;

import java.util.ArrayList;
import java.util.Arrays;

public class Observable {

    final ArrayList<Observer> observers = new ArrayList<>();

    public void addObserver(Observer o){
        if(observers.contains(o))
            throw new RuntimeException("This observer is already registered");
        observers.add(o);
    }

    public void clearObserver(){
        observers.clear();
    }

    public void notifyObservers(Object obj){
        for(Observer i : observers)
            i.update(obj);
        if(this instanceof Match && obj instanceof ServerMessage && !Arrays.asList(Status.NAME_CHOICE,Status.WORKER_CHOICE,Status.CARD_CHOICE).contains(((Match) this).getStatus()) && !(obj instanceof ReConnectionServer))
            ((Match) this).saveToFile((ServerMessage) obj);
    }

}
