package it.polimi.ingsw.model;

import it.polimi.ingsw.cards.Card;
import it.polimi.ingsw.cards.CardName;
import it.polimi.ingsw.cards.FactoryCard;

public class Player {

    private String name;
    private Card card;
    private Worker worker1;
    private Worker worker2;
   // private boolean hasLost;

    public Player(String name, CardName card, Worker worker1, Worker worker2)
    {
        this.name = name;
        this.card = FactoryCard.getCard(card);
        this.worker1 = worker1;
        this.worker2 = worker2;
        //this.hasLost = false;
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

    public void setCard(Card card) {
        this.card = card;
    }

    public Worker getWorker1() {
        return worker1;
    }

    public void setWorker1(Worker worker1) {
        this.worker1 = worker1;
    }

    public Worker getWorker2() {
        return worker2;
    }

    public void setWorker2(Worker worker2) {
        this.worker2 = worker2;
    }

    /*public void setHasLost(boolean lost) {
            this.hasLost = lost;
    }

    public boolean isHasLost() {
        return hasLost;
    }*/

    public Worker getCurrentWorker(){
         if(getWorker1().isActive())
             return worker1;
         else if(getWorker2().isActive())
             return worker2;
         else return null;
    }

    public void setCurrentWorker(int i)
    {
        if(i==1||i==2) {
            worker1.setActive(i == 1);
            worker2.setActive(i == 2);
        }
    }

}
