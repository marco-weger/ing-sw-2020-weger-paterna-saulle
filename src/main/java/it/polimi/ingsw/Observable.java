package it.polimi.ingsw;

import java.util.ArrayList;

public class Observable {

    ArrayList<Observer> observers = new ArrayList<>();

    public void addObserver(Observer o){

        if(observers.contains(o))
            throw new RuntimeException("This observer is already registered");

        observers.add(o);

    }

    public void notifyObservers(Object obj){

        for(Observer i : observers)
            i.notifyObserver(obj);

    }

}
