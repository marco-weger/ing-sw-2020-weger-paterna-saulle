package it.polimi.ingsw;

public interface Observer {

    /**
     * It is called when there is a notify
     * @param obj
     */
    void update(Object obj);

}
