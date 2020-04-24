package it.polimi.ingsw.network;

import java.util.TimerTask;

public class TimerTurn extends TimerTask {

    ServerClientHandler sch;
    int maxTimer;
    int count;

    public TimerTurn(ServerClientHandler sch, int maxTimer){
        this.sch = sch;
        this.maxTimer = maxTimer;
        this.count = 0;
    }

    /**
     * The action to be performed by this timer task.
     */
    @Override
    public void run() {
        if(++count==maxTimer){
            sch.turnTimesUp = true;
            this.cancel();
        } else sch.countdown(maxTimer-count);
    }
}