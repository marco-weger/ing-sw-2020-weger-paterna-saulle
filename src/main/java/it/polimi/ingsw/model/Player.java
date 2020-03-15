package it.polimi.ingsw.model;

public class Player {

    private String name;
    private Card card;
    private Worker worker1;
    private Worker worker2;
    private boolean hasLost;

    public Worker getCurrentWorker(){return worker1;}

    public void setCurrentWorker(int i)
    {
        worker1.setActive(i==1);
        worker1.setActive(i==2);
    }

}
