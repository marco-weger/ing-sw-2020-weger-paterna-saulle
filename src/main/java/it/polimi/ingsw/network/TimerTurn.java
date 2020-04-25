package it.polimi.ingsw.network;

import java.util.TimerTask;

public class TimerTurn extends TimerTask {

    /**
     * The client
     */
    ServerClientHandler sch;

    /**
     * The value of this time in seconds
     */
    int maxTimer;

    /**
     * Counter of the timer
     */
    int count;

    /**
     * @param sch the client
     * @param maxTimer the value of this timer in seconds
     */
    public TimerTurn(ServerClientHandler sch, int maxTimer){
        this.sch = sch;
        this.maxTimer = maxTimer;
        this.count = 0;
    }

    /**
     * The timer task runs every 1 second and send the countdown to the client
     */
    @Override
    public void run() {
        if(++count==maxTimer){
            sch.turnTimesUp = true;
            this.cancel();
        } else sch.countdown(maxTimer-count);
    }
}