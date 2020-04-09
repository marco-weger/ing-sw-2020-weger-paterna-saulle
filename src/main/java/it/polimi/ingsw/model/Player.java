package it.polimi.ingsw.model;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.commons.serverMessages.CardChosenServer;
import it.polimi.ingsw.commons.serverMessages.QuestionAbilityServer;
import it.polimi.ingsw.commons.serverMessages.WorkerChosenServer;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.CardName;
import it.polimi.ingsw.model.cards.FactoryCard;
import it.polimi.ingsw.network.VirtualView;

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
    private boolean current;

    /**
     * The ip address string
     */
    String ip;

    public Player(String name, String ip, VirtualView vw)
    {
        this.name = name;
        this.ip = ip;
        this.card = null; //FactoryCard.getCard(card);
        this.worker1 = null;
        this.worker2 = null;
        this.current = false;
        this.addObserver(vw);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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
    public void setCard(CardName card, VirtualView vv) {
        this.card = FactoryCard.getCard(card, vv);
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
        notifyObservers(new WorkerChosenServer(name,1,worker1.getRow(),worker1.getRow()));
    }

    public Worker getWorker2() {
        return worker2;
    }

    /**
     * The first worker is instanced, all players will be notified
     * @param worker2 the istance of worker
     */
    public void setWorker2(Worker worker2) {
        this.worker2 = worker2;
        notifyObservers(new WorkerChosenServer(name,1,worker2.getRow(),worker2.getRow()));
    }

    public void setCurrent(boolean current){
        this.current = current;
    }

    public boolean isCurrent(){
        return current;
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
        notifyObservers(new QuestionAbilityServer(name,ip));
    }
}
