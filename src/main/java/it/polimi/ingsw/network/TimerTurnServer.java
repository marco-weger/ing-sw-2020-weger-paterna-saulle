package it.polimi.ingsw.network;

import java.util.TimerTask;

public class TimerTurnServer extends TimerTask {

    /**
     * The client
     */
    final ServerClientHandler sch;

    /**
     * @param sch the client
     */
    public TimerTurnServer(ServerClientHandler sch){
        System.out.println("[TIMER] - " + sch.getName());
        this.sch = sch;
    }

    /**
     * The timer task runs every 1 second and send the countdown to the client
     */
    @Override
    public void run() {
        System.out.println("[TIMER END] - " + sch.getName());
        sch.timesUp();
        this.cancel();
    }
}