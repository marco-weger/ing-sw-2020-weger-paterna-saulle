package it.polimi.ingsw.model;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.commons.serverMessages.CardChosenServer;
import it.polimi.ingsw.commons.serverMessages.QuestionAbilityServer;
import it.polimi.ingsw.commons.serverMessages.WorkerChosenServer;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.CardName;
import it.polimi.ingsw.model.cards.FactoryCard;

public class Player extends Observable {

    /**
     * Username
     */
    private String name;

    /**
     * His card, must be assigned by FACTORY class
     */
    private Card card;

    /**
     * Workers
     */
    private Worker worker1,worker2;

    /**
     * True if this is the current player
     */
    private boolean active;

    public Player(String name)
    {
        this.name = name;
        this.card = null; //FactoryCard.getCard(card);
        this.worker1 = null;
        this.worker2 = null;
        this.active = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Card getCard() {
        return card;
    }

    /**
     * The card is instanced by FACTORY class, all players will be notified
     * @param card value from card ENUM
     */
    public void setCard(CardName card) {
        this.card = FactoryCard.getCard(card);
        notifyObservers(new CardChosenServer(name,card));
    }

    public Worker getWorker1() {
        return worker1;
    }

    /**
     * The first worker is instanced, all players will be notified
     * @param worker1 the istance of worker
     */
    public void setWorker1(Worker worker1) {
        this.worker1 = worker1;
        notifyObservers(new WorkerChosenServer(name,1,worker1.getRow(),worker2.getRow()));
    }

    public Worker getWorker2() {
        return worker2;
    }

    public void setWorker2(Worker worker2) {
        this.worker2 = worker2;
    }

    public void setActive(boolean active){
        this.active=active;
    }

    public boolean isActive(){
        return active;
    }

    /**
     *a method to get the current worker
     */
    public Worker getCurrentWorker(){
         if(getWorker1().isActive())
             return worker1;
         else if(getWorker2().isActive())
             return worker2;
         else return null;
    }

    /**
     * Only one worker a time could be active
     * @param i 1 first worker, 2 the second one
     */
    public void setCurrentWorker(int i)
    {
        if(i==1||i==2) {
            worker1.setActive(i == 1);
            worker2.setActive(i == 2);
        }
    }

    /**
     * When called notify the view that it's the moment to make the quest to user
     */
    public void doQuestion(){
        notifyObservers(new QuestionAbilityServer(name));
    }
}
