package it.polimi.ingsw;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.model.Match;

import java.util.ArrayList;

public class Observable {

    ArrayList<Observer> observers = new ArrayList<>();

    public void addObserver(Observer o){
        if(observers.contains(o))
            throw new RuntimeException("This observer is already registered");
        observers.add(o);
    }

    /*
    public void removeObserver(Observer observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }
     */

    public void notifyObservers(Object obj){
        for(Observer i : observers)
            i.update(obj);
        if(this instanceof Match && obj instanceof ServerMessage)
            ((Match) this).saveToFile((ServerMessage) obj);
    }

}
